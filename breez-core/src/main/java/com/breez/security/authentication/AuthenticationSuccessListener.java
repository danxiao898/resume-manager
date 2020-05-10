package com.breez.security.authentication;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个接口用来监听认证成功后的处理，也就是说认证成功后让成功处理器调用此方法successListener
 */
public interface AuthenticationSuccessListener {
    void successListener(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication);
}
