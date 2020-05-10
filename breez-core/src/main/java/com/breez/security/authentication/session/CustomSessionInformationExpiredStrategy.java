package com.breez.security.authentication.session;

import com.breez.security.authentication.CustomAuthenticationFailureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 当同一用户的session数量达到时，会执行该类
 */
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException {
        //1. 获取用户名
        UserDetails principal = (UserDetails)sessionInformationExpiredEvent.getSessionInformation().getPrincipal();

        logger.info("同一用户的session数量达到设定值");

        AuthenticationException exception = new AuthenticationServiceException(String.format("[%s] 用户在另外一台电脑登录，您已下线",principal.getUsername()));


        try {
            //当用户在另一台电脑登陆后，交给失败处理器
            sessionInformationExpiredEvent.getRequest().setAttribute("toAuthentication", true);
            customAuthenticationFailureHandler
                    .onAuthenticationFailure(sessionInformationExpiredEvent.getRequest(),sessionInformationExpiredEvent.getResponse(),exception);
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
}
