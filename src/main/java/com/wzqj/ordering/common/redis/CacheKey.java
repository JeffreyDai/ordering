package com.wzqj.ordering.common.redis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CacheKey {

    /**
     * redis的键前缀, 所有操作redis的key前缀应当在这里注册, 避免误覆盖别人存放的数据.
     * 以双斜线结尾
     */
    public static final String
            桌面点单商户订单编号 = "desktop/wang/order/mcode//";

    /**
     * 检查redisKey是否合法
     * @param key 唯一标识
     */
    public static void checkKey(String key) {
        for (String s : allConstants) {
            if (key.startsWith(s)) {
                return;
            }
        }
        throw new UnRegCacheKeyException(key);
    }

    private static List<String> allConstants = new ArrayList<String>();

    static {
        for (Field f : CacheKey.class.getFields()) {
            try {
                allConstants.add((String) f.get(new CacheKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
