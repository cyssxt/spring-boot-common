package com.cyssxt.common.listener;

import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.exception.ValidException;

public abstract class UserLoginListener {

    public abstract boolean checkLogin(Authorization authorization,String sessionId) throws ValidException;

    /**
     * 校验sessionId是否合法改动 默认合法，主要用于需要校验sessionId的合法性
     * @param sessionId
     * @return
     */
    public boolean checkSessionId(String sessionId){
        return true;
    }

}
