package com.breez.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.base.result.BreezResult;
import com.breez.web.entities.*;
import com.breez.web.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 职位控制层代码
 */
@Controller
@RequestMapping("/position")
public class PositionController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String HTML_PREFIX = "system/position/";

    @Autowired
    PositionService positionService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    PositionTypeService positionTypeService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    PositionApplyStatusService positionApplyStatusService;

    @Autowired
    PositionCollectService positionCollectService;


    @Autowired
    JavaMailService javaMailService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    QuestionRecordService questionRecordService;


    @PreAuthorize("hasAuthority('sys:position:list')")
    @GetMapping(value = {"/", ""})
    public String list(Model model) {

        //把部门列表传给前端，前端拿到后做筛选
        List<Department> departments = departmentService.list();
        model.addAttribute("departments", departments);

        //把工作地点传给前端，前端拿到后做筛选
        List<String> locations = positionService.getAllLocation();
        model.addAttribute("locations", locations);


        return HTML_PREFIX + "position-list";
    }


    /**
     * 分页查询
     * @param page 分页对象 size，current
     * @param position 查询条件
     * @return
     */
    @PreAuthorize("hasAuthority('sys:position:list')")
    @PostMapping("/page")
    @ResponseBody
    public BreezResult page(Page<Position> page, Position position, Long departmentId) {

        System.out.println("departmentId:" + departmentId);
        if(departmentId != null) {
            Department department = new Department();
            department.setId(departmentId);
            position.setDepartment(department);
        }
        IPage<Position> positionIPage = positionService.selectPage(page, position);

        return BreezResult.ok(positionIPage);
    }

    @PostMapping("/customPage")
    @ResponseBody
    public BreezResult customPage(Page<Position> page, Position position, Long departmentId) {
        if(departmentId != null) {
            Department department = new Department();
            department.setId(departmentId);
            position.setDepartment(department);
        }
        IPage<Position> positionIPage = positionService.selectPage(page, position);

        return BreezResult.ok(positionIPage);
    }

    @PreAuthorize("hasAnyAuthority('sys:position:edit','sys:position:add')")
    @GetMapping(value = {"/form","/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //1.查询职位信息
        Position position = positionService.findById(id);
        model.addAttribute("position", position);

        //把部门列表传给前端
        List<Department> departments = departmentService.list();
        model.addAttribute("departments", departments);

        //把类别列表传给前端
        List<PositionType> positionTypes = positionTypeService.list();
        model.addAttribute("positionTypes", positionTypes);

        return HTML_PREFIX + "position-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:position:edit','sys:position:add')")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "")
    public String saveOrUpdate(Position position) {
        System.out.println(position);
        //1.保存用户表
        positionService.saveOrUpdate(position);
        return "redirect:/position";
    }

    @PreAuthorize("hasAuthority('sys:position:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public BreezResult deleteById(@PathVariable Long id) {
        //删除
        positionService.removeById(id);
        return BreezResult.ok();
    }

    @GetMapping("/apply")
    public String applyPage(Model model) {

        //把部门列表传给前端，前端拿到后做筛选
        List<Department> departments = departmentService.list();
        model.addAttribute("departments", departments);

        //把工作地点传给前端，前端拿到后做筛选
        List<String> locations = positionService.getAllLocation();
        model.addAttribute("locations", locations);
        return HTML_PREFIX + "apply";
    }

    @GetMapping("/apply/info/{id}")
    public String applyInfo(@PathVariable("id") Long id, Model model) {
        //1.查询职位信息
        Position position = positionService.findById(id);
        model.addAttribute("position", position);

        return HTML_PREFIX + "apply-info";
    }

    /**
     * 职位申请
     * @param id
     * @return
     */
    @PostMapping("/apply/{id}")
    @ResponseBody
    public BreezResult createApply(@PathVariable("id") Long id) {

        //根据用户id获取简历信息
        Resume resume = resumeService.getResumeById(sysUserService.getCurrentUser().getId());

        //判断简历信息是否合法
        if(resume.getUserBaseInfo() == null || resume.getUserBaseInfo().getName() == null) {
            return BreezResult.build(0, "请完善简历的基本信息后再投递");
        }

        if(resume.getUserEducationExperiences() == null || resume.getUserEducationExperiences().size() == 0) {
            return BreezResult.build(0, "请完善简历的教育经历后再投递");
        }

        if(resume.getUserProfessionalSkills() == null || resume.getUserProfessionalSkills().size() == 0) {
            return BreezResult.build(0, "请完善简历的专业技能后再投递");
        }

        if(resume.getUserProjectExperience() == null || resume.getUserProjectExperience().size() == 0) {
            return BreezResult.build(0, "请完善简历的项目经历后再投递");
        }

        if(resume.getUserWorkExperience() == null || resume.getUserWorkExperience().size() == 0) {
            return BreezResult.build(0, "请完善简历的工作经历后再投递");
        }

        //根据用户id获取答题记录
        List<QuestionRecord> questionRecords = questionRecordService.selectByUserId(sysUserService.getCurrentUser().getId());
        if (questionRecords == null || questionRecords.size() == 0) {
            return BreezResult.build(0, "请答题后再投递");
        }

        boolean applyPosition = positionApplyStatusService.applyPosition(id, sysUserService.getCurrentUser().getId());

        if (applyPosition == false) {
            return BreezResult.build(0, "您已经申请过该职位了，无须重复申请");
        }

        return BreezResult.ok();
    }

    /**
     * 查看自己申请的职位
     * @return
     */
    @GetMapping("/myApply")
    public String myApply(Model model) {

        List<PositionApplyStatus> positionApplyStatuses = positionApplyStatusService.getMyApply();
//        System.out.println(positionApplyStatuses);

        model.addAttribute("positionApplyStatuses", positionApplyStatuses);

        return HTML_PREFIX + "my-apply";
    }

    @GetMapping("/myCollect")
    public String myCollect(Model model) {

        List<PositionCollect> positionCollects = positionCollectService.getMyCollect();

        model.addAttribute("positionCollects", positionCollects);

        return HTML_PREFIX + "my-collect";
    }

    /**
     * 职位收藏
     */
    @PostMapping("/collect/{id}")
    @ResponseBody
    public BreezResult createCollect(@PathVariable("id") Long id) {
        positionCollectService.collectPosition(id, sysUserService.getCurrentUser().getId());

        return BreezResult.ok();
    }

    /**
     * 删除收藏的职位
     */
    @DeleteMapping("/collect/{id}")
    @ResponseBody
    public BreezResult deleteCollect(@PathVariable("id") Long id) {
        positionCollectService.removeById(id);

        return BreezResult.ok();
    }

    @PreAuthorize("hasAuthority('sys:position:apply:list')")
    @PostMapping("/status/page")
    @ResponseBody
    public BreezResult getPositionStatus(Page<PositionApplyStatus> page, Integer wayType , PositionApplyStatus positionApplyStatus) {
        IPage<PositionApplyStatus> applyStatusIPage = positionApplyStatusService.selectPage(page, wayType, positionApplyStatus);

        return BreezResult.ok(applyStatusIPage);
    }

    @PreAuthorize("hasAuthority('sys:position:apply')")
    @GetMapping("/status")
    public String toStatusPage() {
        return HTML_PREFIX + "position-status-list";
    }

    @PreAuthorize("hasAnyAuthority('sys:position:apply:edit')")
    @GetMapping(value = "/status/form/{id}")
    public String applyStatusForm(@PathVariable Long id, Model model) {
        //1.查询职位状态信息
        PositionApplyStatus positionApplyStatus = positionApplyStatusService.findById(id);
        model.addAttribute("positionApplyStatus", positionApplyStatus);

        return HTML_PREFIX + "position-status-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:position:apply:edit')")
    @PutMapping("/status")
    public String updateStatus(PositionApplyStatus positionApplyStatus, String mailMessage, String email) {

        //判断是否需要发送邮件
        if(mailMessage != null && mailMessage.trim() != "") {
            logger.info("将发送邮件通知求职者:");
            logger.info("求职者邮箱:" + email);
            logger.info("邮件内容:" + mailMessage);

            javaMailService.sendSimpleMail("【求职状态进度更新】", mailMessage , email);
        }


        //更新状态
        positionApplyStatusService.updateById(positionApplyStatus);
        logger.info("职位申请状态更新为" + positionApplyStatus.getStatus());

        return "redirect:/position/status";
    }

    @PreAuthorize("hasAuthority('sys:position:apply:delete')")
    @DeleteMapping("/status/{id}")
    @ResponseBody
    public BreezResult deleteApplyById(@PathVariable Long id) {
        //删除
        positionApplyStatusService.removeById(id);
        return BreezResult.ok();
    }

}
