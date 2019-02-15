package com.cyssxt.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {
        private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

        protected HttpServletRequest request;
        protected HttpServletResponse response;
        protected HttpSession session;

        @ModelAttribute
        public void setReqAndRes( HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
            this.request = httpServletRequest;
            this.response = httpServletResponse;
            this.session = httpServletRequest.getSession();
//        this.responseData = ResponseData.getDefaultSuccessResponse(baseReq);

        }

}
