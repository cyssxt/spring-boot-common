package com.cyssxt.common.hibernate.transformer;

import com.cyssxt.common.bean.ReflectBean;
import com.cyssxt.common.utils.ReflectUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class DataTransformer extends IgnoreCaseResultTransformer {

    private final static Logger logger = LoggerFactory.getLogger(DataTransformer.class);

    private Filter filter;

    public DataTransformer(Class clazz,Filter filter) {
        super(clazz);
        this.filter = filter;
    }

    @Override
    public List<String> getKeys() {
        return null;
    }

    public Object transformTuple(final Object[] tuple, final String[] aliases) {
        Object result;
        String lastKey = null;
        try {
            result = this.getResultClass().newInstance();
            Map<String, ReflectBean> reflectBeans = ReflectUtils.getBeanMap(getResultClass(),ReflectUtils.WRITE,true);
            for (int i=0;i<aliases.length;i++) {
                Object object = tuple[i];
                String alias = aliases[i];
                lastKey = alias;
                alias = alias.replace("_","").toLowerCase();
                ReflectBean reflectBean = reflectBeans.get(alias);
                if(reflectBean==null){
                    logger.error("reflectBean is null={}",alias);
                    continue;
                }
                ReflectUtils.copyValue(reflectBean,object,result);
            }
            if(result!=null) {
                filter.callback(result);
            }
        } catch (Exception e) {
            throw new HibernateException(lastKey+"=lastKey,Could not instantiate resultclass: " + this.getResultClass().getName(), e);
        }
        return result;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }

    public interface Filter<T> {
        void callback(T result);
    }
}
