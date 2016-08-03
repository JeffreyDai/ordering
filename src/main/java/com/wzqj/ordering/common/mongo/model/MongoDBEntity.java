package com.wzqj.ordering.common.mongo.model;

import com.mongodb.DB;

import java.util.Map;

/**
 * MongoDB集合封装实体
 *
 * @author: wh
 * @time: 15-9-8 下午2:57
 * @version: 1.0
 */
public class MongoDBEntity {

    /**
     * mongo数据库
     */
    private DB db;

    /**
     * 集合名字
     */
    private String collectionName;

    /**
     * 字段封装Map
     */
    private Map<String, Object> fieldMap;

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Object> fieldMap) {
        this.fieldMap = fieldMap;
    }
}
