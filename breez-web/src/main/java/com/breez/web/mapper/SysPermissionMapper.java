package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.breez.web.entities.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 继承BaseMapper<T>接口，他提供了很多对T表的操作方法
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectPermissionByUserId(@Param("userId") Long userId);

    List<SysPermission> findByRoleId(@Param("roleId") Long roleId);
}
