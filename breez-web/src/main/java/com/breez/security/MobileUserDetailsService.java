package com.breez.security;

import com.breez.web.entities.SysUser;
import com.breez.web.service.SysPermissionService;
import com.breez.web.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过手机号获取用户信息和权限资源
 */
@Component("mobileUserDetailsService")
public class MobileUserDetailsService extends AbstractUserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysPermissionService sysPermissionService;

    @Override
    public SysUser findSysUser(String usernameOrMobile) {
        logger.info("请求的手机号是:" + usernameOrMobile);

        //1.通过请求的用户名去数据库中查询信息
        SysUser sysUser = sysUserService.findByMobile(usernameOrMobile);

        return sysUser;
    }
}
