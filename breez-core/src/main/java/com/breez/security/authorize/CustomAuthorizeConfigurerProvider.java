package com.breez.security.authorize;

import com.breez.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 身份認證相關的授權配置
 */
@Component
//值越小加载越优先，值越大，加载越靠后
@Order(Integer.MAX_VALUE)
public class CustomAuthorizeConfigurerProvider implements AuthorizeConfigurerProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        expressionInterceptUrlRegistry.antMatchers(securityProperties.getAuthentication().getLoginPage(),
                securityProperties.getAuthentication().getImageCodeUrl(),
                securityProperties.getAuthentication().getMobilePage(),
                securityProperties.getAuthentication().getMobileCodeUrl()
        ).permitAll();//放行请求

        expressionInterceptUrlRegistry.anyRequest().authenticated();//所有访问该应用的http请求都要通过身份认证才能实现访问
    }
}
