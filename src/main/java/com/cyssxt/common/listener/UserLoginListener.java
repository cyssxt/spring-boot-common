package com.cyssxt.common.listener;

import com.cyssxt.common.annotation.Authorization;

public abstract class UserLoginListener {

    public abstract boolean login(Authorization authorization, String sessionId);

    public boolean checkSessionId(String sessionId){
        return false;
    }
}
