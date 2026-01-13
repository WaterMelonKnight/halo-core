package com.watermelon.halo.common;

/**
 * Distributed lock interface used by sidecars and services.
 */
public interface DistributedLock {
    /**
     * Try to acquire a lock for given key with expire time in seconds.
     * @param key lock key
     * @param expireTime seconds to live
     * @return true if acquired
     */
    boolean tryLock(String key, long expireTime);

    /**
     * Unlock given key (only if this instance holds the lock).
     * @param key lock key
     */
    void unlock(String key);

    /**
     * Renew (extend) the lock TTL for given key if this instance holds it.
     * @param key lock key
     * @param expireTime new TTL in seconds
     * @return true if renewed
     */
    boolean renew(String key, long expireTime);
}
