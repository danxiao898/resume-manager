package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.SysRole;
import com.breez.web.entities.SysUser;
import com.breez.web.mapper.SysRoleMapper;
import com.breez.web.mapper.SysUserMapper;
import com.breez.web.service.SysUserService;
import com.breez.web.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    private final static String PASSWORD_DEFAULT = "$2a$10$rDkPvvAFV8kqwvKJzwlRv.i.q.wz1w1pz0SFsHn/55jNeZFQv/eCm";
    @Override
    public SysUser findByUserName(String username) {

        if(StringUtils.isEmpty(username)) {
            return null;
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        /**
         * baseMapper其实就是SysUserMapper
         */
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public SysUser findByMobile(String mobile) {
        if(StringUtils.isEmpty(mobile)) {
            return null;
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);

        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public SysUser findByEmail(String email) {
        if(StringUtils.isEmpty(email)) {
            return null;
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);

        SysUser sysUser = baseMapper.selectOne(queryWrapper);

        return sysUser;
    }

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> page, SysUser sysUser) {
        return baseMapper.selectPage(page, sysUser);
    }


    @Autowired
    private SysRoleMapper sysRoleMapper;
    /**
     * 1.通过用户id查询用户信息
     * 2.通过用户id查询所拥有的角色
     * @param id
     * @return
     */
    @Override
    public SysUser findById(Long id) {
        if(id == null) {
            return new SysUser();
        }
        //1 通过用户id查询用户信息
        SysUser sysUser = baseMapper.selectById(id);

        //2 通过用户id查询所拥有的角色
        List<SysRole> roleList = sysRoleMapper.findByUserId(id);

        sysUser.setRoleList(roleList);

        return sysUser;
    }


    /**
     * 1.更新到sys_user
     * 2.更新到sys_user_role(先删除再新增)
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public boolean saveOrUpdate(SysUser entity) {
        if(entity != null && entity.getId() == null) {
            if(entity.getPassword() == null || entity.getPassword().trim().equals("")) {
                //如果是新增，且没填密码,设置默认密码1234
                entity.setPassword(PASSWORD_DEFAULT);
            }

        }
        entity.setUpdateDate(new Date());
        //1.更新或保存操作
        boolean flag = super.saveOrUpdate(entity);
        if(flag) {
            //2.先删除用户角色表中的数据
            baseMapper.deleteUserRoleByUserId(entity.getId());

            if(CollectionUtils.isNotEmpty(entity.getRoleIds())) {
                //3.再新增用户角色信息
                baseMapper.saveUserRole(entity.getId(), entity.getRoleIds());
            }

        }


        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        //1.查询用户信息
        SysUser sysUser = baseMapper.selectById(id);
        //2.再更新用户信息
        sysUser.setEnabled(false);
        sysUser.setUpdateDate(new Date());
        baseMapper.updateById(sysUser);

        return true;
    }

    public SysUser getCurrentUser() {
        //获取当前登陆用户
        UserDetails user = UserUtils.getUser();
        //根据用户名查找,主要是拿到id
        SysUser sysUser = findByUserName(user.getUsername());

        return sysUser;
    }
}
