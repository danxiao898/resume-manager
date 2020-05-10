package com.breez.security;

import com.breez.web.entities.SysUser;
import com.breez.web.service.SysPermissionService;
import com.breez.web.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 查询数据库中的用户信息
 */
@Component("customUserDetailsService")
//public class CustomUserDetailsService implements UserDetailsService {
public class CustomUserDetailsService extends AbstractUserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public SysUser findSysUser(String usernameOrMobile) {
        logger.info("请求认证的用户名:" + usernameOrMobile);

        //1.通过请求的用户名去数据库中查询信息
        SysUser sysUser = sysUserService.findByUserName(usernameOrMobile);

        return sysUser;
    }
}
