package com.wzqj.ordering.common.redis;


public interface CacheCallback {

    /**
     * @return 由于返回的对象要序列化存入redis, 所以要全部实现Serializable接口,否则没有缓存效果
     */
    Object execute(CacheClient cacheClient);

}
