package com.wzqj.ordering.common.redis;

/**
 * 未在RedisKey中注册的redisKey
 */
public class UnRegCacheKeyException extends RuntimeException {

    public UnRegCacheKeyException() {
        super();
    }

    public UnRegCacheKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnRegCacheKeyException(String message) {
        super(message);
    }
}
