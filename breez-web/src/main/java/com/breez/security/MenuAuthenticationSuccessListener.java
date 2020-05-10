package com.breez.security;

import com.breez.security.authentication.AuthenticationSuccessListener;
import com.breez.web.entities.SysPermission;
import com.breez.web.entities.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class MenuAuthenticationSuccessListener implements AuthenticationSuccessListener {

    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 查询用户所拥有的权限菜单
     * 当认证成功后会触发此实现类的方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 当用户认证通过后，会将认证对象传入
     */
    @Override
    public void successListener(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        logger.info("查询用户所拥有的权限菜单");
        Object principal = authentication.getPrincipal();

        if(principal != null && principal instanceof SysUser) {
            SysUser sysUser = (SysUser) principal;
            loadMenuTree(sysUser);

            logger.info("permissions:" + sysUser.getPermissions());
        }

    }

    /**
     * 只加载菜单，不需要按钮
     * @param sysUser
     */
    public void loadMenuTree(SysUser sysUser) {
        List<SysPermission> permissions = sysUser.getPermissions();

        if(CollectionUtils.isEmpty(permissions)) {
            return;
        }

        List<SysPermission> menuList = new ArrayList<>();
        //1.获取权限集合中所有的菜单，不要按钮
        for(SysPermission p : permissions) {
            if(p.getType().equals(1)) {
                menuList.add(p);
            }
        }

        //获取每菜单的子菜单
        for(SysPermission menu : menuList) {
            //存放当前菜单的所有子菜单
            List<SysPermission> childMenu = new ArrayList<>();
            List<String> childUrl = new ArrayList<>();
            // 获取menu菜单下的所有子菜单
            for(SysPermission p : menuList) {
                //如果p.getParentId = menu.getId()则当前菜单就是外层的子菜单
                if(p.getParentId().equals(menu.getId())) {
                    childMenu.add(p);
                    childUrl.add(p.getUrl());
                }
            }
            //设置子菜单
            menu.setChildren(childMenu);
            menu.setChildrenUrl(childUrl);
        }

        //3. menuList里面每个对象都有一个Children集合 首页---没有元素   系统管理---有3个

        List<SysPermission> result = new ArrayList<>();
        for(SysPermission menu : menuList) {
            //如果父id是0，则是根元素
            if(menu.getParentId().equals(0L)) {
                result.add(menu);
            }
        }

        //最终把获取的根菜单(子菜单集合)重新设置到permission中
        sysUser.setPermissions(result);
    }
}
