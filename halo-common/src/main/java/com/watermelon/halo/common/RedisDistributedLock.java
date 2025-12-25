package com.watermelon.halo.common;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis based distributed lock implementation.
 * <p>
 * Stores a per-key token locally to ensure only the owner may unlock/renew.
 */
@Component
public class RedisDistributedLock implements DistributedLock {
    private final StringRedisTemplate redisTemplate;

    // local map holding lock tokens for keys acquired by this instance
    private final Map<String, String> localTokens = new ConcurrentHashMap<>();

    // Lua: if value matches then delete -> return 1 else 0
    private static final String UNLOCK_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    // Lua: if value matches then pexpire -> return result else 0
    private static final String RENEW_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('pexpire',KEYS[1],ARGV[2]) else return 0 end";

    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean tryLock(String key, long expireTime) {
        String token = UUID.randomUUID().toString();
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, token, Duration.ofSeconds(expireTime));
        if (Boolean.TRUE.equals(result)) {
            localTokens.put(key, token);
            return true;
        }
        return false;
    }

    @Override
    public void unlock(String key) {
        String token = localTokens.remove(key);
        if (token == null) {
            return; // we don't hold the lock
        }
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(UNLOCK_LUA, Long.class);
        try {
            redisTemplate.execute(script, Collections.singletonList(key), token);
        } catch (Exception ignored) {
            // best-effort; do not rethrow to avoid crashing callers
        }
    }

    @Override
    public boolean renew(String key, long expireTime) {
        String token = localTokens.get(key);
        if (token == null) {
            return false;
        }
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(RENEW_LUA, Long.class);
        long millis = Duration.ofSeconds(expireTime).toMillis();
        Long res = 0L;
        try {
            res = redisTemplate.execute(script, Collections.singletonList(key), token, String.valueOf(millis));
        } catch (Exception e) {
            return false;
        }
        return res != null && res > 0;
    }
}
