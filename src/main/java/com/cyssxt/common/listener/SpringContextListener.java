package com.cyssxt.common.listener;

import com.cyssxt.common.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class SpringContextListener<C extends ApplicationContextEvent> implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(SpringContextListener.class);
    private static ApplicationContext context;
    @Resource
    private RedisDao redisDao;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        context = contextRefreshedEvent.getApplicationContext();
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    /**
     * 初始化schedule
     */
}
