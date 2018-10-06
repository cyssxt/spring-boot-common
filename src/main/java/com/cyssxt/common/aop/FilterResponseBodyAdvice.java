package com.cyssxt.common.aop;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by 520cloud on 2017-09-05.
 */
public class FilterResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //判断返回的对象是单个对象，还是list，活着是map
        String[] includes= {};
        String[] excludes= {};
        boolean encode=false;
        if(o==null){
            return null;
        }
        if(methodParameter.getMethod().isAnnotationPresent(SerializedField.class)){
            //获取注解配置的包含和去除字段
            SerializedField serializedField = methodParameter.getMethodAnnotation(SerializedField.class);
            //过滤
            includes = serializedField.includes();
            //包含
            excludes = serializedField.excludes();
            //是否加密
            encode = serializedField.encode();
        }

        Object retObj = o;
        return retObj;
    }
    private Object handleSingleObject(Object o){
        return null;
    }
    private Object validIncludes(Object o,String[] includes){
        return null;
    }

    private Object validExcludes(Object o,String[] excludes){
        return null;
    }
}
