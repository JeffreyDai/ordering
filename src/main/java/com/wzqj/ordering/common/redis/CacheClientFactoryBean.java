package com.wzqj.ordering.common.redis;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import redis.clients.jedis.JedisPool;

/**
 * 使用FactoryBean控制CacheClient初始化
 *
 * @author wh
 * @date 2015年7月30日
 */
public class CacheClientFactoryBean implements FactoryBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        CacheClient client = new CacheClient();
        JedisPool jedisPool = (JedisPool) applicationContext.getBean("jedisPool");
        client.setImpl(new RedisOperImpl(jedisPool));
        return client;
    }

    @Override
    public Class getObjectType() {
        return CacheClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
