package com.cyssxt.common.annotation;


import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    int value() default 1;
}
