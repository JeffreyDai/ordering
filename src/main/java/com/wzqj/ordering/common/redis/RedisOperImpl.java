package com.wzqj.ordering.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisOperImpl implements CacheOper {

    private JedisPool jedisPool;

    public RedisOperImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Long del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.del(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public byte[] get(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void setex(byte[] key, int seconds, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.setex(key, seconds, value);
        } finally {
            jedisPool.returnResource(jedis); // must be
        }
    }

    @Override
    public long setnx(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setnx(key, value);
        } finally {
            jedisPool.returnResource(jedis); // must be
        }
    }

    @Override
    public Long ttl(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.ttl(key);
        } finally {
            jedisPool.returnResource(jedis); // must be
        }
    }


}
