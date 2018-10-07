package com.cyssxt.common.utils;

import com.cyssxt.common.dao.RedisDao;
import com.cyssxt.common.listener.SpringContextListener;

public class ServiceUtil {

    private static RedisDao _redisDao = null;

    public static RedisDao getRedisDao(){
        if(_redisDao==null){
            _redisDao = (RedisDao)SpringContextListener.getBean("redisDao");
        }
        return _redisDao;
    }
}
