package com.breez.security.authentication.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        //要将浏览器中的jsessionid删除
        cancelCookie(httpServletRequest, httpServletResponse);

//        MengxueguResult result = MengxueguResult.build(HttpStatus.UNAUTHORIZED.value(),
//                "登录已超时，请重新登录");
//
//        httpServletResponse.setContentType("application/json;charset=utf-8");
//        httpServletResponse.getWriter().write(result.toJsonString());
        httpServletResponse.sendRedirect("/");

    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }

    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        this.logger.debug("Cancelling cookie");
        Cookie cookie = new Cookie("MY_JSESSIONID", (String)null);
        cookie.setMaxAge(0);
        cookie.setPath(this.getCookiePath(request));
        response.addCookie(cookie);
    }
}
