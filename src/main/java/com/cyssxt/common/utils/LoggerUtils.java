package com.cyssxt.common.utils;


import org.slf4j.LoggerFactory;

public class LoggerUtils {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(LoggerUtils.class);
    public static void get(Class clazz){
    }

    public static void main(String[] args) {

        get(LoggerUtils.class);
    }
}
