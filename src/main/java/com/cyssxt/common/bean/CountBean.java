package com.cyssxt.common.bean;

import lombok.Data;

@Data
public class CountBean {
    private Long count;
    private Long count2;

    private String rowId;

    public CountBean(Long count) {
        this.count = count;
    }

    public CountBean(Long count, String rowId) {
        this.count = count;
        this.rowId = rowId;
    }

    public CountBean(Long count, Long count2, String rowId) {
        this.count = count;
        this.count2 = count2;
        this.rowId = rowId;
    }
}
