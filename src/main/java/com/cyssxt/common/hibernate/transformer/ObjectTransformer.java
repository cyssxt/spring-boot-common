package com.cyssxt.common.hibernate.transformer;

import org.hibernate.transform.ResultTransformer;

import java.util.List;

public class ObjectTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] objects, String[] strings) {
        return objects[0];
    }

    @Override
    public List transformList(List list) {
        return list;
    }
}
