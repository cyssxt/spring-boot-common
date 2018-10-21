package com.cyssxt.common.bean;

import lombok.Data;

@Data
public class CountBean {
    private Long count;

    public CountBean(Long count) {
        this.count = count;
    }
}
