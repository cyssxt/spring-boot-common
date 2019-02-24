package com.cyssxt.common.constant;

public enum PageTypeConstant {
    FORCAST((byte)0,"预测"),
    INBOX((byte)1,"收件箱"),
    TODAY((byte)2,"今天"),
    WAIT((byte)3,"待完成"),
    ;
    private Byte value;
    private String msg;

    PageTypeConstant(Byte value, String msg) {
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

    public boolean compare(Byte type){
        if(type!=null && type.byteValue()==this.getValue().byteValue()){
            return true;
        }
        return false;
    }
}

