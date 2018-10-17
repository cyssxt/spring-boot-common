package com.cyssxt.common.request;

import com.alibaba.fastjson.JSON;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.utils.CommonUtils;
import com.cyssxt.common.utils.ReflectUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zqy on 13/05/2018.
 */
public class BaseReq {
    private final static Logger logger = LoggerFactory.getLogger(BaseReq.class);
    private String reqId;
    private String sessionId;
    @Setter
    @Getter
    private Byte clientType;
//    @ApiModelProperty(value="需要登录",required = false)
//    private boolean shouldLogin = false;

    public String getReqId() {
        return reqId==null? CommonUtils.generatorKey():reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

//    public boolean isShouldLogin() {
//        return shouldLogin;
//    }
//
//    public void setShouldLogin(boolean shouldLogin) {
//        this.shouldLogin = shouldLogin;
//    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
