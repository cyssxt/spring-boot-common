package com.cyssxt.common.listener;

import com.cyssxt.common.annotation.Authorization;

public interface UserLoginListener {

    boolean login(Authorization authorization);
}
