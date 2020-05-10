package com.breez.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.Department;
import com.breez.web.entities.QuestionType;
import com.breez.web.service.DepartmentService;
import com.breez.web.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 部门控制层代码
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    private static final String HTML_PREFIX = "system/department/";

    @Autowired
    DepartmentService departmentService;

    @PreAuthorize("hasAuthority('sys:department:list')")
    @GetMapping(value = {"/", ""})
    public String list() {
        return HTML_PREFIX + "department-list";
    }


    /**
     * 分页查询
     * @param page 分页对象 size，current
     * @param department 查询条件 name
     * @return
     */
    @PreAuthorize("hasAuthority('sys:department:list')")
    @PostMapping("/page")
    @ResponseBody
    public BreezResult page(Page<Department> page, Department department) {

        IPage<Department> departmentIPage = departmentService.selectPage(page, department);
        return BreezResult.ok(departmentIPage);
    }

    @PreAuthorize("hasAnyAuthority('sys:department:edit','sys:department:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //1.查询用户信息，包含了用户所拥有的角色
        Department department = departmentService.getById(id);
        model.addAttribute("department", department);

        return HTML_PREFIX + "department-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:department:edit','sys:department:add')")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "")
    public String saveOrUpdate(Department department) {
        System.out.println(department);
        //1.保存用户表
        departmentService.saveOrUpdate(department);
        return "redirect:/department";
    }

    @PreAuthorize("hasAuthority('sys:department:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        //删除
        departmentService.removeById(id);
        return BreezResult.ok();
    }
}
