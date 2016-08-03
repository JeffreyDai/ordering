package com.wzqj.ordering.common.page;

import java.util.List;

/**
 * 普通分页模型,比pager多了两个属性:pageNo, pageSize.
 * 其它的属性可以根据需要扩展:总页数,上一页,下一页,首页,尾页.这几个字段根据pageNo, pageSize, total三个元数据计算得出.
 */
public class PageModel<T> extends Pager<T> {

    private int pageNo;
    
    private int pageSize;

    /**
     * 构造方法.
     */
    public PageModel() {
    }

    /**
     * 构造方法.
     */
    public PageModel(List<T> list, int pageNo, int pageSize, int total) {
        super();
        this.list = list;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }

    /**
     * 构造方法.
     */
    public PageModel(Pager<T> pager, int pageNo, int pageSize) {
        super();
        this.list = pager.getList();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = pager.getTotal();
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
