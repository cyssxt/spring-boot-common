package com.cyssxt.common.hibernate.transformer;

import com.cyssxt.common.bean.ReflectBean;
import com.cyssxt.common.utils.ReflectUtils;
import lombok.Getter;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IgnoreCaseResultTransformer implements KeyTransformer {
    private static final long serialVersionUID = -3779317531110592988L;
    private final static Logger logger = LoggerFactory.getLogger(IgnoreCaseResultTransformer.class);

    @Getter
    private final Class<?> resultClass;
    @Getter
    private List<String> keys = new ArrayList<>();
    private Filter filter;
    private String keyName;
    public IgnoreCaseResultTransformer(final Class<?> resultClass,String keyName) {
        this.resultClass = resultClass;
        this.keyName = keyName;
    }
    public IgnoreCaseResultTransformer(final Class<?> resultClass, Filter filter) {
        this.resultClass = resultClass;
        this.filter = filter;
    }
    public IgnoreCaseResultTransformer(final Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    /**
     * aliases为每条记录的数据库字段名,ORACLE字段名默认为大写
     * tupe为与aliases对应的字段的值
     */
    public Object transformTuple(final Object[] tuple, final String[] aliases) {
        String lastKey = null;
        Object result;
        try {
            result = this.resultClass.newInstance();
            Map<String, ReflectBean> reflectBeans = ReflectUtils.getBeanMap(resultClass,ReflectUtils.WRITE,true);
            for (int i=0;i<aliases.length;i++) {
                Object object = tuple[i];
                String alias = aliases[i];
                lastKey = alias;
                alias = alias.replace("_","").toLowerCase();
                if(alias.equals(this.keyName)){
                   keys.add(object+"");//主键转为string
                }
                ReflectBean reflectBean = reflectBeans.get(alias);
                if(reflectBean==null){
                    logger.error("reflectBean is null={}",alias);
                    continue;
                }
                if(object!=null) {
                    ReflectUtils.copyValue(reflectBean, object, result);
                }
            }
            if(filter!=null){
                filter.callback(result);
            }
        } catch (Exception e) {
            throw new HibernateException("Could not instantiate resultclass: " + this.resultClass.getName()+",lastKey="+lastKey, e);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List transformList(final List collection) {
        return collection;
    }

    public static void main(String[] args) {
//        System.out.println(（）));
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    @Override
    public boolean[] includeInTransform(String[] aliases, int tupleLength) {
        if ( aliases == null ) {
            throw new IllegalArgumentException( "aliases cannot be null" );
        }
        if ( aliases.length != tupleLength ) {
            throw new IllegalArgumentException(
                    "aliases and tupleLength must have the same length; " +
                            "aliases.length=" + aliases.length + "tupleLength=" + tupleLength
            );
        }
        boolean[] includeInTransform = new boolean[tupleLength];
        for ( int i = 0 ; i < aliases.length ; i++ ) {
            if ( aliases[ i ] != null ) {
                includeInTransform[ i ] = true;
            }
        }
        return includeInTransform;
    }
}
