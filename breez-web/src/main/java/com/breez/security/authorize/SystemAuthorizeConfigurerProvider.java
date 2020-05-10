package com.breez.security.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 用户系统管理的授权配置
 */
@Component
public class SystemAuthorizeConfigurerProvider implements AuthorizeConfigurerProvider{
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
//        //                .antMatchers("/user").hasRole("ADMIN")//設置橘色時會自動加上ROLE_作為前綴
//        //有sys:user權限的用戶可以以任意請求方式訪問/user
//        expressionInterceptUrlRegistry.antMatchers("/user").hasAuthority("sys:user")
//                //有sys:role權限的用戶可以以get方式訪問/role
//                .antMatchers(HttpMethod.GET,"/role").hasAuthority("sys:role")
//                //有sys:permission權限的用戶或者角色"ADMIN"、"ROOT"可以訪問/permission
//                .antMatchers(HttpMethod.GET, "/permission")
//                .access("hasAnyAuthority('sys:permission') or hasAnyRole('ADMIN', 'ROOT')");
    }
}
