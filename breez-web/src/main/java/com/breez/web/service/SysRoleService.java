package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.SysRole;

/**
 * IService<T> 提供了对T表操作的很多抽象方法，比如批量操作
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 分页查询角色列表
     * @param sysRole
     * @param page
     * @return
     */
    IPage<SysRole> selectPage(Page<SysRole> page, SysRole sysRole);

    /**
     * 通过角色id查询该角色所拥有的角色和权限信息
     * @param id
     * @return
     */
    SysRole findById(Long id);

    /**
     * 1.通过id删除角色信息表数据
     * 2.通过id删除角色权限关系表数据
     * @param id 角色id
     * @return
     */
    boolean deleteById(Long id);
}
