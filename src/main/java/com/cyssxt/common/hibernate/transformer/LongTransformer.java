package com.cyssxt.common.hibernate.transformer;

import org.hibernate.transform.ResultTransformer;

import java.util.List;

public class LongTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        if(tuple==null || tuple.length<1 || tuple[0]==null){
            return null;
        }
        return Long.valueOf(tuple[0]+"");
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}
