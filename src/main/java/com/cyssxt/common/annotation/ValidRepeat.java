package com.cyssxt.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验重复请求
 */
@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ValidRepeat {
}
