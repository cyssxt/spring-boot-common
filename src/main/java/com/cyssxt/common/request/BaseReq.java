package com.cyssxt.common.request;

import com.alibaba.fastjson.JSON;
import com.cyssxt.common.bean.Copy;
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
public class BaseReq extends Copy {
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
