package com.cyssxt.common.config.bean;


/**
 * author:cyssxt
 * date:2018/9/7
 * $comment$
 **/
public abstract class FilterJSONSerializer {
    private String[] excludeFields;
    public abstract String[] getExcludeFields();
}
