package com.cyssxt.common.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");

    private Integer apiVersion;

    public ApiVersionCondition(Integer apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            if(version >= this.apiVersion) // 如果请求的版本号大于配置版本号， 则满足
                return this;
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return other.getApiVersion()-this.apiVersion;
    }
}
