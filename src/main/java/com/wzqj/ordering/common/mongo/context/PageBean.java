package com.wzqj.ordering.common.mongo.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * [添加说明]
 * <br>User: wh
 * <br>DateTime: Sep 8, 2015 9:16:16 PM
 * <br>Version 1.0
 */
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = -3284694395882955584L;

    public static final int DEFAULT_PAGESIZE = 30;

    /**
     * 当前页
     */
    private int page;

    /**
     * 每页多少项
     */
    private final int pageSize;

    /**
     * 总项数
     */
    private final int itemCount;

    /**
     * 总页数
     */
    private final int pageCount;

    /**
     * 排序参数
     */
    private LinkedHashMap<String, String> sortItemMap;

    /**
     * 数据列表
     */
    private List<T> underly;
    /**
     * 游标
     */
    private T cursor;

    public LinkedHashMap<String, String> getSortItemMap() {
        return sortItemMap;
    }

    public void setSortItemMap(LinkedHashMap<String, String> sortItemMap) {
        this.sortItemMap = sortItemMap;
    }

    public PageBean() {
        this(1, 0, DEFAULT_PAGESIZE);
    }

    public PageBean(int page) {
        this(page, 0, DEFAULT_PAGESIZE);
    }

    public PageBean(int page, int pageSize) {
        this(page, 0, pageSize);
    }

    /**
     * PageBean.
     */
    public PageBean(int page, int itemCount, int pageSize) {
        if (page <= 0) {
            page = 1;
        }
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGESIZE;
        }
        if (itemCount < 0) {
            itemCount = 0;
        }
        this.page = page;
        this.pageSize = pageSize;
        this.itemCount = itemCount;
        this.pageCount = (this.itemCount % this.pageSize > 0) ? (this.itemCount / this.pageSize + 1)
                : this.itemCount / this.pageSize;
        //this.pageCount = (int) Math.ceil(((double) itemCount / (double) pageSize));
        this.underly = new ArrayList<T>(pageSize);
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    public T getCursor() {
        return cursor;
    }

    public void setCursor(T cursor) {
        this.cursor = cursor;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getStartIndex() {
        return (page - 1) * pageSize + 1;
    }

    /**
     * getEndIndex.
     */
    public int getEndIndex() {
        int end = page * pageSize;
        if (end > itemCount) {
            end = itemCount;
        }
        return end;
    }

    public List<T> getUnderly() {
        return underly;
    }

    public boolean addAll(Collection<? extends T> c) {
        return underly.addAll(c);
    }

    public void setUnderly(List<T> underly) {
        this.underly = underly;
    }

    public void setPage(int page) {
        this.page = page;
    }


}
