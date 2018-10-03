package com.cyssxt.common.constant;

import com.cyssxt.common.message.bean.MessageInfo;
import lombok.Getter;
import lombok.Setter;

public enum ErrorMessage {
    SUCCESS(new MessageInfo("0","SUCCESS"),"成功"),
    FAIL(new MessageInfo("99999999","FAIL"),"失败"),
    ID_NOT_NULL(new MessageInfo("99999998","ID不能为空"),"ID不能为空"),
    NOT_EXIST(new MessageInfo("99999997","记录不存在"),"记录不存在"),
    HAS_DELETE(new MessageInfo("99999996","记录已经被删除"),"记录已经被删除"),
    LOAD_SQL_DATA_ERROR(new MessageInfo("99999995","加载sql数据失败"),"加载sql数据失败"),
    ;
    @Setter
    @Getter
    private MessageInfo messageInfo;
    @Setter
    @Getter
    private String msg;

    ErrorMessage(MessageInfo messageInfo, String msg) {
        this.messageInfo = messageInfo;
        this.msg = msg;
    }
}
