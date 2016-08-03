package com.wzqj.ordering.common.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;

/**
 * 缓存操作, 功能如下:
 * 1.根据配置文件
 * 2.向缓存中set值时,检查key是否注册
 * 3.加入getAndSet方法, 当get无值时或异常时,向缓存set值
 */
public class CacheClient {

    private static Logger log = LogManager.getLogger();
    private CacheOper impl;

    /**
     * 向redis存放对象, 并设置缓存时间.
     * value必须为jdk可序列化的对象, 一般要实现Serializable接口.
     * 包括String,byte[]都会被jdk序列化后再存入redis.
     * @param key 唯一标识
     * @param seconds 数据过期时间, 秒
     * @param value 该对象的关联对象需全部实现Serializable
     */
    public void setex(String key, int seconds, Object value) {
        CacheKey.checkKey(key);
        impl.setex(toBytes(key), seconds, ObjectSerializer.serialize(value));
    }

    /**
     * 获取KEY对应的对象.
     * @param key 唯一标识
     * @return 对象
     */
    public Object get(String key) {
        byte[] retBytes = impl.get(toBytes(key));
        if (retBytes == null || retBytes.length == 0) {
            return null;
        }
        return ObjectSerializer.deSerialize(retBytes);
    }

    /**
     * 删除KEY值对应的VALUE
     * @param keys 标识串
     */
    public void del(String... keys) {
        for (String s : keys) {
            impl.del(toBytes(s));
        }
    }

    /**
     * @param key 唯一标识
     * @param value 值
     */
    public long setnx(String key, Object value) {
        CacheKey.checkKey(key);
        byte[] valueBytes = ObjectSerializer.serialize(value);
        return impl.setnx(toBytes(key), valueBytes);
    }

    /**
     * 尝试从redis获得对象, 不成功时进行回调, 将回调得到的值放入redis中. 不向外抛出异常,以免redis出问题时影响主服务.
     * @param key 唯一标识
     * @param callback 指示如何生成值,以放入redis.
     */
    public Object getAndSetNx(String key, CacheCallback callback) {
        Object cache = null;
        try {
            cache = get(key);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        if (cache != null) {
            return cache;
        } else {
            Object o = callback.execute(this);
            try {
                if (o != null) {
                    setnx(key, o);
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
            return o;
        }
    }

    /**
     * 尝试从redis获得对象, 不成功时进行回调, 将回调得到的值放入redis中. 不向外抛出异常,以免redis出问题时影响主服务.
     *
     * @param key 唯一标识
     * @param expireSec 缓存时长
     * @param callback  指示如何生成值,以放入redis.
     */
    public Object getAndSetEx(String key, int expireSec, CacheCallback callback) {
        Object cache = null;
        try {
            cache = get(key);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        if (cache != null) {
            return cache;
        } else {
            Object o = callback.execute(this);
            try {
                if (o != null) {
                    setex(key, expireSec, o);
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
            return o;
        }
    }

    /**
     * @param key 唯一标识
     * @return long 当前key有效时间
     * @Title: ttl
     * @Description 返回key值的剩余有效时间
     */
    public Long ttl(String key) {
        return impl.ttl(toBytes(key));
    }

    public void setImpl(CacheOper impl) {
        this.impl = impl;
    }

    private byte[] toBytes(String s) {
        if (s == null) {
            return null;
        }
        try {
            return s.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
