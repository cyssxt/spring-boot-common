package com.cyssxt.common.bean;

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
        Map<String,Method> readMethods = null;
        Map<String,Method> writeMethods = null;
        try {
            readMethods = ReflectUtils.getReadMapper(this.getClass());
            writeMethods  = ReflectUtils.getWriteMap(t.getClass());
            Iterator<String> iterator = readMethods.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                Method read = readMethods.get(key);
                Object object = read.invoke(this);
                Method writeMethod =writeMethods.get(key);
                if((object!=null || "expireTime".equals(key)) && writeMethod!=null){
                    logger.debug("parse,fieldName={}",key);
                    writeMethod.invoke(t,object);
                }
            }
        } catch (Exception e) {
            throw new ValidException("");
        }

        return t;
    }

}