package com.breez.security.authentication;

import com.breez.base.result.BreezResult;
import com.breez.security.properties.LoginResponseType;
import com.breez.security.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理失败认证
 */
@Component("customAuthenticationFailureHandler")
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SecurityProperties securityProperties;

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e 认证失败时抛出的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        if(LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
            //认证失败，响应json字符串
            BreezResult result = BreezResult.build(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            String json = result.toJsonString();
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(json);
        } else {
            logger.info("认证失败,重定向到登录界面");
            // 重定向到认证界面
//            super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage() + "?error");
            String referer = httpServletRequest.getHeader("Referer");
            logger.info("referer:" + referer);


            //如果下面有值，直接认为是多端登录，返回一个登录地址
            Object toAuthentication = httpServletRequest.getAttribute("toAuthentication");
            String lastUrl = "";
            if(toAuthentication != null && (Boolean)toAuthentication == true) {
                lastUrl = securityProperties.getAuthentication().getLoginPage();
            } else {
                lastUrl = referer.substring(0, referer.lastIndexOf('?') == -1 ? referer.length() : referer.lastIndexOf('?'));
            }

            logger.info("上一次请求的路径:" + lastUrl);
            super.setDefaultFailureUrl(lastUrl + "?error");
            super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        }
    }
}
