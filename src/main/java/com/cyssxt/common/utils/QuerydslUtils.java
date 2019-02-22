package com.cyssxt.common.utils;

import com.cyssxt.common.hibernate.util.IgnoreCaseResultTransformer;
import com.cyssxt.common.request.BaseReq;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.PageResponse;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

/**
 * Created by zqy on 2018/6/30.
 */
public class QuerydslUtils {

    public static interface Parameter{
        void initParam(Query query);
    }

    public static interface ReqParameter<T extends BaseReq>{
        void initParam(Query query,T t);
    }

    public static interface PageParameter<T extends PageReq>{
        void initParam(Query query,T t);
    }
    @Deprecated
    public  static <T> PageResponse<T> applyPage(PageReq pageReq, JPAQuery jpaQuery){
        long count = jpaQuery.fetchCount();
        int pageNo = pageReq.getPageNo();
        int pageSize = pageReq.getPageSize();
        jpaQuery.offset((pageNo-1)*pageSize).limit(pageSize*pageNo);
        List<T> list = jpaQuery.fetch();
        PageResponse pageResponse = new PageResponse(list,pageNo,pageSize,count);
        return pageResponse;
    }
    @Deprecated
    public  static <T> PageResponse<T> applyNativePage(String sql, EntityManager entityManager, PageReq pageReq, Parameter parameter, ResultTransformer transformer){
        Query query = entityManager.createNativeQuery(sql);
        parameter.initParam(query);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(transformer);
        Query totalQuery = entityManager.createNativeQuery(String.format("select count(1) countBean from (%s) A",sql));
        parameter.initParam(totalQuery);
        Number totalNumber = (Number)totalQuery.getSingleResult();
        long totalCount = totalNumber.longValue();
        int pageNo = pageReq.getPageNo();
        int pageSize = pageReq.getPageSize();
        query.setFirstResult((pageNo-1)*pageSize);
        query.setMaxResults(pageSize);
        List<T> list = query.getResultList();
        PageResponse pageResponse = new PageResponse(list,pageNo,pageSize,totalCount);
        return pageResponse;
    }

    @Deprecated
    public  static <T> PageResponse<T> applyNativePage(String sql, EntityManager entityManager, PageReq pageReq,Parameter parameter,Class alias){
       return applyNativePage(sql,entityManager,pageReq,parameter,Transformers.aliasToBean(alias));
    }


    @Deprecated
    public  static <T> PageResponse<T> applyNativePageWithIgnoreCaseTransformer(String sql, EntityManager entityManager, PageReq pageReq,Parameter parameter,Class alias){
        return applyNativePage(sql,entityManager,pageReq,parameter,new IgnoreCaseResultTransformer(alias));
    }

    @Deprecated
    public  static <T> List<T> applyNativePage(String sql, EntityManager entityManager,Parameter parameter,Class alias){
        Query query = entityManager.createNativeQuery(sql);
        parameter.initParam(query);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(alias));
        List<T> list = query.getResultList();
        return list;
    }

    @Deprecated
    public  static <T> List<T> applyNativeList(String sql,BaseReq req, EntityManager entityManager, ReqParameter parameter, Class alias){
        Query query = entityManager.createNativeQuery(sql);
        parameter.initParam(query,req);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(new IgnoreCaseResultTransformer(alias));
        List<T> list = query.getResultList();
        return list;
    }



    public static String like(String value){
        return String.format("%%%s%%",value);
    }
}
