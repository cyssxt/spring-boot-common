package com.cyssxt.common.annotation;

import com.cyssxt.common.constant.ErrorMessage;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zqy on 2018/6/15.
 */
@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Authorization {

    String roleCode() default "";

    byte[] value() default {};

    boolean isAll() default false;

    boolean isAdmin() default false;

    boolean isVip() default false;

    boolean existSession() default false;//只校验session是否存在

    ErrorMessage message() default ErrorMessage.AUTH_NOT_ENOUGH;
}
