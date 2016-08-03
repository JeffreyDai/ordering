package com.wzqj.ordering.common.mongo.context;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Mongo执行计划日志
 * User: wh
 * Date: 15-3-20
 * Time: 下午2:09
 * To change this meal use File | Settings | File Templates.
 */
@Document(collection = "lagd_mongo_log")
public class MongoLogBean {

    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 方法
     */
    @Field("method")
    private String method;

    /**
     * 实体
     */
    @Field("entity")
    private String entity;

    /**
     * 条件
     */
    @Field("conditions")
    private String conditions;

    /**
     * 时长
     */
    @Field("time")
    private Long time;

    /**
     * 创建时间
     */
    @Field("create_date")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * getCreateDate.
     */
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
