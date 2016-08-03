package com.wzqj.ordering.common.redis;


/**
 * 封装jedis通用的操作方法
 */
public interface CacheOper {

    /**
     * 删除KEY值对应的VALUE
     */
    Long del(byte[] keys);

    /**
     * 获取KEY对应的字节数组.
     */
    byte[] get(byte[] key);

    /**
     * 向redis存放对象, 并设置缓存时间.
     * @param seconds 数据过期时间, 秒
     * @param value   该对象的关联对象需全部实现Serializable
     */
    void setex(byte[] key, int seconds, byte[] value);

    long setnx(byte[] key, byte[] value);

    /**
     * @return long    返回该key的有效时间
     * @Title: ttl
     * @Description:获取key的当前有效时间
     */
    Long ttl(byte[] key);
}
