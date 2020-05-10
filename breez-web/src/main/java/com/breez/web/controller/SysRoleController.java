package com.breez.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.SysRole;
import com.breez.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/role")
public class SysRoleController {

    private static final String HTML_PREFIX = "system/role/";

    @PreAuthorize("hasAuthority('sys:role')")
    @GetMapping(value = {"/", ""})
    public String role() {
        return HTML_PREFIX + "role-list";
    }


    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/page") //  /role/page
    @ResponseBody
    @PreAuthorize("hasAuthority('sys:role:list')")
    public BreezResult page(Page<SysRole> page, SysRole sysRole) {
        return BreezResult.ok(sysRoleService.selectPage(page,sysRole));
    }

    /**
     * 跳转新增或修改页面
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:role:add','sys:role:edit')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        // 通过角色id查询对应的角色信息和权限信息
        SysRole role = sysRoleService.findById(id);
        model.addAttribute("role",role);

        return HTML_PREFIX + "role-form";
    }

    /**
     * 提交新增或修改的数据
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:role:add','sys:role:edit')")
    @RequestMapping(value = "",method = {RequestMethod.POST,RequestMethod.PUT})
    public String saveOrUpdate(SysRole sysRole) {
        System.out.println(sysRole);

        sysRoleService.saveOrUpdate(sysRole);
        return "redirect:/role";
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/{id}") // /role/id
    @ResponseBody
    public BreezResult deleteById(@PathVariable("id") Long id) {
        sysRoleService.deleteById(id);

        return BreezResult.ok();
    }
}
