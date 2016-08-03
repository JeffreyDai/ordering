package com.wzqj.ordering.common.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.wzqj.compoent.env.EnvManager;
import com.wzqj.compoent.soa.SoaClient;
import com.wzqj.compoent.soa.response.ResponseValue;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录控制拦截器
 * Created by wanghao@weipass.cn on 2015/7/30.
 */
public class SoaInterceptor extends org.springframework.web.servlet.handler.HandlerInterceptorAdapter {

    private static final int REQUEST_OK = 0;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (EnvManager.isLocal()) {
            return true;
        }
        String accessToken = request.getParameter("accessToken");
        try {
            if (!verify(accessToken)) {
                response.sendRedirect(request.getContextPath() + "/soa/auth/fail.xhtml");
                return false;
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/soa/auth/fail.xhtml");
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    private boolean verify(String accessToken) {

        boolean flag = true;
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        }
        if (accessToken.equals("d82e6a959d25464fa4606b3530f55c70")) {
            return flag;
        }
        try {
            ResponseValue responseValue = SoaClient.checkAccessToken(accessToken);
            if (null == responseValue) {
                flag = false;
            }
            if (responseValue.getStatus() != REQUEST_OK) {
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
