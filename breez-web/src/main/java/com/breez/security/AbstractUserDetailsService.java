package com.breez.security;

import com.breez.web.entities.SysPermission;
import com.breez.web.entities.SysUser;
import com.breez.web.service.SysPermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserDetailsService implements UserDetailsService {


    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 这个方法交给子类实现
     * @param usernameOrMobile 用户名或手机号
     * @return
     */
    public abstract SysUser findSysUser(String usernameOrMobile);

    @Override
    public UserDetails loadUserByUsername(String usernameOrMobile) throws UsernameNotFoundException {

        //1.通过请求的用户名去数据库中查询信息
        SysUser sysUser = findSysUser(usernameOrMobile);

        //通过用户id获取权限信息
        findSysPermission(sysUser);

        return sysUser;

    }

    private void findSysPermission(SysUser sysUser) {
        if(sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        //2. 查询该用户有哪些权限
        List<SysPermission> sysPermissions = sysPermissionService.findByUserId(sysUser.getId());

        if(CollectionUtils.isEmpty(sysPermissions)) {
            return;
        }

        //在左侧菜单动态渲染时会用，目前把他先传入
        sysUser.setPermissions(sysPermissions);

        //3. 封装用户信息或权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(SysPermission sp : sysPermissions) {
            //权限标识
            authorities.add(new SimpleGrantedAuthority(sp.getCode()));
        }
        sysUser.setAuthorities(authorities);
    }
}
