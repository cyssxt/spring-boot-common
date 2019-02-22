package com.cyssxt.common.filters;

public interface JsonDecoderFilter {
    String[] getExcludeFields();
    String[] getIncludeFields();
    Class loadBtoClass();
}
