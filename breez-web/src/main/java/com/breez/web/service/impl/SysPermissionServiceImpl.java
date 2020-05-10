package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.SysPermission;
import com.breez.web.mapper.SysPermissionMapper;
import com.breez.web.service.SysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService{
    @Override
    public List<SysPermission> findByUserId(Long userId) {
        if(userId == null) {
            return null;
        }
        List<SysPermission> sysPermissions = baseMapper.selectPermissionByUserId(userId);
        //如果没有权限，则将集合中的数据null移除
        sysPermissions.remove(null);

        return sysPermissions;
    }



    @Transactional
    @Override
    public boolean deleteById(Long id) {

        //1.删除parent_id = id 的权限
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<SysPermission>();
        queryWrapper.eq(SysPermission::getParentId,id);
        baseMapper.delete(queryWrapper);

        //2.删除当前id的权限
        baseMapper.deleteById(id);
        return false;
    }
}
