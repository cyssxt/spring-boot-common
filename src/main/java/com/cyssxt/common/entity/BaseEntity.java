package com.cyssxt.common.entity;

import com.cyssxt.common.bean.Copy;
import com.cyssxt.common.filters.JsonDecoderFilter;
import com.cyssxt.common.utils.CommonUtils;
import com.cyssxt.common.utils.DateUtils;
import com.cyssxt.common.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zqy on 18/05/2018.
 */
public abstract class BaseEntity extends Copy implements Serializable, JsonDecoderFilter {

    public abstract void setCreateTime(Timestamp timestamp);
    public abstract void setUpdateTime(Timestamp timestamp);
    public abstract Timestamp getCreateTime();
    public abstract void setRowId(String id);
    public abstract void setDelFlag(Boolean flag);
    public String[] defineExcludes(){return  new String[]{};}
    public abstract Boolean getDelFlag();
    private String[] excludeFields = new String[]{"delFlag","excludeFields","includeFields"};
    private String[] includeFields = null;

    public String[] getExcludeFields() {
        return excludeFields;
    }

    public void setExcludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

    public String[] getIncludeFields() {
        return includeFields;
    }

    public void setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

    public BaseEntity(){
        //初始化字段过滤
        String[] excludeFields = defineExcludes();
        if(excludeFields!=null && excludeFields.length>0) {
            int len = excludeFields.length;
            int tLen = this.excludeFields.length;
            excludeFields = Arrays.copyOf(excludeFields, len + tLen);//数组扩容
            System.arraycopy(this.excludeFields, 0, excludeFields, len, tLen);
            this.setExcludeFields(excludeFields);
        }
        this.setRowId(CommonUtils.generatorKey());
        this.setCreateTime(DateUtils.getCurrentTimestamp());
        this.setUpdateTime(this.getCreateTime());
        this.setDelFlag(false);
    }

    private final static Logger logger = LoggerFactory.getLogger(BaseEntity.class);

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String,Method> sourceMap = new HashMap<>();
        try {
            sourceMap = ReflectUtils.getReadMapper(this.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        Iterator<String> iterator = sourceMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            Method read = sourceMap.get(key);
            stringBuffer.append(key+"=");
            try {
                stringBuffer.append(read.invoke(this)+",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
//        logger.debug(stringBuffer.toString(),values);
        String value = stringBuffer.toString();
        logger.debug("{}toString,value={}",this.getClass().getName(),value);
        return value;
    }
    public Class loadBtoClass(){
        return null;
    }

    public boolean isDel(){
        return this.getDelFlag()!=null && this.getDelFlag();
    }


}
