package com.cyssxt.common.listener;

import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.exception.ValidException;

public abstract class UserLoginListener {

    public abstract void cacheUserInfo(String sessionId) throws ValidException;
    public abstract boolean login(Authorization authorization) throws ValidException;

    public boolean checkSessionId(String sessionId){
        return false;
    }
}
