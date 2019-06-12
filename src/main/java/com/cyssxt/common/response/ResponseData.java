package com.cyssxt.common.response;

import com.cyssxt.common.constant.ErrorMessage;
import com.cyssxt.common.message.bean.MessageInfo;
import com.cyssxt.common.request.BaseReq;
import com.cyssxt.common.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 520cloud on 2017-09-05.
 */
@Setter
@Getter
public class ResponseData<T> {
    private String reqId = null;
    private String retCode;
    private String sessionId = null;
    private String contentType;
    private String retMsg;
    private long reqTime=0;
    private String nextReqId;
    private Throwable exception;
    private Object extra;
    private T data;
    private List<String> errors = new ArrayList<>();
    public ResponseData(){
        this.parseCode(ErrorMessage.SUCCESS.getMessageInfo());
    }
    public static ResponseData getResponse(Object t, MessageInfo messageInfo, BaseReq baseReq){
        ResponseData responseData = new ResponseData(messageInfo);
        responseData.setData(t);
        String sessionId = null;//baseReq.getSessionId();
        if(baseReq!=null){
            sessionId = baseReq.getSessionId();
            responseData.setReqId(baseReq.getReqId());
        }
        responseData.setSessionId(sessionId);
        return responseData;
    }
    public static ResponseData getResponse(Object t, MessageInfo messageInfo){
        return getResponse(t,messageInfo,null);
    }

    public static ResponseData getSuccessResponse(Object t){
        return getResponse(t, ErrorMessage.SUCCESS.getMessageInfo());
    }

    public static ResponseData getDefaultSuccessResponse(){
        return getResponse(null, ErrorMessage.SUCCESS.getMessageInfo());
    }

    public static ResponseData getFailResponse(MessageInfo messageInfo){
        return getResponse(null, messageInfo);
    }

    public static ResponseData getDefaultFailResponse() {
        return getFailResponse(ErrorMessage.FAIL.getMessageInfo());
    }

    public static ResponseData getDefaultSuccessResponse(BaseReq baseReq) {
        return getResponse(null, ErrorMessage.SUCCESS.getMessageInfo(),baseReq);
    }

    public ResponseData(MessageInfo messageInfo){
        this.parseCode(messageInfo);
    }
    public void  parseValidError(List<ObjectError> errors){
        if(CommonUtils.isNotEmpty(errors)){
            for(ObjectError error:errors){
                if(error instanceof FieldError) {
                    String msg = error.getDefaultMessage();
                    this.errors.add((((FieldError) error).getField()+ ":" + msg));
                }
            }
        }
    }

    public ResponseData(T t){
        this.setData(t);
        this.parseCode(ErrorMessage.SUCCESS.getMessageInfo());

    }
    public ResponseData(T t, MessageInfo messageInfo){
        this.setData(t);
        this.parseCode(messageInfo);
    }
    public void parseCode(MessageInfo messageInfo){
        this.setRetCode(messageInfo.getRetCode());
        this.setRetMsg(messageInfo.getRetMsg());
    }

    public void addError(String msg){
        errors.add(msg);
    }

    public void appendRetMsg(String msg){
        this.retMsg=this.retMsg+msg;
    }

}
