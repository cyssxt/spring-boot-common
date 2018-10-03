package com.cyssxt.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:cyssxt
 * date:2018/9/15
 * $comment$
 **/
public class FilterUtils {
    final static ThreadLocal<List<String>> threadLocal = new ThreadLocal();

    public static void set(List<String> filters){
        threadLocal.set(filters);
    }

    public static List<String> get(){
        return threadLocal.get();
    }

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("123");
        System.out.println(test.contains("123"));
    }
}
