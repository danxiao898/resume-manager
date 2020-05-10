package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.SysPermission;
import com.breez.web.entities.SysRole;
import com.breez.web.mapper.SysPermissionMapper;
import com.breez.web.mapper.SysRoleMapper;
import com.breez.web.service.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public IPage<SysRole> selectPage(Page<SysRole> page, SysRole sysRole) {
        return baseMapper.selectPage(page,sysRole);
    }

    @Autowired
    private SysPermissionMapper sysPermissionMapper;//报错正常idea不识别

    @Override
    public SysRole findById(Long id) {
        if(id == null) {
            return new SysRole();
        }
        //1.通过角色id查询角色信息
        SysRole sysRole = baseMapper.selectById(id);
        //2.通过角色id查询权限信息
        List<SysPermission> permissions = sysPermissionMapper.findByRoleId(id);
        //3.将权限信息set到sysRole中
        sysRole.setPerList(permissions);
        return sysRole;
    }



    @Transactional
    @Override
    public boolean saveOrUpdate(SysRole entity) {
        entity.setUpdateDate(new Date());
        //1.更新角色表中的数据
        boolean flag = super.saveOrUpdate(entity);

        if(flag) {
            //2.更新角色权限关系表中的数据(删除)
            baseMapper.deleteRolePermissionByRoleId(entity.getId());

            //3.更新角色权限关系表中的数据(新增)
            if(CollectionUtils.isNotEmpty(entity.getPerIds())) {
                baseMapper.saveRolePermission(entity.getId(), entity.getPerIds());
            }
        }

        return flag;
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        //1.通过角色id删除角色
        baseMapper.deleteById(id);
        //2.删除角色关系表数据
        baseMapper.deleteRolePermissionByRoleId(id);

        return true;
    }
}