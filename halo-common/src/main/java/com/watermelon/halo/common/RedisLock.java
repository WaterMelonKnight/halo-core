package com.watermelon.halo.common;

public interface RedisLock {
    /**
     * Try to acquire lock with given key and ttl seconds.
     * @return true if acquired
     */
    boolean tryLock(String key, long ttlSeconds);

    /**
     * Release lock
     */
    void release(String key);
}
