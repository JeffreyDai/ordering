package com.wzqj.ordering.common.mongo.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MongoDB条件查询封装实体（支持limit，skip，sort）
 *
 * @author: wh
 * @time: 15-9-8 下午5:00
 * @version: 1.0
 */
public class MongoDBCursor extends MongoDBEntity {

    /**
     * skip属性
     */
    private int skip;

    /**
     * limit属性
     */
    private int limit = 10;

    /**
     * 排序属性
     */
    private Map<String, Object> sort = new LinkedHashMap<String, Object>();

    /**
     * 自定义查询字段属性
     */
    private Map<String, Object> customFieldMap = new HashMap<String, Object>();

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Map<String, Object> getSort() {
        return sort;
    }

    public void setSort(Map<String, Object> sort) {
        this.sort = sort;
    }

    /**
     * getSortObject
     */
    public DBObject getSortObject() {
        if (this.sort == null) {
            return null;
        }
        DBObject dbo = new BasicDBObject();
        for (String k : sort.keySet()) {
            dbo.put(k, Integer.valueOf(sort.get(k).toString()));
        }
        return dbo;
    }

    public Map<String, Object> getCustomFieldMap() {
        return customFieldMap;
    }

    public void setCustomFieldMap(Map<String, Object> customFieldMap) {
        this.customFieldMap = customFieldMap;
    }
}
