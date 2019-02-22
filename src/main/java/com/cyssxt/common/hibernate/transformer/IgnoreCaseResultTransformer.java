package com.cyssxt.common.hibernate.transformer;

import com.cyssxt.common.bean.ReflectBean;
import com.cyssxt.common.utils.CommonUtils;
import com.cyssxt.common.utils.ReflectUtils;
import lombok.Getter;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class IgnoreCaseResultTransformer implements KeyTransformer {
    private static final long serialVersionUID = -3779317531110592988L;
    private final static Logger logger = LoggerFactory.getLogger(IgnoreCaseResultTransformer.class);

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
        Object result;
        try {
            result = this.resultClass.newInstance();
            Map<String, ReflectBean> reflectBeans = ReflectUtils.getBeanMap(resultClass,ReflectUtils.WRITE,true);
            for (int i=0;i<aliases.length;i++) {
                Object object = tuple[i];
                String alias = aliases[i];
                alias = alias.replace("_","").toLowerCase();
                if(alias.equals(this.keyName)){
                   keys.add(object+"");//主键转为string
                }
                ReflectBean reflectBean = reflectBeans.get(alias);
                if(reflectBean==null){
                    logger.error("reflectBean is null={}",alias);
                    continue;
                }
                Class type = reflectBean.getFieldType();
                if(object!=null) {
                    String obj = object+"";
                    if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                        object = "1".equals(obj)?true:Boolean.valueOf(obj);
                    } else if (type.equals(Integer.class) || type.equals(int.class)) {
                        object = Integer.valueOf(obj);
                    } else if (type.equals(Double.class) || type.equals(double.class)) {
                        object = Double.valueOf(obj);
                    } else if (type.equals(Float.class) || type.equals(float.class)) {
                        object = Float.valueOf(obj);
                    } else if (type.equals(Long.class) || type.equals(Long.class)) {
                        object = Long.valueOf(obj);
                    } else if (type.equals(Date.class) || type.equals(Timestamp.class)) {
                        if(CommonUtils.isInteger(obj)){
                            object = new Timestamp(Long.valueOf(obj));
                        }else {
                            object = Timestamp.valueOf(obj);
                        }
                    }else if (type.equals(Byte.class) || type.equals(byte.class)) {
                        object = Byte.valueOf(obj);
                    }
                }
                Method method = reflectBean.getMethod();
                method.invoke(result,object);
            }
        } catch (Exception e) {
            throw new HibernateException("Could not instantiate resultclass: " + this.resultClass.getName(), e);
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
