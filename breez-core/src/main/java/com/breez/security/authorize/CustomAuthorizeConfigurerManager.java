package com.breez.security.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 授權配置統一接口，將所有模塊的授權配置類管理起來
 */
@Component
public class CustomAuthorizeConfigurerManager implements AuthorizeConfigurerManager {

    /**
     * 這樣寫就可以拿到這個接口所有的實現
     */
    @Autowired
    List<AuthorizeConfigurerProvider> authorizeConfigurerProviders;

    /**
     * 將一個個的AuthorizeConfigurerProvider實現類傳入配置的參數expressionInterceptUrlRegistry
     * @param expressionInterceptUrlRegistry
     */
    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        for(AuthorizeConfigurerProvider provider : authorizeConfigurerProviders) {
            provider.config(expressionInterceptUrlRegistry);
        }
    }
}
