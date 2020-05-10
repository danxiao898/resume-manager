package com.mengxuegu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.breez.web.service.impl.JavaMailServiceImpl;
import com.breez.web.entities.SysPermission;
import com.breez.web.entities.SysRole;
import com.breez.web.entities.SysUser;
import com.breez.web.service.SysPermissionService;
import com.breez.web.service.SysRoleService;
import com.breez.web.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWebApplication {

    @Autowired
    SysUserService sysUserService;

    @Test
    public void testSysUser() {
//        List<SysUser> list = sysUserService.list();
//        System.out.println("list:" + list);

        SysUser sysUser = sysUserService.findByUserName("admin");
        System.out.println(sysUser);
    }

    @Autowired
    SysRoleService sysRoleService;

    @Test
    public void testSysRole() {
//        System.out.println("list:" + sysRoleService.list());

        QueryWrapper<SysRole> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", 9);
        queryWrapper.like("name", "超级");
        SysRole sysRole = sysRoleService.getOne(queryWrapper);
        System.out.println(sysRole);
    }

    @Autowired
    SysPermissionService sysPermissionService;

    @Test
    public void testSysPermission() {
//        SysPermission sysPermission = sysPermissionService.getById(11);
//        System.out.println(sysPermission);

        List<SysPermission> sysPermissions = sysPermissionService.findByUserId(9L);
        System.out.println(sysPermissions);
        System.out.println(sysPermissions.size());
    }

    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Test
    public void testSimpleMail() {
        //封装简单邮件内容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("这是邮件主题");
        message.setText("这是邮件内容");

        //发件人
        message.setFrom("zhangzhiquan@breez.com.cn");
        //收件人
        message.setTo("1437801548@qq.com");
        javaMailSender.send(message);
    }

    @Autowired
    JavaMailServiceImpl javaMailServiceImpl;
    @Test
    public void javaMailUtilsTest() {


        javaMailServiceImpl.sendSimpleMail("测试标题1","测试内容1","1437801548@qq.com");
    }

}
