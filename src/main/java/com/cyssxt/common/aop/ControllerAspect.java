package com.cyssxt.common.aop;

import com.cyssxt.common.annotation.Authorization;
import com.cyssxt.common.annotation.ValidRepeat;
import com.cyssxt.common.constant.ErrorMessage;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.listener.UserLoginListener;
import com.cyssxt.common.request.BaseReq;
import com.cyssxt.common.response.ResponseData;
import com.cyssxt.common.utils.CommonUtils;
import com.cyssxt.common.utils.DateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;

@Aspect
@Component
public class ControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Resource
    private UserLoginListener userLoginListener;

//    @Pointcut("execution(public * com.cyssxt.*.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    public void point() {
//
//    }

    public String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("clientIp", ip);
        return ip;
    }

    @Around("execution(public * com.cyssxt.*.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object validController(ProceedingJoinPoint joinPoint) throws ValidException {
        Timestamp start = DateUtils.getCurrentTimestamp();
        logger.info("aop start={}", new java.util.Date());
        logger.info("controller={},valid={}", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] objects = joinPoint.getArgs();
        BindingResult bindingResult = null;
        String reqId = CommonUtils.generatorKey();
        String sessionId = null;
        BaseReq req = null;
        HttpServletRequest request = null;
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            //测试用例中，bindResult有可能传入为空
            if (object == null) {
                continue;
            }
            if (BaseReq.class.isAssignableFrom(object.getClass())) {
                req = (BaseReq) object;
                reqId = req.getReqId();
                sessionId = req.getSessionId();
            }
//            SpringValidatorAdapter
            if (object instanceof BindingResult) {
                bindingResult = (BindingResult) object;
                if (bindingResult.hasErrors()) {
                    ResponseData responseData = ResponseData.getFailResponse(ErrorMessage.PARSE_ERROR.getMessageInfo());
                    responseData.parseValidError(bindingResult.getAllErrors());
                    responseData.setReqId(reqId);
                    throw new ValidException(responseData);
                }
            }
            if (object instanceof HttpServletRequest) {
                request = (HttpServletRequest) object;
            }
        }
        //校验sessionId是否有效
        if (!StringUtils.isEmpty(sessionId)){
            if(!userLoginListener.checkSessionId(sessionId)) {
                throw new ValidException(ErrorMessage.SESSION_NOT_VALID.getMessageInfo());
            }
            userLoginListener.cacheUserInfo(sessionId);
        }
        Authorization authorization = method.getAnnotation(Authorization.class);
        if (authorization != null) {
            if (authorization.existSession()) {
                if (StringUtils.isEmpty(sessionId)) {
                    throw new ValidException(ErrorMessage.SESSION_NOT_VALID.getMessageInfo());
                }
            } else {
                if (userLoginListener != null && !userLoginListener.login(authorization)) {
                    throw new ValidException(ErrorMessage.AUTH_NOT_ENOUGH.getMessageInfo());
                }
            }
        }
        ValidRepeat validRepeat = method.getAnnotation(ValidRepeat.class);
        if (validRepeat != null) {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            //校验请求重复
//            validRepeat(req,className,methodName,request);
        }
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("validController={}", throwable);
            if (throwable instanceof ValidException) {
                throw (ValidException) throwable;
            }
            throw new ValidException(ErrorMessage.FAIL.getMessageInfo(), throwable);
        }
        Timestamp endTime = DateUtils.getCurrentTimestamp();
        if (result instanceof ResponseData) {
            ResponseData responseData = (ResponseData) result;
            String nextReqId = CommonUtils.generatorKey();
            responseData.setNextReqId(nextReqId);
            responseData.setReqTime(endTime.getTime() - start.getTime());
        }
        logger.info("aop end={},start={},end={}", new java.util.Date(), start, endTime);
        return result;
    }
}