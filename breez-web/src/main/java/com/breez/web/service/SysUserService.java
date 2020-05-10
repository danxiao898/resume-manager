package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.SysUser;

/**
 * IService<T> 提供了对T表操作的很多抽象方法，比如批量操作
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名查找用户信息
     * @param username 用户名
     * @return
     */
    SysUser findByUserName(String username);

    /**
     * 通过手机号查找用户信息
     * @param mobile 手机号
     * @return
     */
    SysUser findByMobile(String mobile);

    /**
     * 通过邮箱查询用户信息
     * @param email
     * @return
     */
    SysUser findByEmail(String email);

    IPage<SysUser> selectPage(Page<SysUser> page, SysUser sysUser);

    /**
     * 1.通过用户id查询用户信息
     * 2.通过用户id查询所拥有的角色
     * @param id
     * @return
     */
    SysUser findById(Long id);

    /**
     * 通过用户id将is_enable设置为0
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 获取当前登陆用户
     * @return
     */
    SysUser getCurrentUser();
}
