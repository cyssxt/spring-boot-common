package com.cyssxt.common.utils;

import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.hibernate.transformer.IgnoreCaseResultTransformer;
import com.cyssxt.common.hibernate.transformer.ObjectTransformer;
import com.cyssxt.common.hibernate.transformer.StringTransformer;
import com.cyssxt.common.request.BaseReq;
import com.cyssxt.common.request.PageReq;
import com.cyssxt.common.response.PageResponse;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.spi.NativeQueryImplementor;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class QueryUtil {
    public interface ReqParameter<T extends BaseReq>{
        void initParam(Query query, T t) throws ValidException;
    }

    public interface Parameter{
        void init(Query query);
    }

    public interface PageParameter<T extends PageReq> extends ReqParameter<T>{
        @Override
        void initParam(Query query,T t) throws ValidException;
    }

    public static void execute(String sql, EntityManager entityManager, Parameter parameter) {
        Query query = entityManager.createNativeQuery(sql);
        if(parameter!=null) {
            parameter.init(query);
        }
        query.executeUpdate();
    }
    public static void execute(String sql, EntityManager entityManager){
        execute(sql,entityManager,null);
    }

    public  static <T> PageResponse<T> applyNativePage(String sql, EntityManager entityManager, PageReq pageReq, PageParameter parameter, ResultTransformer transformer) throws ValidException {
        Query query = entityManager.createNativeQuery(sql);
        if(null!=parameter){
            parameter.initParam(query,pageReq);
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(transformer);
        Query totalQuery = entityManager.createNativeQuery(String.format("select count(1) countBean from (%s) A",sql));
        if(null!=parameter) {
            parameter.initParam(totalQuery, pageReq);
        }
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

    public  static Long applyTotal(String sql, EntityManager entityManager,BaseReq req,ReqParameter reqParameter) throws ValidException {
        Query totalQuery = entityManager.createNativeQuery(String.format("select count(1) countBean from (%s) A",sql));
        if(reqParameter!=null){
            reqParameter.initParam(totalQuery,req);
        }
        Number totalNumber = (Number)totalQuery.getSingleResult();
        long totalCount = totalNumber.longValue();
        return totalCount;

    }
    public  static Long applyTotal(String sql, EntityManager entityManager) throws ValidException {
        return applyTotal(sql,entityManager,null,null);
    }

    public  static <T> PageResponse<T> applyNativePage(String sql, EntityManager entityManager, PageReq pageReq, PageParameter parameter, Class alias) throws ValidException {
        return applyNativePage(sql,entityManager,pageReq,parameter, Transformers.aliasToBean(alias));
    }

    public  static <T> PageResponse<T> applyNativePageByFile(String fileName,EntityManager entityManager,PageParameter reqParameter,Class alias) throws ValidException {
        String sql = FileUtil.getContent(fileName);
        return applyNativePage(sql,entityManager,null,reqParameter,alias);
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

    public  static <T> PageResponse<T> applyNativePageWithIctByFile(String fileName, EntityManager entityManager, PageReq pageReq, PageParameter parameter, ResultTransformer resultTransformer) throws ValidException {
        String sql = FileUtil.getContent(fileName);
        return applyNativePage(sql,entityManager,pageReq,parameter,resultTransformer);
    }

    public  static <T> List<T> applyNativeListWithIct(String sql, PageReq req,EntityManager entityManager, PageParameter parameter, Class alias) throws ValidException {
        return applyNativeList(sql,req,entityManager,parameter,new IgnoreCaseResultTransformer(alias));
    }
    public  static <T> List<T> applyNativeListWithIct(String sql,EntityManager entityManager, Class alias) throws ValidException {
        return applyNativeList(sql,null,entityManager,null,new IgnoreCaseResultTransformer(alias));
    }

    public  static <T> List<T> applyNativeListWithIct(String sql, EntityManager entityManager, ReqParameter parameter, Class alias) throws ValidException {
        return applyNativeList(sql,null,entityManager,parameter,new IgnoreCaseResultTransformer(alias));
    }

    public  static <T> List<T> applyNativeListWithIctByFile(String fileName, EntityManager entityManager, ReqParameter parameter, Class alias) throws ValidException {
        String sql = FileUtil.getContent(fileName);
        return applyNativeList(sql,null,entityManager,parameter,new IgnoreCaseResultTransformer(alias));
    }
    public  static <T> List<T> applyNativeListWithIctByFile(String fileName, EntityManager entityManager, Class alias) throws ValidException {
        String sql = FileUtil.getContent(fileName);
        return applyNativeList(sql,null,entityManager,null,new IgnoreCaseResultTransformer(alias));
    }

    public  static <T> List<T> applyNativeList(String sql, PageReq req,EntityManager entityManager, PageParameter parameter, Class alias) throws ValidException {
        return applyNativeList(sql,req,entityManager,parameter,Transformers.aliasToBean(alias));
    }
    public  static <T> List<T> applyNativeList(String sql, EntityManager entityManager,  Class alias) throws ValidException {
        return applyNativeList(sql,null,entityManager,null,Transformers.aliasToBean(alias));
    }
    public  static <T> List<T> applyNativeList(String sql,EntityManager entityManager, ReqParameter parameter, Class alias) throws ValidException {
        return applyNativeList(sql,null,entityManager,parameter,Transformers.aliasToBean(alias));
    }
    public  static <T> List<T> applyNativeList(String sql, EntityManager entityManager, ReqParameter parameter, ResultTransformer transformer) throws ValidException {
        return applyNativeList(sql,null,entityManager,parameter,transformer);
    }
    public  static <T> List<T> applyNativeList(String sql, BaseReq req, EntityManager entityManager, ReqParameter parameter, ResultTransformer transformer) throws ValidException {
        Query query = entityManager.createNativeQuery(sql);
        if(parameter!=null) {
            parameter.initParam(query, req);
        }
        NativeQueryImplementor nativeQueryImplementor = query.unwrap(NativeQueryImpl.class);
        nativeQueryImplementor.setResultTransformer(transformer);
        List<T> list = query.getResultList();
        return list;
    }

    public  static <T> T applyFirst(String sql,EntityManager entityManager, ReqParameter parameter, Class alias) throws ValidException {
        List<T> t =  applyNativeList(sql,entityManager,parameter,new IgnoreCaseResultTransformer(alias));
        return CollectionUtils.isEmpty(t)?null:t.get(0);
    }

    public  static <T> T applyFirstByFile(String fileName,EntityManager entityManager, ReqParameter parameter, Class alias) throws ValidException {
        String sql = FileUtil.getContent(fileName);
        List<T> t =  applyNativeList(sql,entityManager,parameter,new IgnoreCaseResultTransformer(alias));
        return CollectionUtils.isEmpty(t)?null:t.get(0);
    }

    public  static <T> T applyFirst(String sql,EntityManager entityManager, ReqParameter parameter, ResultTransformer resultTransformer) throws ValidException {
        List<T> t =  applyNativeList(sql,entityManager,parameter,resultTransformer);
        return CollectionUtils.isEmpty(t)?null:t.get(0);
    }

    public  static <T> T applySignleStringResult(String sql,EntityManager entityManager) throws ValidException {
        List<T> t =  applyNativeList(sql,entityManager,null,new StringTransformer());
        return CollectionUtils.isEmpty(t)?null:t.get(0);
    }

    public  static <T> T applySingleObjectResult(String sql, EntityManager entityManager) throws ValidException {
        List<T> t =  applyNativeList(sql,entityManager,null,new ObjectTransformer());
        return CollectionUtils.isEmpty(t)?null:t.get(0);
    }

    public  static List<Map<String, Object>> applyNativeListMap(String sql, EntityManager entityManager,Parameter parameter){
        Query query = entityManager.createNativeQuery(sql);
        if(parameter!=null){
            parameter.init(query);
        }
        // 将结果转化为 Map<tableKey, keyValue>
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        return list;
    }
    public  static List<Map<String, Object>> applyNativeListMap(String sql, EntityManager entityManager){
        return applyNativeListMap(sql,entityManager,null);
    }

    public static String like(String value){
        return String.format("%%%s%%",value);
    }

}
