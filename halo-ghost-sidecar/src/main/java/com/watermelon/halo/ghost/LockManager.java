package com.watermelon.halo.ghost;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LockManager {
    private final StringRedisTemplate redis;

    public LockManager(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean tryLock(String key, long ttlSeconds) {
        Boolean success = redis.opsForValue().setIfAbsent(key, "locked", Duration.ofSeconds(ttlSeconds));
        return Boolean.TRUE.equals(success);
    }

    public void release(String key) {
        redis.delete(key);
    }
}
