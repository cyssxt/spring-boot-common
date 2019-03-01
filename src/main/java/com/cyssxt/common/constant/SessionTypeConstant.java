package com.cyssxt.common.constant;

public enum SessionTypeConstant {
    SMS((byte)0,"发送验证码"),
    LOGIN((byte)1,"登陆"),
    GUEST((byte)-1,"GUEST"),
    ;
    private Byte value;
    private String msg;

    SessionTypeConstant(Byte value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
