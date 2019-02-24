package com.cyssxt.common.hibernate.transformer;

import com.cyssxt.common.bean.ReflectBean;
import com.cyssxt.common.utils.CommonUtils;
import com.cyssxt.common.utils.ReflectUtils;
import lombok.Getter;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class IgnoreCaseResultTransformer implements KeyTransformer {
    private static final long serialVersionUID = -3779317531110592988L;
    private final static Logger logger = LoggerFactory.getLogger(IgnoreCaseResultTransformer.class);

    @Getter
    private final Class<?> resultClass;
    @Getter
    private List<String> keys = new ArrayList<>();
    private String keyName;
    public IgnoreCaseResultTransformer(final Class<?> resultClass,String keyName) {
        this.resultClass = resultClass;
        this.keyName = keyName;
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
}
