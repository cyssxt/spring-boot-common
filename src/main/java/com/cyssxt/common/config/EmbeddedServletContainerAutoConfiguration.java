package com.cyssxt.common.config;


import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Created by 520cloud on 2017-09-11.
 */
@Configuration
public class EmbeddedServletContainerAutoConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(EmbeddedServletContainerAutoConfiguration.class);

    @Value("${server.tomcat.docbase:/tmp/springboot}")
    private  String tomcatBaseDirectory;

    @Value("${server.https.enable:false}")
    private boolean httpsEnable;

    @Value("${http.port:8080}")
    private  Integer httpPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory = new TomcatServletWebServerFactory();
        logger.info("tomcatEmbeddedServletContainerFactory:{}",this.tomcatBaseDirectory);
        File file =new File(this.tomcatBaseDirectory);
        if(!file.exists()){
            file.mkdirs();
        }
        tomcatEmbeddedServletContainerFactory.setDocumentRoot(file);
        tomcatEmbeddedServletContainerFactory.setBaseDirectory(file);
        if(httpsEnable) {
            tomcatEmbeddedServletContainerFactory.addAdditionalTomcatConnectors(createStandardConnector());
        }
        return tomcatEmbeddedServletContainerFactory;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        return connector;
    }

//    @Bean
//    public ConfigurableServletWebServerFactory servletContainer() {
//
//    }
}