package com.wzqj.ordering.common.mongo.util;

import com.mongodb.DBObject;

/**
 * MongoDB更新操作接口定义
 *
 * @author: wh
 * @time: 15-9-8 下午5:25
 * @version: 1.0
 */
interface UpdateCallback {

    DBObject doCallback(DBObject valueDBObject);
}
