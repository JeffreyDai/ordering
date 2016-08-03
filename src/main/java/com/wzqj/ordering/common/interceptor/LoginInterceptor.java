package com.wzqj.ordering.common.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.wzqj.compoent.constant.Const;
import com.wzqj.compoent.env.EnvManager;
import com.wzqj.compoent.util.CookieUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录控制拦截器
 * Created by wanghao@weipass.cn on 2015/7/30.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LogManager.getLogger();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (EnvManager.isLocal()) {
            return true;
        }
        Cookie token = CookieUtil.getCookieByName(request, Const.USER_TOKEN);
        if (null == token) {
            response.sendRedirect(request.getContextPath() + "/index/dispatch.xhtml");
            return false;
        }
        String userToken = token.getValue();
        if (StringUtils.isEmpty(userToken)) {
            response.sendRedirect(request.getContextPath() + "/index/dispatch.xhtml");
            return false;
        }
        return super.preHandle(request, response, handler);
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
