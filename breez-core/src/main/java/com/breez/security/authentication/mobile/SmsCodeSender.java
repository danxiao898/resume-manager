package com.breez.security.authentication.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送短信验证码
 */
public class SmsCodeSender implements SmsSend {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *
     * @param mobile 手机号
     * @param content 发送的内容: 接受的是验证码
     * @return
     */
    @Override
    public boolean sendSms(String mobile, String content) {
        String sendContent = String.format("梦学谷，验证码%s, 请勿泄露",content);
        //调用第三方sdk发送
        logger.info("向手机号:" + mobile + ",发送的短信是:" + sendContent);
        return true;
    }
}
