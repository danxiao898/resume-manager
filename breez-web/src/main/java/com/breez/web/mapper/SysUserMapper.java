package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 继承BaseMapper<T>接口，他提供了很多对T表的操作方法
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    IPage<SysUser> selectPage(Page<SysUser> page, @Param("u") SysUser sysUser);

    /**
     * 通过用户id删除用户角色关系表中的所有记录
     * @param userId
     * @return
     */
    boolean deleteUserRoleByUserId(@Param("userId") Long userId);

    /**
     * 保存用户角色关系表数据
     * @param userId
     * @param roleIds
     * @return
     */
    boolean saveUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
