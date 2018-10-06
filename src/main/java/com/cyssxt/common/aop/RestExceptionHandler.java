package com.cyssxt.common.aop;


import com.alibaba.fastjson.JSONException;
import com.cyssxt.common.constant.ErrorMessage;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by 520cloud on 2017-09-08.
 */
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = ValidException.class)
    public ResponseData HttpMessageNotReadableException(HttpServletRequest req, Exception e) throws Exception {
        ValidException validException = null;
        if(e instanceof UndeclaredThrowableException){
            UndeclaredThrowableException undeclaredThrowableException = (UndeclaredThrowableException)e;
            Throwable throwable = undeclaredThrowableException.getUndeclaredThrowable();
            if(throwable instanceof ValidException){
                validException = (ValidException) throwable;
            }
        }else{
            validException = (ValidException)e;
        }
        return validException.getResponseData();
    }

    @ExceptionHandler(value=JSONException.class)
    public ResponseData JSONParseError(HttpServletRequest req, Exception e) throws Exception {
        ValidException validException = null;
        ResponseData responseData = ResponseData.getDefaultFailResponse();
        responseData.parseCode(ErrorMessage.JSON_PARSE_ERROR.getMessageInfo());
        responseData.setData(e.toString());
        return responseData;
    }

}
