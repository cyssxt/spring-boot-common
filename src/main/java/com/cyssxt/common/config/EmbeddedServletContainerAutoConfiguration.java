package com.cyssxt.common.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by 520cloud on 2017-09-11.
 */
@Configuration
public class EmbeddedServletContainerAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(EmbeddedServletContainerAutoConfiguration.class);

    @Value("${server.tomcat.docbase}")
    private  String tomcatBaseDirectory;
    @Bean
    public ConfigurableServletWebServerFactory servletContainer() {
        logger.info("tomcatEmbeddedServletContainerFactory:{}",this.tomcatBaseDirectory);
        TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory = new TomcatServletWebServerFactory();
        File file =new File(this.tomcatBaseDirectory);
        if(!file.exists()){
            file.mkdirs();
        }
        tomcatEmbeddedServletContainerFactory.setDocumentRoot(file);
        tomcatEmbeddedServletContainerFactory.setBaseDirectory(file);
        return tomcatEmbeddedServletContainerFactory;
    }
}