package com.cyssxt.common.utils;

import org.springframework.util.StringUtils;

public class ArrayUtil {
    public static final String join(final Object[] arr, final String joinStr) {
        if (arr == null || arr.length < 1) {
            return "";
        }
        final StringBuffer sb = new StringBuffer(String.valueOf(arr[0]));
        for (int i = 1, len = arr.length; i < len; i++) {
            sb.append(!StringUtils.isEmpty(joinStr) ? joinStr : "").append(String.valueOf(arr[i]));
        }
        return sb.toString();
    }
}
