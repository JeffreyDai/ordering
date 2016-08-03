package com.wzqj.ordering.common.mongo.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.wzqj.ordering.common.mongo.context.MongoLogBean;
import com.wzqj.ordering.common.mongo.context.PageBean;
import com.wzqj.ordering.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * MongoDB基础DAO接口（实体类）
 * User: wh
 * Date: 15-9-8
 * Time: 下午5:58
 * To change this meal use File | Settings | File Templates.
 */
@Repository
public class EntityMongoBaseDao<T> {

    private static final Log log = LogFactory.getLog(EntityMongoBaseDao.class);

    private static final Integer TIME = 3000;

    private static final Integer INITVALUE = 10000000;

    @Resource
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 新建集合
     *
     * @param entity 集合对象
     * @return true ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean createCollection(T entity) throws DataAccessException {
        boolean flag = true;
        try {
            mongoTemplate.createCollection(entity.getClass());
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 新建集合
     *
     * @param collectionName 集合名称
     * @return true ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean createCollection(String collectionName) throws DataAccessException {
        boolean flag = true;
        try {
            mongoTemplate.createCollection(collectionName);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 新增文档
     *
     * @param entity ： 文档对象
     * @return true ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean insert(T entity) throws DataAccessException {
        boolean flag = true;
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field filed : fields) {
                if (filed.getAnnotations().length == 0) {
                    filed.setAccessible(true);
                    filed.set(entity, null);
                }
            }
            mongoTemplate.insert(entity);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        } catch (IllegalAccessException e) {
            flag = false;
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 批量新增文档
     *
     * @param entityCollection ： 文档对象列表
     * @param entityClass      ： 文档对象
     * @return true ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean insert(List<T> entityCollection, Class entityClass) throws DataAccessException {
        boolean flag = true;
        try {
            for (T entity : entityCollection) {
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field filed : fields) {
                    if (filed.getAnnotations().length == 0) {
                        filed.setAccessible(true);
                        filed.set(entity, null);
                    }
                }
            }
            mongoTemplate.insert(entityCollection, entityClass);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        } catch (IllegalAccessException e) {
            flag = false;
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 更新文档
     *
     * @param entity 文档对象
     * @return true ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean update(T entity) throws DataAccessException {
        return this.update(entity, true);
    }

    /**
     * 更新文档
     *
     * @param entity     文档对象
     * @param filterFlag 过滤空值标识
     * @return true ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean update(T entity, Boolean filterFlag) throws DataAccessException {
        boolean flag = true;
        try {
            Query query = new Query();
            Update update = new Update();

            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field filed : fields) {
                if (filed.getAnnotations().length > 0) {
                    filed.setAccessible(true);
                    Object filedValue = filed.get(entity);                              //获取属性值
                    Id idAnnotation = filed.getAnnotation(Id.class);
                    if (idAnnotation != null) {
                        //添加当前对象带有@Id注解的查询条件
                        query.addCriteria(Criteria.where(filed.getName()).is(filedValue));
                    } else {
                        //过滤空值
                        if (filterFlag) {
                            if (filedValue != null) {
                                update.set(filed.getName(), filedValue);
                            }
                        } else {
                            update.set(filed.getName(), filedValue);
                        }
                    }
                }
            }
            mongoTemplate.updateFirst(query, update, entity.getClass());
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        } catch (IllegalAccessException e) {
            flag = false;
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 删除记录
     *
     * @param entity 文档对象
     * @return ： 成功   false ： 失败
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public boolean delete(T entity) throws DataAccessException {
        boolean flag = true;
        try {
            Query query = new Query();
            Criteria criteria = new Criteria();
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field filed : fields) {
                if (filed.getAnnotations().length > 0) {
                    filed.setAccessible(true);
                    Object filedValue = null;                              //获取属性值
                    try {
                        filedValue = filed.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (filedValue != null && !filedValue.toString().equals("")) {
                        criteria.and(filed.getName()).is(filedValue);
                    }
                }
            }
            query.addCriteria(criteria);
            mongoTemplate.remove(query, entity.getClass());
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 根据主键查询一条记录
     *
     * @param entity 文档对象
     * @return 返回一个对象
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public T selectByPrimaryKey(T entity) throws DataAccessException {
        T result = null;
        try {
            Query query = new Query();
            Criteria criteria = new Criteria();
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field filed : fields) {
                filed.setAccessible(true);
                Object filedValue = filed.get(entity);                              //获取属性值
                if (filed.getAnnotation(Id.class) != null) {
                    criteria.and(filed.getName()).is(filedValue);
                }
            }
            query.addCriteria(criteria);
            result = (T) mongoTemplate.findOne(query, entity.getClass());
        } catch (DataAccessException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 查询一条记录
     *
     * @param entity ：文档对象
     * @return 返回一个对象
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public T queryForObject(T entity) throws DataAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotations().length > 0) {
                field.setAccessible(true);
                Object fieldValue = null;                              //获取属性值
                try {
                    fieldValue = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (!Util.checkDefaultValue(fieldValue)) {
                    criteria.and(field.getName()).is(fieldValue);
                }
            }
        }
        query.addCriteria(criteria);

        long startTime = new Date().getTime();
        T t = (T) mongoTemplate.findOne(query, entity.getClass());
        String method = "查询一条记录";
        insertOperateLog(entity,query,startTime,method);
        return t;
    }

    /**
     * 查询max记录
     *
     * @param entity ：文档对象
     * @return 返回一个对象
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public T QueryForMAX(T entity, String field) throws DataAccessException {

        Query query = new Query();
        //条件设置
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field fieldTemp : fields) {
            if (fieldTemp.getAnnotations().length > 0) {
                fieldTemp.setAccessible(true);
                Object fieldValue = null;                              //获取属性值
                try {
                    fieldValue = fieldTemp.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (!Util.checkDefaultValue(fieldValue)) {
                    criteria.and(fieldTemp.getName()).is(fieldValue);
                }
            }
        }
        query.addCriteria(criteria);

        //排序设置
        Sort sort = new Sort(Sort.Direction.DESC, field);
        query.with(sort);

        //分页设置
        query.limit(1);


        long startTime = new Date().getTime();
        T t = (T) mongoTemplate.findOne(query, entity.getClass());
        String method = "分页查询";
        insertOperateLog(entity.getClass(), query, startTime, method);
        return t;
    }

    /**
     * 查询全部记录
     *
     * @return 返回对象列表
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public List<T> queryAll() throws DataAccessException {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        return (List<T>) mongoTemplate.findAll(entityClass);
    }

    /**
     * 查询门店所有记录
     *
     * @param entity      ：文档对象
     * @param criteria:条件
     * @return 返回一个对象
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public List<T> QueryAll(T entity, Sort sort) throws DataAccessException {

        Query query = new Query();
        //条件设置
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field fieldTemp : fields) {
            if (fieldTemp.getAnnotations().length > 0) {
                fieldTemp.setAccessible(true);
                Object fieldValue = null;                              //获取属性值
                try {
                    fieldValue = fieldTemp.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (!Util.checkDefaultValue(fieldValue)) {
                    criteria.and(fieldTemp.getName()).is(fieldValue);
                }
            }
        }
        query.addCriteria(criteria);
        //排序设置
        if (sort != null) {
            query.with(sort);
        }
        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        String method = "分页查询";
        insertOperateLog(entity, query, startTime, method);
        return list;
    }

    private void insertOperateLog(T entity, Criteria criteria,
                                  long startTime, String method) {
        long currentTime = new Date().getTime();

        String entityObj = entity.getClass().toString();
        String conditions = criteria.toString();
        long time = currentTime - startTime;
        if (time > TIME) {
            this.insertMongoLog(method, entityObj, conditions, time);
        }
        log.info("【执行方法】：" + method);
        log.info("【实体对象】：" + entityObj);
        log.info("【查询条件】：" + conditions);
        log.info("【执行时长】：" + time + " 毫秒");
    }

    private void insertOperateLog(Class entityClass, Query query,
                                  long startTime, String method) {
        long currentTime = new Date().getTime();

        String entityObj = entityClass.toString();
        String conditions = query.toString();
        long time = currentTime - startTime;
        if (time > TIME) {
            this.insertMongoLog(method, entityObj, conditions, time);
        }
        log.info("【执行方法】：" + method);
        log.info("【实体对象】：" + entityObj);
        log.info("【查询条件】：" + conditions);
        log.info("【执行时长】：" + time + " 毫秒");
    }
    
    private void insertOperateLog(T entity, Query query, long startTime, String method) {
        insertOperateLog(entity.getClass(), query, startTime, method);
    }

    /**
     * 查询多条记录
     *
     * @param entity ：文档对象
     * @return 返回对象列表
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public List<T> queryForList(T entity) throws DataAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            if (filed.getAnnotations().length > 0) {
                filed.setAccessible(true);
                Object filedValue = null;                              //获取属性值
                try {
                    filedValue = filed.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("")) {
                    criteria.and(filed.getName()).is(filedValue);
                }
            }
        }
        query.addCriteria(criteria);

        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        String method = "查询多条记录";
        insertOperateLog(entity, query, startTime, method);
        return list;
    }

    /**
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public List<T> queryLikeForList(T entity) throws DataAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            if (filed.getAnnotations().length > 0) {
                filed.setAccessible(true);
                Object filedValue = null;                              //获取属性值
                try {
                    filedValue = filed.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("")) {
                    criteria.and(filed.getName()).regex(".*?" + filedValue + ".*");

                }
            }
        }
        query.addCriteria(criteria);

        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        String method = "查询多条记录";
        insertOperateLog(entity,query,startTime,method);
        return list;
    }

    /**
     * 分页查询
     *
     * @param pageBean 分页组件
     * @param entity   文档对象
     * @return 数据列表
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public PageBean<T> pageQuery(PageBean<T> pageBean, T entity) throws DataAccessException {

        Query query = new Query();
        //查询条件设置
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            if (filed.getAnnotations().length > 0) {
                filed.setAccessible(true);
                Object filedValue = null;
                try {
                    filedValue = filed.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("")) {
                    //criteria.and(filed.getName()).is(filedValue);
                    if ("productName".equals(filed.getName())) {
                        criteria.and(filed.getName()).regex(".*?" + filedValue + ".*");
                        log.error("----------" + filedValue);
                    } else {
                        criteria.and(filed.getName()).is(filedValue);
                    }
                }
            }
        }
        query.addCriteria(criteria);
        //排序条件设置
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        LinkedHashMap<String, String> sortItemMap = pageBean.getSortItemMap();
        if (sortItemMap != null) {
            for (Map.Entry<String, String> entry : sortItemMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, entry.getKey()));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, entry.getKey()));
                }
            }
            Sort sort = new Sort(orders);
            query.with(sort);
        }
        //分页设置
        if (pageBean.getPage() == 1) {
            query.limit(pageBean.getPageSize());
        } else {
            query.skip(pageBean.getPage() * pageBean.getPageSize() - pageBean.getPageSize());
            query.limit(pageBean.getPageSize());
        }
        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        String method = "分页查询";
        insertOperateLog(entity,query,startTime,method);
        PageBean<T> result = new PageBean<T>(pageBean.getPage(), (int) this.pageQueryCount(entity),
                pageBean.getPageSize());
        result.setSortItemMap(pageBean.getSortItemMap());
        result.setUnderly(list);
        return result;
    }

    /**
     * 分页查询
     *
     * @param pageBean 分页组件
     * @param entity   文档对象
     * @return 数据列表
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public PageBean<T> pageQuery(PageBean<T> pageBean, Criteria criteria, T entity) throws DataAccessException {

        
        Query query = new Query();
        //查询条件设置
        if (criteria == null) {
            criteria = new Criteria();
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotations().length > 0) {
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (!Util.checkDefaultValue(fieldValue)) {
                    criteria.and(field.getName()).is(fieldValue);
                }
            }
        }
        query.addCriteria(criteria);

        //排序条件设置
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        LinkedHashMap<String, String> sortItemMap = pageBean.getSortItemMap();
        if (sortItemMap != null) {
            for (Map.Entry<String, String> entry : sortItemMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, entry.getKey()));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, entry.getKey()));
                }
            }
            Sort sort = new Sort(orders);
            query.with(sort);
        }

        //分页设置
        if (pageBean.getPage() == 1) {
            query.limit(pageBean.getPageSize());
        } else {
            query.skip(pageBean.getPage() * pageBean.getPageSize() - pageBean.getPageSize());
            query.limit(pageBean.getPageSize());
        }

        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        String method = "分页查询";
        insertOperateLog(entity,query,startTime,method);
        PageBean<T> result = new PageBean<T>(pageBean.getPage(), (int) this.pageQueryCount(entity),
                pageBean.getPageSize());
        result.setSortItemMap(pageBean.getSortItemMap());
        result.setUnderly(list);
        return result;
    }

    /**
     * 排序字段必须唯一并有序
     */
    public PageBean<T> pageQueryNew(PageBean<T> pageBean, Criteria criteria, T entity) throws Exception {
        T cursor = pageBean.getCursor();

        List<Field> annFields = new ArrayList<Field>();
        Query query = getQuery(pageBean, criteria, entity, cursor, annFields, true);

        if (annFields == null) {
            return pageBean;
        }
        long startTime = new Date().getTime();
        List<T> list = (List<T>) getMongoTemplate().find(query, entity.getClass());
        String method = "分页查询";
        insertOperateLog(entity,query,startTime,method);
        pageBean.setUnderly(list);
        return pageBean;
    }

    /**
     * getQuery.
     */
    public Query getQuery(PageBean<T> pageBean, Criteria criteria, T entity, T cursor, List<Field> annFields,
                          boolean isCursor) throws IllegalAccessException {
        
        LinkedHashMap<String, String> sortItemMap = pageBean.getSortItemMap();
        //查询条件设置
        if (sortItemMap == null) {
            log.error("没有排序条件，无法查询");
            return null;
        }
        Set<String> annotationNames = sortItemMap.keySet();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        Field[] fields = entity.getClass().getDeclaredFields();
        if (criteria == null) {
            criteria = new Criteria();
            for (Field field : fields) {
                if (field.getAnnotations().length > 0) {
                    field.setAccessible(true);
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (!Util.checkDefaultValue(fieldValue)) {
                        criteria.and(field.getName()).is(fieldValue);
                    }
                }
            }
        }
        for (Field field : fields) {
            org.springframework
                    .data.mongodb.core.mapping.Field ann = field.getAnnotation(org.springframework
                    .data.mongodb.core.mapping.Field.class);
            if (ann == null) {
                continue;
            }
            String annotation = field.getAnnotation(org.springframework
                    .data.mongodb.core.mapping.Field.class).value();
            if (annotationNames.contains(annotation)) {
                field.setAccessible(true);

                if (sortItemMap.get(annotation) != null && sortItemMap.get(annotation).equals("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, annotation));
                    if (cursor != null && isCursor) {
                        Object value = field.get(cursor);
                        criteria.and(annotation).gt(value);
                        //criteria.and(annotation).gte(value);
                    }
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, annotation));
                    if (cursor != null && isCursor) {
                        Object value = field.get(cursor);
                        criteria.and(annotation).lt(value);
                        // criteria.and(annotation).lte(value);
                    }
                }
                annFields.add(field);

            }

        }
        Sort sort = new Sort(orders);
        Query query = new Query();
        query.with(sort);
        query.addCriteria(criteria);

        //分页设置
        //query.limit(pageBean.getPageSize() + 1);
        query.limit(pageBean.getPageSize());

        log.info("【查询条件】：" + query.toString());
        return query;
    }

    /**
     * contains.
     */
    public boolean contains(List<T> list, T cursor, List<Field> annFields) throws Exception {
        if (cursor == null) {
            return false;
        }
        boolean flag = false;
        for (Field f : annFields) {
            Object v1 = f.get(cursor);
            //list.get(0)是cursor自己
            Object v2 = f.get(list.get(1));
            if (v2.equals(v1)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 根据条件查询记录数
     *
     * @param entity 文档对象
     * @return 记录数
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public long pageQueryCount(T entity) throws DataAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotations().length > 0) {
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (!Util.checkDefaultValue(fieldValue)) {
                    criteria.and(field.getName()).is(fieldValue);
                }
            }
        }
        query.addCriteria(criteria);

        long startTime = new Date().getTime();
        long count = mongoTemplate.count(query, entity.getClass());
        String method = "根据条件查询记录数";
        insertOperateLog(entity,query,startTime,method);
        return count;
    }

    /**
     * 分页查询  如果字段有商品名称，对商品名称进行模糊查询
     *
     * @param pageBean 分页组件
     * @param entity   文档对象
     * @return 数据列表
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public PageBean<T> pageQueryVague(PageBean<T> pageBean, T entity) throws DataAccessException {
     
        Query query = new Query();
        //查询条件设置
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            if (filed.getAnnotations().length > 0) {
                filed.setAccessible(true);
                Object filedValue = null;
                try {
                    filedValue = filed.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("")) {
                    if (filed.getName().equals("productName")) {
                        criteria.and(filed.getName()).regex(".*?" + filedValue + ".*");
                    } else {
                        criteria.and(filed.getName()).is(filedValue);
                    }
                }
            }
        }
        query.addCriteria(criteria);

        //排序条件设置
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        LinkedHashMap<String, String> sortItemMap = pageBean.getSortItemMap();
        if (sortItemMap != null) {
            for (Map.Entry<String, String> entry : sortItemMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, entry.getKey()));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, entry.getKey()));
                }
            }
            Sort sort = new Sort(orders);
            query.with(sort);
        }

        //分页设置
        if (pageBean.getPage() == 1) {
            query.limit(pageBean.getPageSize());
        } else {
            query.skip(pageBean.getPage() * pageBean.getPageSize() - pageBean.getPageSize());
            query.limit(pageBean.getPageSize());
        }
        log.error("[标准库分页查询query]：" + query.toString());
        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        log.error("[标准库分页查询dao中list size]：" + list.size());
        String method = "分页查询";
        insertOperateLog(entity,query,startTime,method);
        PageBean<T> result = new PageBean<T>(pageBean.getPage(), (int) this.pageQueryCountVague(entity),
                pageBean.getPageSize());
        result.setSortItemMap(pageBean.getSortItemMap());
        result.setUnderly(list);
        return result;
    }

    /**
     * 根据条件查询记录数
     *
     * @param entity 文档对象
     * @return 记录数
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public long pageQueryCountVague(T entity) throws DataAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            if (filed.getAnnotations().length > 0) {
                filed.setAccessible(true);
                Object filedValue = null;
                try {
                    filedValue = filed.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("")) {
                    if (filed.getName().equals("productName")) {
                        criteria.and(filed.getName()).regex(".*?" + filedValue + ".*");
                    } else {
                        criteria.and(filed.getName()).is(filedValue);
                    }
                }
            }
        }
        query.addCriteria(criteria);

        long startTime = new Date().getTime();
        long count = mongoTemplate.count(query, entity.getClass());
        String method = "根据条件查询记录数";
        insertOperateLog(entity,query,startTime,method);
        return count;
    }

    /**
     * 分组查询
     *
     * @param entity 实体ID
     * @param keys   主键
     * @return 查询结果列表
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public List<T> group(T entity, String... keys) throws DataAccessException {
        List<T> result = new ArrayList<T>();

        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotations().length > 0) {
                field.setAccessible(true);
                Object filedValue = null;                              //获取属性值
                try {
                    filedValue = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("") 
                        && field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class) != null) {
                    criteria.and(field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class)
                            .value()).is(filedValue);
                }
            }
        }

        GroupBy groupBy = GroupBy.key(keys).initialDocument("{count:0}")
                .reduceFunction("function(doc, prev){prev.count += 1}");
        Document document = entity.getClass().getAnnotation(Document.class);

        long startTime = new Date().getTime();
        GroupByResults<T> results = (GroupByResults<T>) mongoTemplate.group(criteria, 
                document.collection(), groupBy, entity.getClass());
        String method = "分组查询";
        insertOperateLog(entity, criteria, startTime, method);
        for (T t : results) {
            result.add(t);
        }
        return result;
    }

   
    /**
     * 字段排序查询列表
     *
     * @param sortItemMap 排序
     * @param entity      实体
     * @return 集合
     * @throws org.springframework.dao.DataAccessException 数据通道异常
     */
    public List<T> querySortForList(LinkedHashMap<String, String> sortItemMap, T entity) throws DataAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field filed : fields) {
            if (filed.getAnnotations().length > 0) {
                filed.setAccessible(true);
                Object filedValue = null;                              //获取属性值
                try {
                    filedValue = filed.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (filedValue != null && !filedValue.toString().equals("")) {
                    criteria.and(filed.getName()).is(filedValue);
                }
            }
        }
        query.addCriteria(criteria);
        //排序条件设置
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        if (sortItemMap != null) {
            for (Map.Entry<String, String> entry : sortItemMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, entry.getKey()));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, entry.getKey()));
                }
            }
            Sort sort = new Sort(orders);
            query.with(sort);
        }

        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entity.getClass());
        String method = "字段排序查询列表";
        insertOperateLog(entity,query,startTime,method);
        return list;
    }

    /**
     * 记录Mongo执行日志
     *
     * @param method     执行方法
     * @param entity     实体对象
     * @param conditions 查询条件
     * @param time       执行时长
     */
    public void insertMongoLog(String method, String entity, String conditions, long time) {
        MongoLogBean mongoLogBean = new MongoLogBean();
        mongoLogBean.setMethod(method);
        mongoLogBean.setEntity(entity);
        mongoLogBean.setConditions(conditions);
        mongoLogBean.setTime(time);
        mongoLogBean.setCreateDate(new Date());
        mongoTemplate.insert(mongoLogBean);
    }

    /**
     * getSequence.
     */
    public int getSequence(String tableName) {
        DBCollection table = mongoTemplate.getCollection("lagd_sequence");
        DBObject query = new BasicDBObject();
        query.put("_id", tableName);
        DBObject newDocument = new BasicDBObject();
        newDocument.put("$inc", new BasicDBObject().append("currentIdValue", 1));
        DBObject ret = table.findAndModify(query, newDocument);

        if (ret == null) {
            synchronized (EntityMongoBaseDao.class) {
                ret = table.findAndModify(query, newDocument);
                if (ret == null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("_id", tableName);
                    map.put("currentIdValue", INITVALUE);
                    this.getMongoTemplate().insert(map, "lagd_sequence");
                    return INITVALUE;
                } else {
                    return (Integer) ret.get("currentIdValue") + 1;
                }
            }

        } else {
            return (Integer) ret.get("currentIdValue") + 1;
        }
    }

    /**
     * @param map         查询条件
     * @param pageBean    page
     * @param criteria    查询条件
     * @param entityClass 查询类类对象
     * @author xuliang
     * 通过map批量查询
     * attention："mongoId"为id
     */
    public PageBean<T> pageQueryByMap(Map<String, Object> map, PageBean<T> pageBean, Criteria criteria,
                                      Class entityClass) {
      
        Query query = new Query();
        //查询条件设置
        if (criteria == null) {
            criteria = new Criteria();
        }
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("mongoId")) {
                    criteria.and("_id").is(entry.getValue());
                } else {
                    criteria.and(entry.getKey()).is(entry.getValue());
                }
            }
            query.addCriteria(criteria);
        }
        //排序条件设置
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        LinkedHashMap<String, String> sortItemMap = pageBean.getSortItemMap();
        if (sortItemMap != null) {
            for (Map.Entry<String, String> entry : sortItemMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, entry.getKey()));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, entry.getKey()));
                }
            }
            Sort sort = new Sort(orders);
            query.with(sort);
        }
        //分页设置
        if (pageBean.getPage() == 1) {
            query.limit(pageBean.getPageSize());
        } else {
            query.skip(pageBean.getPage() * pageBean.getPageSize() - pageBean.getPageSize());
            query.limit(pageBean.getPageSize());
        }
        long startTime = new Date().getTime();
        List<T> list = (List<T>) mongoTemplate.find(query, entityClass);
        String method = "根据map分页查询";
        insertOperateLog(entityClass, query, startTime, method);
        PageBean<T> result = new PageBean<T>(pageBean.getPage(), pageBean.getPageSize());
        result.setSortItemMap(pageBean.getSortItemMap());
        result.setUnderly(list);
        return result;
    }



    /**
     * @param map         查询条件
     * @param entityClass 查询类类对象
     * @author xuliang
     * 通过map查询
     * attention："mongoId"为id
     */
    public T queryByMap(Map<String, Object> map, Class entityClass) {
        Query query = new Query();
        //查询条件设置
        Criteria criteria = new Criteria();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("mongoId")) {
                    criteria.and("_id").is(entry.getValue());
                } else {
                    criteria.and(entry.getKey()).is(entry.getValue());
                }
            }
            query.addCriteria(criteria);
        }

        long startTime = new Date().getTime();
        T t = (T) mongoTemplate.findOne(query, entityClass);
        String method = "根据map查询";
        insertOperateLog(entityClass,query,startTime,method);
        return t;
    }
}
