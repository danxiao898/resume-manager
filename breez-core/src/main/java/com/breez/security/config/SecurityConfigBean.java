package com.breez.security.config;

import com.breez.security.authentication.mobile.SmsCodeSender;
import com.breez.security.authentication.mobile.SmsSend;
import com.breez.security.authentication.session.CustomInvalidSessionStrategy;
import com.breez.security.authentication.session.CustomSessionInformationExpiredStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 主要为容器中添加Bean实例
 */
@Configuration
public class SecurityConfigBean {

    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new CustomSessionInformationExpiredStrategy();
    }
    /**
     * session失效后的处理类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new CustomInvalidSessionStrategy();
    }

    /**
     * @ConditionalOnMissingBean : 如果容器中有创建这个接口的实例，则当前实例失效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsSend smsSend() {
        return new SmsCodeSender();
    }
}
