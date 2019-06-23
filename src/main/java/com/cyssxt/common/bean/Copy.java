package com.cyssxt.common.bean;

import com.cyssxt.common.constant.ErrorMessage;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class Copy {
    private final Logger logger = LoggerFactory.getLogger(Copy.class);
    public <T>T parse(T t) throws ValidException {
        parse(t, (key, o) -> o!=null || "expireTime".equals(key));
        return t;
    }
    public <T>T parse(T t,Filter filter) throws ValidException {
        return parse(t,filter,false,null);
    }
    public <T>T parse(T t, Filter filter, boolean flag, ReflectUtils.FieldListener fieldListener) throws ValidException {
        Map<String,Method> readMethods = null;
        Map<String,Method> writeMethods = null;
        try {
            Class readClass  = flag?t.getClass():this.getClass();
            Class writeClass  = flag?this.getClass():t.getClass();
            readMethods = ReflectUtils.getReadMapper(readClass,fieldListener);
            writeMethods  = ReflectUtils.getWriteMap(writeClass,fieldListener);
            Iterator<String> iterator = readMethods.keySet().iterator();
            String lastKey = null;
            while (iterator.hasNext()){
                String key = iterator.next();
                lastKey = key;
                Method read = readMethods.get(key);
                Object object = read.invoke(flag?t:this);
                Method writeMethod =writeMethods.get(key);
                if((filter==null || filter.valid(key,object)) && writeMethod!=null){
                    logger.debug("parse,fieldName={}",key);
                    writeMethod.invoke(flag?this:t,object);
                }
            }
        } catch (Exception e) {
            throw new ValidException(ErrorMessage.PARSE_ERROR.getMessageInfo(),e);
        }

        return t;
    }

    public interface Filter {
        boolean valid(String key,Object o);
    }

}
