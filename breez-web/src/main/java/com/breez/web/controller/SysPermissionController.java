package com.breez.web.controller;

import com.breez.base.result.BreezResult;
import com.breez.web.entities.SysPermission;
import com.breez.web.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    private static final String HTML_PREFIX = "system/permission/";

    @PreAuthorize("hasAuthority('sys:permission')")
    @GetMapping(value = {"/", ""})
    public String permission() {
        return HTML_PREFIX + "permission-list";
    }

    @PreAuthorize("hasAuthority('sys:permission:list')")
    @PostMapping("list")
    @ResponseBody
    public BreezResult list() {
        //Mybatis-plus已经提供的
        List<SysPermission> list = sysPermissionService.list();
        return BreezResult.ok(list);
    }

    /**
     * 跳转至新增或修改页面
     * /form 新增
     * /form/{id} 修改
     * @PathVariable(required = false) 设置成false，id可传可不传
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:permission:edit', 'sys:permission:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {

        //1.通过权限ID查询对应的权限信息
        SysPermission permission = sysPermissionService.getById(id);

        //绑定后页面可获取
        model.addAttribute("permission", permission == null ? new SysPermission() : permission);

        return HTML_PREFIX + "permission-form";
    }

    /**
     * 这个是提交新增或修改的数据
     * @param permission
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:permission:edit','sys:permission:add')")
    @RequestMapping(value = "",method = {RequestMethod.PUT,RequestMethod.POST})//permission
    public String saveOrUpdate(SysPermission permission) {
        sysPermissionService.saveOrUpdate(permission);

        return "redirect:/permission";
    }


    @PreAuthorize("hasAuthority('sys:permission:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        sysPermissionService.deleteById(id);

        return BreezResult.ok();
    }
}
