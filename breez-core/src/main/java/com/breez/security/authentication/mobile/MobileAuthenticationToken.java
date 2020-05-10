package com.breez.security.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MobileAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 520L;

    private final Object principal;//认证前是手机号码，认证后是用户信息

    /**
     * 认证之前使用的构造方法，此方法会标识未认证
     * @param principal
     */
    public MobileAuthenticationToken(Object principal) {
        super((Collection)null);
        this.principal = principal;//手机号码
        this.setAuthenticated(false);
    }

    /**
     * 认证通过之后会重新创建MobileAuthenticationToken实例，来进行封装认证信息
     * @param principal 用户信息
     * @param authorities 权限资源
     */
    public MobileAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    /**
     * 因为他是父类中的抽象方法
     * @return
     */
    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
