package com.wzqj.ordering.common.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单分页模型
 */
public class Pager<T> {

    protected List<T> list = new ArrayList<T>();
    protected long total;

    public Pager() {
    }

    public Pager(List<T> list, int total) {
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> elements) {
        this.list = elements;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
