package com.wzqj.ordering.common.mongo.dao;

import com.wzqj.ordering.common.mongo.context.PageBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * MongoDB基础DAO接口（Map）
 * User: wh
 * Date: 15-9-8
 * Time: 下午5:58
 * To change this meal use File | Settings | File Templates.
 */
@Repository
public class MapMongoBaseDao<T> {

    @Resource
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 新增实体数据
     *
     * @param map            数据对象
     * @param collectionName 集合名称
     * @return true ： 成功   false ： 失败
     */
    public boolean insert(Map<String, Object> map, String collectionName) {
        boolean flag = true;
        try {
            this.getMongoTemplate().insert(map, collectionName);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 新增实体数据
     *
     * @param list           数据对象
     * @param collectionName 集合名称
     * @return true ： 成功   false ： 失败
     */
    public boolean insert(List<Map<String, Object>> list, String collectionName) {
        boolean flag = true;
        try {
            this.getMongoTemplate().insert(list, collectionName);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 修改实体数据
     *
     * @param map            数据对象
     * @param collectionName 集合名称
     * @return true ： 成功   false ： 失败
     */
    public boolean update(Map<String, Object> map, String collectionName) {
        return this.update(map, collectionName, true);
    }

    /**
     * 修改实体数据
     *
     * @param map            文档对象
     * @param collectionName 集合名称
     * @param filterFlag     过滤空值标识
     * @return true ： 成功   false ： 失败
     */
    public boolean update(Map<String, Object> map, String collectionName, 
                          Boolean filterFlag) throws DataAccessException {
        boolean flag = true;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(map.get("mongoId")));
            Update update = new Update();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!entry.getKey().equals("mongoId")) {
                    if (filterFlag) {
                        if (entry.getValue() != null) {
                            update.set(entry.getKey(), entry.getValue());
                        }
                    } else {
                        update.set(entry.getKey(), entry.getValue());
                    }
                }
            }

            this.getMongoTemplate().updateFirst(query, update, collectionName);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 根据主键删除实体数据
     *
     * @param id             主键ID
     * @param collectionName 集合名称
     * @return 成功   false ： 失败
     */
    public boolean deleteByPrimaryKey(String id, String collectionName) {
        boolean flag = true;
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            this.getMongoTemplate().remove(query, collectionName);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 根据条件删除实体数据
     *
     * @param map            数据对象
     * @param collectionName 集合名称
     * @return 成功   false ： 失败
     */
    public boolean delete(Map<String, Object> map, String collectionName) {
        boolean flag = true;
        try {
            Query query = new Query();
            Criteria criteria = new Criteria();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("mongoId")) {
                    criteria.and("_id").is(entry.getValue());
                } else {
                    criteria.and(entry.getKey()).is(entry.getValue());
                }
            }
            query.addCriteria(criteria);
            this.getMongoTemplate().remove(query, collectionName);
        } catch (DataAccessException e) {
            flag = false;
            throw e;
        }
        return flag;
    }

    /**
     * 根据主键查询一条记录
     *
     * @param id             ：主键ID
     * @param collectionName ：文档名称
     * @return 返回一个对象
     */
    public HashMap selectByPrimaryKey(String id, String collectionName) throws DataAccessException {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return this.getMongoTemplate().findOne(query, HashMap.class, collectionName);
    }

    /**
     * 查询实体数据
     *
     * @param map            ：数据对象
     * @param collectionName ：文档名称
     * @return 返回一个对象
     */
    public List<HashMap> query(Map<String, Object> map, String collectionName) {
        Query query = new Query();
        if (map != null && map.size() > 0) {
            Criteria criteria = new Criteria();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("mongoId")) {
                    criteria.and("_id").is(entry.getValue());
                } else {
                    criteria.and(entry.getKey()).is(entry.getValue());
                }
            }
            query.addCriteria(criteria);
        }

        query.limit(100);
        return this.getMongoTemplate().find(query, HashMap.class, collectionName);
    }

    /**
     * 查询实体数据
     *
     * @param map            ：数据对象
     * @param collectionName ：文档名称
     * @return 返回一个对象
     */
    public List<HashMap> queryForOne(Map<String, Object> map, String collectionName) {
        Query query = new Query();
        if (map != null && map.size() > 0) {
            Criteria criteria = new Criteria();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("mongoId")) {
                    criteria.and("_id").is(entry.getValue());
                } else {
                    criteria.and(entry.getKey()).is(entry.getValue());
                }
            }
            query.addCriteria(criteria);
        }

        query.limit(100);
        return this.getMongoTemplate().find(query, HashMap.class, collectionName);
    }

    /**
     * 查询全部记录
     *
     * @param collectionName 集合名称
     * @return 返回对象列表
     */
    public List<HashMap> queryAll(String collectionName) throws DataAccessException {
        return this.getMongoTemplate().find(new Query(), HashMap.class, collectionName);
    }

    /**
     * 分页查询
     *
     * @param pageBean       分页组件
     * @param map            ：文档对象
     * @param collectionName 集合名称
     * @return 数据列表
     */
    public PageBean<HashMap> pageQuery(PageBean<Map> pageBean, Map<String, Object> map,
            String collectionName) throws DataAccessException {

        Query query = new Query();
        //查询条件设置
        if (map != null && map.size() > 0) {
            Criteria criteria = new Criteria();
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
        List<HashMap> list = this.getMongoTemplate().find(query, HashMap.class, collectionName);
        PageBean<HashMap> result = new PageBean<HashMap>(pageBean.getPage(),
                (int) this.pageQueryCount(map, collectionName), pageBean.getPageSize());
        result.setSortItemMap(pageBean.getSortItemMap());
        result.setUnderly(list);
        return result;
    }

    /**
     * 分页查询-获取总记录数
     *
     * @param map            ：文档对象
     * @param collectionName 集合名称
     * @return 总记录数
     */
    private long pageQueryCount(Map<String, Object> map, String collectionName) throws DataAccessException {
        Query query = new Query();
        if (map != null && map.size() > 0) {
            Criteria criteria = new Criteria();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                criteria.and(entry.getKey()).is(entry.getValue());
            }
            query.addCriteria(criteria);
        }
        return this.getMongoTemplate().count(query, collectionName);
    }
}
