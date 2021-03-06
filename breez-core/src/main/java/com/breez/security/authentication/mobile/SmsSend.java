package com.breez.security.authentication.mobile;

/**
 * 短信发送统一接口
 */
public interface SmsSend {

    /**
     * 发送短信
     * @param mobile 手机号
     * @param content 发送的内容
     * @return true表示发送成功，false表示失败
     */
    boolean sendSms(String mobile, String content);
}
