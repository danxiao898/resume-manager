package com.breez.security.authentication;

import com.breez.base.result.BreezResult;
import com.breez.security.properties.LoginResponseType;
import com.breez.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理器
 * 1.决定响应json还是跳转页面，或者认证成功后做其他处理
 */
@Component("customAuthenticationSuccessHandler")
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    SecurityProperties securityProperties;

    @Autowired(required = false)//容器中可以没有这个接口的实现，如果有则自动注入
    AuthenticationSuccessListener authenticationSuccessListener;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        if(authenticationSuccessListener != null) {
            //当认证成功后，调用此监听，进行后续处理
            authenticationSuccessListener.successListener(httpServletRequest, httpServletResponse, authentication);
        }

        if(LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
            BreezResult result = BreezResult.ok("认证成功");
            String json = result.toJsonString();
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(json);
        } else {
            //重定向到上次请求的地址，即引发跳转到认证页面的地址
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }

    }
}
