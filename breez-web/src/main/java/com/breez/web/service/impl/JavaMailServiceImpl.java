package com.breez.web.service.impl;

import com.breez.web.service.JavaMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class JavaMailServiceImpl implements JavaMailService {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    /**
     *
     * @param title 邮件主题
     * @param msg 邮件内容
     * @param to 接收者
     */
    public void sendSimpleMail(String title, String msg, String to) {
        //封装简单邮件内容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(msg);

        //发件人
        message.setFrom(javaMailSender.getUsername());
        //收件人
        message.setTo(to);
        javaMailSender.send(message);
    }
}
