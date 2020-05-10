package com.breez.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.PositionType;
import com.breez.web.service.PositionTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 部门控制层代码
 */
@Controller
@RequestMapping("/positionType")
public class PositionTypeController {

    private static final String HTML_PREFIX = "system/position/";

    @Autowired
    PositionTypeService positionTypeService;

    @PreAuthorize("hasAuthority('sys:position:type:list')")
    @GetMapping(value = {"/", ""})
    public String list() {
        return HTML_PREFIX + "position-type-list";
    }


    /**
     * 分页查询
     * @param page 分页对象 size，current
     * @param positionType 查询条件 name
     * @return
     */
    @PreAuthorize("hasAuthority('sys:position:type:list')")
    @PostMapping("/page")
    @ResponseBody
    public BreezResult page(Page<PositionType> page, PositionType positionType) {

        IPage<PositionType> positionTypeIPage = positionTypeService.selectPage(page, positionType);
        return BreezResult.ok(positionTypeIPage);
    }

    @PreAuthorize("hasAnyAuthority('sys:position:type:edit','sys:position:type:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //1.查询用户信息，包含了用户所拥有的角色
        PositionType positionType = positionTypeService.getById(id);
        model.addAttribute("positionType", positionType);

        return HTML_PREFIX + "position-type-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:position:type:edit','sys:position:type:add')")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "")
    public String saveOrUpdate(PositionType positionType) {
        System.out.println(positionType);
        //1.保存用户表
        positionTypeService.saveOrUpdate(positionType);
        return "redirect:/positionType";
    }

    @PreAuthorize("hasAuthority('sys:position:type:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        //删除
        positionTypeService.removeById(id);
        return BreezResult.ok();
    }
}
