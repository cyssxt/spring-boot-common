package com.cyssxt.common.bean;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ReflectBean {

    String fieldName;
    Method method;
    Class fieldType;
    public ReflectBean(String fieldName, Method method, Class fieldType) {
        this.fieldName = fieldName;
        this.method = method;
        this.fieldType = fieldType;
    }
}
