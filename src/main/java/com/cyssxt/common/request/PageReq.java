package com.cyssxt.common.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by zqy on 2018/6/18.
 */
public class PageReq extends BaseReq {
    private Integer pageNo = 1;
    private Integer pageSize = 10;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset(){
        return (this.getPageNo()-1)*pageNo;
    }

    public Pageable toPageable(){
        return PageRequest.of(pageNo-1,pageSize);
    }

    public Pageable toPageable(Sort sort){
        return PageRequest.of(pageNo-1,pageSize,sort);
    }

}
