package com.cyssxt.common.filters;

public class DefaultFilter implements JsonDecoderFilter {

    @Override
    public String[] getExcludeFields() {
        return null;
    }

    @Override
    public String[] getIncludeFields() {
        return null;
    }

    @Override
    public Class loadBtoClass() {
        return null;
    }
}
