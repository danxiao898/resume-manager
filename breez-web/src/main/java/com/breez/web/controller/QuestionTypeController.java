package com.breez.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.QuestionType;
import com.breez.web.entities.SysRole;
import com.breez.web.entities.SysUser;
import com.breez.web.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目类型控制层代码
 */
@Controller
@RequestMapping("/questionType")
public class QuestionTypeController {

    private static final String HTML_PREFIX = "system/question/";

    @Autowired
    QuestionTypeService questionTypeService;

    @PreAuthorize("hasAuthority('sys:question:type:list')")
    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "question-type-list";
    }


    /**
     * 分页查询
     * @param page 分页对象 size，current
     * @param questionType 查询条件 name
     * @return
     */
    @PreAuthorize("hasAuthority('sys:question:type:list')")
    @PostMapping("/page")
    @ResponseBody
    public BreezResult page(Page<QuestionType> page, QuestionType questionType) {

        IPage<QuestionType> questionTypeIPage = questionTypeService.selectPage(page, questionType);
        return BreezResult.ok(questionTypeIPage);
    }

    @PreAuthorize("hasAnyAuthority('sys:question:type:edit','sys:question:type:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //1.查询用户信息，包含了用户所拥有的角色
        QuestionType questionType = questionTypeService.getById(id);
        model.addAttribute("questionType", questionType);

        return HTML_PREFIX + "question-type-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:question:type:edit','sys:question:type:add')")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "")
    public String saveOrUpdate(QuestionType questionType) {
        System.out.println(questionType);
        //1.保存用户表
        questionTypeService.saveOrUpdate(questionType);
        return "redirect:/questionType";
    }

    @PreAuthorize("hasAuthority('sys:question:type:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        //删除
        questionTypeService.removeById(id);
        return BreezResult.ok();
    }
}
