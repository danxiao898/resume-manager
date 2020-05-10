package com.breez.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.SysRole;
import com.breez.web.entities.SysUser;
import com.breez.web.service.JavaMailService;
import com.breez.web.service.SysRoleService;
import com.breez.web.service.SysUserService;
import com.breez.web.utils.UserUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String HTML_PREFIX = "system/user/";

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "user-list";
    }

    /**
     * 分页查询用户列表
     * @param page 分页对象 size，current
     * @param sysUser 查询条件 username，mobile
     * @return
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @PostMapping("/page")
    @ResponseBody
    public BreezResult page(Page<SysUser> page, SysUser sysUser) {
        return BreezResult.ok(sysUserService.selectPage(page, sysUser));
    }

    @PreAuthorize("hasAnyAuthority('sys:user:edit','sys:user:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //1.查询用户信息，包含了用户所拥有的角色
        SysUser user = sysUserService.findById(id);
        model.addAttribute("user", user);
        //2.查询所有角色
        List<SysRole> roleList = sysRoleService.list();
        model.addAttribute("roleList", roleList);

        return HTML_PREFIX + "user-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:user:edit','sys:user:add')")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "")
    public String saveOrUpdate(SysUser sysUser) {
        //1.保存用户表
        sysUserService.saveOrUpdate(sysUser);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        //假删除
        sysUserService.deleteById(id);
        return BreezResult.ok();
    }

    @GetMapping("/recoverPassword")
    public String recoverPassword() {
        return "system/user/recover-password";
    }


    @PostMapping("/recoverPassword")
    public String changePwd(String newPwd2, String newPwd1, Map<String,Object> map) {
        if(newPwd1 != null && newPwd1.equals(newPwd2)) {
            String psw = passwordEncoder.encode(newPwd1);

            UserDetails user = UserUtils.getUser();

            if (user != null) {
                SysUser sysUser = sysUserService.findByUserName(user.getUsername());
                sysUser.setPassword(psw);
                sysUserService.updateById(sysUser);

                return "redirect:/user/logout";
            } else {
                map.put("message","修改失败,原因是获取用户信息失败");
            }
        } else {
            map.put("message","修改失败,原因是两次输入的不一致");
        }

        return  "/system/user/recover-password";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/forget")
    public String forget() {
        return "forget";
    }

    private static String SESSION_KEY = "EMAIL_CODE";

    @PostMapping("/register")
    @ResponseBody
    public BreezResult registerUser(@RequestBody SysUser sysUser, String code, HttpServletRequest request) {

        System.out.println(sysUser);
        System.out.println("code:" + code);

        //校验验证码是否正确
        String valCode = (String) request.getSession().getAttribute(SESSION_KEY);
        if(valCode != null && valCode.equals(code)) {
            System.out.println("校验成功");
        } else {
            System.out.println("校验失败");
            return BreezResult.build(0, "验证码校验失败");
        }

        //校验用户名是否重复
        SysUser user = sysUserService.findByUserName(sysUser.getUsername());
        if(user != null) {
            System.out.println("用户名已注册");
            return BreezResult.build(0, "用户名已注册");
        }

        //校验手机号是否重复
        SysUser user1 = sysUserService.findByMobile(sysUser.getMobile());
        if(user1 != null) {
            System.out.println("手机号已注册");
            return BreezResult.build(0, "手机号已注册");
        }

        //校验邮箱是否重复
        SysUser user2 = sysUserService.findByEmail(sysUser.getEmail());
        if(user2 != null) {
            System.out.println("邮箱已注册");
            return BreezResult.build(0, "邮箱已注册");
        }

        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        //可以保存该用户了
        sysUserService.saveOrUpdate(sysUser);

        return BreezResult.ok();
    }



    @Autowired
    JavaMailService javaMailService;

    @PostMapping("/sendCode")
    @ResponseBody
    public BreezResult sendCode(String email, HttpServletRequest request) {

        System.out.println(email);

        if(StringUtils.isNotEmpty(email)) {
            //1. 生成一个验证码
            String code = RandomStringUtils.randomNumeric(4);
            //2. 将验证码保存在session中
            request.getSession().setAttribute(SESSION_KEY, code);

            //3. 通过邮箱发送
            javaMailService.sendSimpleMail("注册验证码","您的验证码为:" + code + "，请妥善保管" , email);

        }

        return BreezResult.ok();
    }

    @PostMapping("/forget")
    @ResponseBody
    public BreezResult forget(String email, HttpServletRequest request) {

        System.out.println(email);

        if(StringUtils.isNotEmpty(email)) {

            //通过邮箱查找用户
            SysUser sysUser = sysUserService.findByEmail(email);

            if(sysUser == null) {
                return BreezResult.build(0, "该邮箱未注册");
            }

            //找到了，重置密码
            // 生成一个随机6位密码
            String code = RandomStringUtils.randomNumeric(6);
            sysUser.setPassword(passwordEncoder.encode(code));
            javaMailService.sendSimpleMail("找回密码","您的用户名为:" + sysUser.getUsername() + "。您的密码已被重置为:" + code + "。请使用新密码登陆后自行修改密码。", email);

            sysUserService.saveOrUpdate(sysUser);

            return BreezResult.ok();
        }

        return BreezResult.ok();
    }
}
