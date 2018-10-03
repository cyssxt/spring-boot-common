package com.cyssxt.common.message.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageInfo {

    private String retCode;
    private String retMsg;

    public MessageInfo(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }
}
