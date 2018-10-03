package com.cyssxt.common.utils;

import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.PageResponse;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

/**
 * Created by zqy on 2018/6/30.
 */
public class QuerydslUtils {

    public  static <T> PageResponse<T> applyOraclePage(PageReq pageReq, JPAQuery jpaQuery){
        long count = jpaQuery.fetchCount();
        int pageNo = pageReq.getPageNo();
        int pageSize = pageReq.getPageSize();
        jpaQuery.offset((pageNo-1)*pageSize).limit(pageSize*pageNo);
        List<T> list = jpaQuery.fetch();
        PageResponse pageResponse = new PageResponse(list,pageNo,pageSize,count);
        return pageResponse;
    }

    public  static <T> PageResponse<T> applyMysqlPage(PageReq pageReq, JPAQuery jpaQuery){
        long count = jpaQuery.fetchCount();
        int pageNo = pageReq.getPageNo();
        int pageSize = pageReq.getPageSize();
        jpaQuery.offset((pageNo-1)*pageSize).limit(pageSize);
        List<T> list = jpaQuery.fetch();
        PageResponse pageResponse = new PageResponse(list,pageNo,pageSize,count);
        return pageResponse;
    }


    public static String like(String value){
        return String.format("%%%s%%",value);
    }
}
