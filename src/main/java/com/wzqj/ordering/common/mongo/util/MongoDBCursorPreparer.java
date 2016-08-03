package com.wzqj.ordering.common.mongo.util;

import com.mongodb.DBCursor;

/**
 * 查询转换接口定义
 *
 * @author: wh
 * @time: 15-9-8 下午4:55
 * @version: 1.0
 */
public interface MongoDBCursorPreparer {

    DBCursor prepare(DBCursor cursor);
}
