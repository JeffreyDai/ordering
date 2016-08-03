package com.wzqj.ordering.common.mongo.model;

import java.util.Map;

/**
 * MongoDB更新封装实体
 *
 * @author: wh
 * @time: 15-9-8 下午5:00
 * @version: 1.0
 */
public class MongoDBUpdate extends MongoDBEntity {

    /**
     * where查询Map
     */
    private Map<String, Object> whereMap;

    /**
     * value查询Map
     */
    private Map<String, Object> valueMap;

    public Map<String, Object> getWhereMap() {
        return whereMap;
    }

    public void setWhereMap(Map<String, Object> whereMap) {
        this.whereMap = whereMap;
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }
}
