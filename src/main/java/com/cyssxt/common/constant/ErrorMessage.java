package com.cyssxt.common.constant;

import com.cyssxt.common.message.bean.MessageInfo;
import lombok.Getter;
import lombok.Setter;

public enum ErrorMessage {
    SUCCESS(new MessageInfo("0","SUCCESS"),"成功"),
    FAIL(new MessageInfo("system.fail","FAIL"),"失败"),
    ID_NOT_NULL(new MessageInfo("content.id.notnull","ID不能为空"),"ID不能为空"),
    NOT_EXIST(new MessageInfo("content.notexist","记录不存在"),"记录不存在"),
    HAS_DELETE(new MessageInfo("content.hasdelete","记录已经被删除"),"记录已经被删除"),
    LOAD_SQL_DATA_ERROR(new MessageInfo("sql.error","加载sql数据失败"),"加载sql数据失败"),
    PARSE_ERROR(new MessageInfo("parse.error","加载sql数据失败"),"加载sql数据失败"),
    AUTH_NOT_ENOUGH(new MessageInfo("user.auth.notenough",""),"" ),
    SHOW_LOGIN_AUTH_NOT_ENOUGH(new MessageInfo("user.should.loginorauth","登录失败或者权限不足"),"登录失败或者权限不足" ),
    JSON_PARSE_ERROR(new MessageInfo("system.json.parseerror","系统json解析错误"),"系统json解析错误" ),
    SESSION_NOT_VALID(new MessageInfo("system.session.notvalid","session无效"),"session无效" );

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
