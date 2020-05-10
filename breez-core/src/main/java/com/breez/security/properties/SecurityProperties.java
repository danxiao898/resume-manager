package com.breez.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mengxuegu.security")
@EnableConfigurationProperties
public class SecurityProperties {

    //会将mengxuegu.security.authentication自动加载进来
    private AuthenticationProperties authentication;

    public AuthenticationProperties getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationProperties authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return "SecurityProperties{" +
                "authentication=" + authentication +
                '}';
    }
}
