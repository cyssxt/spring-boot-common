package com.cyssxt.common.hibernate.transformer;


public interface Filter<T> {

    void callback(T t);
}
