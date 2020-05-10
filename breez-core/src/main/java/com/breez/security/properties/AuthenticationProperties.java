package com.breez.security.properties;


import lombok.Data;

import java.util.Arrays;

@Data
public class AuthenticationProperties {

    private String loginPage = "/login/page";
    private String loginProcessingUrl = "/login/form";
    private String usernameParameter = "name";
    private String passwordParameter = "pwd";
    private String[] staticPath = {"/dist/**","/modules/**","/plugins/**"};

    private String imageCodeUrl = "/code/image";
    private String mobileCodeUrl = "/code/mobile";
    private String mobilePage = "/mobile/page";
    private Integer tokenValiditySeconds = 604800;


    /**
     * 响应的类型
     */
    private LoginResponseType loginType;

    @Override
    public String toString() {
        return "AuthenticationProperties{" +
                "loginPage='" + loginPage + '\'' +
                ", loginProcessingUrl='" + loginProcessingUrl + '\'' +
                ", usernameParameter='" + usernameParameter + '\'' +
                ", passwordParameter='" + passwordParameter + '\'' +
                ", staticPath=" + Arrays.toString(staticPath) +
                ", imageCodeUrl='" + imageCodeUrl + '\'' +
                ", mobileCodeUrl='" + mobileCodeUrl + '\'' +
                ", mobilePage='" + mobilePage + '\'' +
                ", tokenValiditySeconds=" + tokenValiditySeconds +
                ", loginType=" + loginType +
                '}';
    }
}
