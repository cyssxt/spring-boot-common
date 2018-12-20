package com.cyssxt.common.exception;


import com.cyssxt.common.message.MessageHelper;
import com.cyssxt.common.message.bean.MessageInfo;
import com.cyssxt.common.response.ResponseData;

/**
 * Created by zqy on 13/05/2018.
 */
public class ValidException extends Exception {
    private ResponseData responseData = null;
    public ValidException(ResponseData responseData) {
        super("参数异常");
        this.responseData = responseData;
    }
    public ValidException(MessageInfo e) {
        super(e.getRetMsg());
        this.responseData = ResponseData.getFailResponse(e);
    }

    public ValidException(String code){
        super(code);
        MessageInfo messageInfo = MessageHelper.getMessageInfo(code);
        this.responseData = ResponseData.getFailResponse(messageInfo);
    }

    public ValidException(String code,Exception e){
        super(code);
        MessageInfo messageInfo = MessageHelper.getMessageInfo(code);
        this.responseData = ResponseData.getFailResponse(messageInfo);
        this.responseData.getErrors().add(e);
        this.responseData.setException(e);
    }

    public ValidException(MessageInfo e, Throwable throwable){
        super(e.getRetMsg());
        this.responseData = ResponseData.getFailResponse(e);
        this.responseData.setException(throwable);
    }

    public ValidException(MessageInfo e, String msg, Object o){
        super(e.getRetMsg());
        this.responseData = ResponseData.getFailResponse(e);
        this.responseData.setRetMsg(msg);
        this.responseData.setData(o);
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }
}
