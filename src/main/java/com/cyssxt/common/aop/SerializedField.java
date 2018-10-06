package com.cyssxt.common.aop;

/**
 * Created by 520cloud on 2017-09-05.
 */
public @interface SerializedField {
    /**
     * 需要返回的字段
     * @return
     */
    String[] includes() default {};

    /**
     * 需要去除的字段
     * @return
     */
    String[] excludes() default {};

    /**
     * 数据是否需要加密
     * @return
     */
    boolean encode() default true;
}
