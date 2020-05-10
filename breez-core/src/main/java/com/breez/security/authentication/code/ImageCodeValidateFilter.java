package com.breez.security.authentication.code;

import com.breez.security.authentication.exception.ValidateCodeException;
import com.breez.security.authentication.CustomAuthenticationFailureHandler;
import com.breez.security.controller.CustomerLoginController;
import com.breez.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OncePerRequestFilter: 所有请求之前被调用一次
 */
@Component("imageCodeValidateFilter")
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    SecurityProperties securityProperties;

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
         HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //1. 如果是登录请求，则校验输入的验证码是否正确
        if(securityProperties.getAuthentication().getLoginProcessingUrl()
            .equalsIgnoreCase(httpServletRequest.getRequestURI())
                && httpServletRequest.getMethod().equalsIgnoreCase("post")) {
            try {
                //校验验证码合法性
                validate(httpServletRequest);
            } catch (AuthenticationException e) {
                //交给失败处理器处理异常
                customAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest) {
        //先获取session中的验证码
        String sessionCode = (String) httpServletRequest.getSession().getAttribute(CustomerLoginController.SESSION_KEY);

        //获取用户输入的验证码
        String inputCode = httpServletRequest.getParameter("code");
        //判断是否正确

        if(StringUtils.isEmpty(inputCode)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        if(!inputCode.equalsIgnoreCase(sessionCode)) {
            throw new ValidateCodeException("验证码输入错误");
        }
    }
}
