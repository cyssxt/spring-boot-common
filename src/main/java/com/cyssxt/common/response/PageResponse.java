package com.cyssxt.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by zqy on 23/05/2018.
 */
public class PageResponse<T> {
    private int pageNo;
    private List<T> t;
    private int pageSize;
    private int totalPage;
    private long totalCount;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getT() {
        return t;
    }

    public void setT(List<T> t) {
        this.t = t;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public PageResponse(Page<T> page){
        this.setPageNo(page.getPageable().getPageNumber()+1);
        this.setPageSize(page.getPageable().getPageSize());
        this.setTotalPage(page.getTotalPages());
        this.setTotalCount(page.getTotalElements());
        this.setT(page.getContent());
    }

    public PageResponse( List<T> t,int pageNo, int pageSize,long totalCount) {
        this.pageNo = pageNo;
        this.t = t;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = (int)Math.ceil(Double.valueOf(totalCount)/pageSize);
    }
}
