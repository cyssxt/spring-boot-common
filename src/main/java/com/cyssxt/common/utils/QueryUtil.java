package com.cyssxt.common.utils;

import com.cyssxt.common.exception.ValidException;
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

public class QueryUtil {
    public static interface ReqParameter<T extends BaseReq>{
        void initParam(Query query, T t) throws ValidException;
    }

    public static interface PageParameter<T extends PageReq> extends ReqParameter<T>{
        void initParam(Query query,T t) throws ValidException;
    }

    public  static <T> PageResponse<T> applyNativePage(String sql, EntityManager entityManager, PageReq pageReq, PageParameter parameter, ResultTransformer transformer) throws ValidException {
        Query query = entityManager.createNativeQuery(sql);
        parameter.initParam(query,pageReq);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(transformer);
        Query totalQuery = entityManager.createNativeQuery(String.format("select count(1) countBean from (%s) A",sql));
        parameter.initParam(totalQuery,pageReq);
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

    public  static <T> PageResponse<T> applyNativePage(String sql, EntityManager entityManager, PageReq pageReq, PageParameter parameter, Class alias) throws ValidException {
        return applyNativePage(sql,entityManager,pageReq,parameter, Transformers.aliasToBean(alias));
    }


    /**
     * 忽略大小写和_转换器
     * @param sql
     * @param entityManager
     * @param pageReq
     * @param parameter
     * @param alias
     * @param <T>
     * @return
     */
    public  static <T> PageResponse<T> applyNativePageWithIct(String sql, EntityManager entityManager, PageReq pageReq, PageParameter parameter, Class alias) throws ValidException {
        return applyNativePage(sql,entityManager,pageReq,parameter,new IgnoreCaseResultTransformer(alias));
    }

    public  static <T> List<T> applyNativeListWithIct(String sql, PageReq req,EntityManager entityManager, PageParameter parameter, Class alias) throws ValidException {
        return applyNativeList(sql,req,entityManager,parameter,new IgnoreCaseResultTransformer(alias));
    }

    public  static <T> List<T> applyNativeList(String sql, PageReq req,EntityManager entityManager, PageParameter parameter, Class alias) throws ValidException {
        return applyNativeList(sql,req,entityManager,parameter,Transformers.aliasToBean(alias));
    }

    public  static <T> List<T> applyNativeList(String sql, BaseReq req, EntityManager entityManager, ReqParameter parameter, ResultTransformer transformer) throws ValidException {
        Query query = entityManager.createNativeQuery(sql);
        parameter.initParam(query,req);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(transformer);
        List<T> list = query.getResultList();
        return list;
    }



    public static String like(String value){
        return String.format("%%%s%%",value);
    }

}
