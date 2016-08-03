package com.wzqj.ordering.common.mongo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Json数据转换工具类
 *
 * @author: wh
 * @time: 15-9-8 上午9:55
 * @version: 1.0
 */
public class JsonConvertUtil {

    private static Logger logger = Logger.getLogger(MongoDBCommonUtil.class);

    /**
     * Json数据转换为Map
     *
     * @param jsonStr json串
     * @return Map集合
     */
    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //最外层解析
            JSONObject json = JSONObject.parseObject(jsonStr);
            Set<String> keys = json.keySet();
            for (String key : keys) {
                Object value = json.get(key);
                //如果内层还是数组的话，继续解析
                if (value instanceof JSONArray) {
                    List<Object> list = new ArrayList<Object>();
                    for (Object temp : ((JSONArray) value).toArray()) {
                        list.add(temp);
                    }
                    map.put(key, list);
                } else if (value instanceof JSONObject) {
                    Map<String, Object> v = parseJSON2Map(value.toString());
                    map.put(key, v);
                } else {
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("转换Map失败！");
        }
        return map;
    }

}
