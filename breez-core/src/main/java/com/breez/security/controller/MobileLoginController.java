package com.breez.security.controller;

import com.breez.base.result.BreezResult;
import com.breez.security.authentication.mobile.SmsSend;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 关于手机登录控制层
 */
@Controller
public class MobileLoginController {

    public static final String SESSION_KEY = "SESSION_KEY_MOBILE";

    @GetMapping("/mobile/page")
    public String toMobilePage() {
        return "login-mobile";
    }


    @Autowired
    SmsSend smsSend;

    @ResponseBody
    @RequestMapping("/code/mobile")
    public BreezResult smsCode(HttpServletRequest request) {
        //1. 生成一个手机验证码
        String code = RandomStringUtils.randomNumeric(4);
        //2. 将手机验证码保存在session中
        request.getSession().setAttribute(SESSION_KEY, code);
        //3. 调用接口发送验证码到用户手机上
        String mobile = request.getParameter("mobile");
        smsSend.sendSms(mobile, code);

        return BreezResult.ok();
    }
}
