package com.breez.security.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class CustomerLoginController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @GetMapping("/login/page")
    public String toLogin() {

        return "login";//templates/login.html
    }

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/code/image")
    public void imageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. 获取验证码字符串
        String code = defaultKaptcha.createText();
        //2.将字符串放到Session里
        request.getSession().setAttribute(SESSION_KEY, code);
        //3.获取验证码图片
        BufferedImage image = defaultKaptcha.createImage(code);
        //4.将验证码图片写出去
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }
}
