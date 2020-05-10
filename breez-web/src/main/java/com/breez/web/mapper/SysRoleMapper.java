package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 继承BaseMapper<T>接口，他提供了很多对T表的操作方法
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 只要将limit的sql语句写在对应的id = "selectPage"里面（SysRoleMapper.xml），不需要自己去分页，mybatis-plus会自动分页
     * 但是，它实现分页，你需要第一个参数传入Page，而其他参数你需要使用@Param（xx）取别名
     * 最终查询的数据会被封装到IPage实现里面
     * @param page
     * @param sysRole
     * @return
     */
    IPage<SysRole> selectPage(Page<SysRole> page,@Param("p") SysRole sysRole);

    /**
     * 通过角色id删除角色权限关系表中的所有记录
     * @param roleId
     * @return
     */
    boolean deleteRolePermissionByRoleId(@Param("roleId") Long roleId);

    /**
     * 保存角色与权限关系表数据
     * @param roleId
     * @param perIds
     * @return
     */
    boolean saveRolePermission(@Param("roleId") Long roleId, @Param("perIds") List<Long> perIds);

    /**
     * 通过用户id查询所拥有的角色
     * @param userId 用户id
     * @return 查询到的角色信息集合
     */
    List<SysRole> findByUserId(@Param("userId") Long userId);
}
