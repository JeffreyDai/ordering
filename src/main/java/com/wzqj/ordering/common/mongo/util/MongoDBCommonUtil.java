package com.wzqj.ordering.common.mongo.util;

import com.mongodb.*;
import com.wzqj.ordering.common.mongo.model.MongoDBEntity;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * MongoDB集合操作工作类
 *
 * @author: wh
 * @time: 15-9-8 下午3:17
 * @version: 1.0
 */
public class MongoDBCommonUtil {

    private static Logger logger = Logger.getLogger(MongoDBCommonUtil.class);

    /**
     * 链接MongoDB
     *
     * @param host   主机地址
     * @param port   端口
     * @param dbName 数据库名字
     * @return DB
     */
    public static DB getDB(String host, int port, String dbName) {
        DB db = null;
        try {
            MongoClient client = new MongoClient(host, port);
            db = client.getDB(dbName);
            db.slaveOk();
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("获取DB出现异常！");
        }
        return db;
    }

    /**
     * getDB.
     */
    public static DB getDB(String host, int port, String dbName, String userName, String password) {
        DB db = null;
        try {
            ServerAddress serverAddress = new ServerAddress(host, port);
            List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
            serverAddresses.add(serverAddress);
            MongoCredential mongoCRCredential = MongoCredential.createMongoCRCredential(userName, 
                    dbName, password.toCharArray());
            List<MongoCredential> mongoCredentials = new ArrayList<MongoCredential>();
            mongoCredentials.add(mongoCRCredential);
            MongoClient client = new MongoClient(serverAddresses, mongoCredentials);
            db = client.getDB(dbName);
            ReadPreference readPreference = ReadPreference.secondaryPreferred();
            readPreference.isSlaveOk();
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("获取DB出现异常！");
        }
        return db;
    }

    /**
     * 链接MongoDB
     *
     * @param host 主机地址
     * @param port 端口
     * @return DB
     */
    public static List<String> getAllDB(String host, int port) {
        List<String> dbNames = new ArrayList<String>();
        try {
            Mongo mongo = new Mongo(host, port);
            dbNames = mongo.getDatabaseNames();
        } catch (UnknownHostException e) {
            dbNames = null;
            logger.error(e);
            throw new RuntimeException("获取所有DB时出现异常！");
        }
        return dbNames;
    }

    /**
     * 测试是否可以连接db
     * @param host   主机地址
     * @param port   端口
     * @param dbName 库名
     */
    public static void connectionTest(String host, int port, String dbName) {
        try {
            Mongo mongo = new Mongo(host, port);
            if (dbName != null && !"".equals(dbName)) {
                mongo.getDB(dbName);
                //DB db = mongo.getDB(dbName);
            }
            // db.authenticate(username, passwd)
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("测试连接出现异常！");
        }
    }

    /**
     * connectionTest.
     */
    public static void connectionTest(String host, int port, String dbName, String userName, String password) {
        try {
            Mongo mongo = new Mongo(host, port);
            if (dbName != null && !"".equals(dbName)) {
                DB db = mongo.getDB(dbName);
                db.authenticate(userName, password.toCharArray());
                db.slaveOk();
            }
            // db.authenticate(username, passwd)
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("测试连接出现异常！");
        }
    }

    /**
     * 获取集合(表)
     *
     * @param mongoDBEntity 实体
     * @return DB连接
     */
    public static DBCollection getCollection(MongoDBEntity mongoDBEntity) {
        return mongoDBEntity.getDb().getCollection(mongoDBEntity.getCollectionName());
    }

    /**
     * 获取所有集合名称
     *
     * @param db 操作DB
     * @return 返回所有集合（表）
     */
    public static List<String> getAllCollection(DB db) {
        List<String> result = new ArrayList<String>();
        for (String collectionName : db.getCollectionNames()) {
            result.add(collectionName);
        }
        return result;
    }

    /**
     * 创建集合
     *
     * @param mongoDBEntity 实体
     * @param options       操作
     */
    public static void createCollection(MongoDBEntity mongoDBEntity, DBObject options) {
        mongoDBEntity.getDb().createCollection(mongoDBEntity.getCollectionName(), options);
    }

    /**
     * 删除
     * @param mongoDBEntity 实体
     */
    public static void dropCollection(MongoDBEntity mongoDBEntity) {
        DBCollection collection = getCollection(mongoDBEntity);
        collection.drop();
    }

    /**
     * getCollectionFields.
     */
    public static List<String> getCollectionFields(MongoDBEntity mongoDBEntity) {
        List<String> result = new ArrayList<String>();
        String mapfun = "function() { " 
                + "for (var key in this) { " 
                + "   emit(key, null); "
                + "}" 
                + "}";
        String reducefun = "function(key, stuff) { "
                + "return null; " 
                + "}";
        MapReduceOutput mro = getCollection(mongoDBEntity).mapReduce(mapfun, reducefun, "keys", new BasicDBObject());
        Iterator<DBObject> it = mro.results().iterator();
        while (it.hasNext()) {
            DBObject dbObject = it.next();
            for (String key : dbObject.keySet()) {
                if (dbObject.get(key) != null) {
                    result.add(String.valueOf(dbObject.get(key)));
                }
            }
        }
        return result;
    }

    /**
     * main.
     */
    public static void main(String[] args) {

        DB db = MongoDBCommonUtil.getDB("127.0.0.1", 27017, "meal");
        DBCollection layout = db.getCollection("layout");
        DBObject one = layout.findOne();
        System.out.println(one.toString());
        BasicDBObject query = new BasicDBObject();
        query.put("name", "wanghao");
        BasicDBObject setValue = new BasicDBObject();
        setValue.put("height", 175);
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", setValue);
        WriteResult update = layout.update(query, updateObject, true, false);
        update.getLastConcern();
        DBCursor cursor = layout.find(query);
        System.out.printf(cursor.next().toString());
        long count = layout.count();
        System.out.println(count);
    }
}
