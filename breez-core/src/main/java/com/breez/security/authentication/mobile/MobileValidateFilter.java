package com.breez.security.authentication.mobile;

import com.breez.security.authentication.exception.ValidateCodeException;
import com.breez.security.authentication.CustomAuthenticationFailureHandler;
import com.breez.security.controller.MobileLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 校验用户输入的验证码是否正确
 */
@Component("mobileValidateFilter")
public class MobileValidateFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 1. 判断是否为手机登录，且post请求
        if("/mobile/form".equals(httpServletRequest.getRequestURI())
            && "post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            try {
                //校验验证码合法性
                validate(httpServletRequest);
                logger.info("验证码校验成功");
            } catch (AuthenticationException e) {
                logger.info("验证码校验失败");
                //交给失败处理器处理异常
                customAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest) {
        //先获取session中的验证码
        String sessionCode = (String) httpServletRequest.getSession().getAttribute(MobileLoginController.SESSION_KEY);

        //获取用户输入的验证码
        String inputCode = httpServletRequest.getParameter("code");
        //判断是否正确

        if(StringUtils.isEmpty(inputCode)) {
            logger.info("验证码不能为空");
            throw new ValidateCodeException("验证码不能为空");
        }

        if(!inputCode.equalsIgnoreCase(sessionCode)) {
            logger.info("验证码输入错误");
            throw new ValidateCodeException("验证码输入错误");
        }
    }
}
