package com.breez.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class MobileAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 认证处理
     * 1.通过手机号码查询用户信息(UserDetailsServices)
     * 2.当查询到用户信息，则认为认证通过，封装Authentication对象
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //1.通过手机号码查询用户信息(UserDetailsServices)
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken)authentication;
        //获取手机号码
        String mobile = (String) mobileAuthenticationToken.getPrincipal();

        //通过手机号查询用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        //未查询到
        if(userDetails == null) {
            throw new AuthenticationServiceException("该手机号未注册");
        }

        //认证通过
        //封装到MobileAuthenticationToken
        MobileAuthenticationToken authenticationToken =
                new MobileAuthenticationToken(userDetails,userDetails.getAuthorities());

        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());

        return authenticationToken;
    }

    /**
     * 通过这个方法来选择对应的provider，返回true则选择当前provider
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return MobileAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
