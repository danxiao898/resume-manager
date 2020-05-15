package com.breez.web.controller;

import com.breez.base.result.BreezResult;
import com.breez.web.entities.*;
import com.breez.web.service.*;
import com.breez.web.utils.SysUtils;
import com.breez.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserBaseInfoService userBaseInfoService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserProfessionalSkillService userProfessionalSkillService;

    @Autowired
    private UserEducationExperienceService userEducationExperienceService;

    @Autowired
    private UserWorkExperienceService userWorkExperienceService;

    @Autowired
    private UserProjectExperienceService userProjectExperienceService;

    /**
     * 获取当前用户信息
     * @return
     */
    private SysUser getCurrentUser() {
        //获取当前登陆用户
        UserDetails user = UserUtils.getUser();
        //根据用户名查找,主要是拿到id
        SysUser sysUser = sysUserService.findByUserName(user.getUsername());

        return sysUser;
    }

    /**
     * 编辑简历
     * @return
     */
    @GetMapping(value = "/edit")
    public String editResume(Model model) {
        SysUser sysUser = getCurrentUser();

        Resume resume = resumeService.getResumeById(sysUser.getId());

        model.addAttribute("resume", resume);

        return "/system/resume/index-edit";
    }

    /**
     * 根据用户id查看用户基本信息
     * @param model
     * @return
     */
    @GetMapping(value = {"/baseInfo"})
    public String formBaseInfo(Model model) {


        SysUser sysUser = getCurrentUser();

        UserBaseInfo userBaseInfo = userBaseInfoService.getByUserId(sysUser.getId());
        model.addAttribute("userBaseInfo", userBaseInfo);

        return "/system/resume/base-info";
    }

    /**
     * 跳转到技能表单
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = {"/skill/{id}","/skill"})
    public String formSkill(@PathVariable(value = "id", required = false) Long id, Model model) {

        UserProfessionalSkill userProfessionalSkill = userProfessionalSkillService.getById(id);
        model.addAttribute("userProfessionalSkill", userProfessionalSkill);

        return "/system/resume/skill-form";
    }

    /**
     * 跳转到教育经历表单
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = {"/educ/{id}","/educ"})
    public String formEduc(@PathVariable(value = "id", required = false) Long id, Model model) {

        UserEducationExperience userEducationExperience = userEducationExperienceService.getById(id);
        model.addAttribute("userEducationExperience", userEducationExperience);

        return "/system/resume/educ-form";
    }

    /**
     * 跳转到工作经历表单
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = {"/work/{id}","/work"})
    public String formWork(@PathVariable(value = "id", required = false) Long id, Model model) {

        UserWorkExperience userWorkExperience = userWorkExperienceService.getById(id);
        model.addAttribute("userWorkExperience", userWorkExperience);

        return "/system/resume/work-form";
    }

    /**
     * 跳转到项目经历表单
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = {"/project/{id}","/project"})
    public String formProject(@PathVariable(value = "id", required = false) Long id, Model model) {

        UserProjectExperience userProjectExperience = userProjectExperienceService.getById(id);
        model.addAttribute("userProjectExperience", userProjectExperience);

        return "/system/resume/project-form";
    }

    /**
     * 保存或更新基本信息
     * @param userBaseInfo
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "/baseInfo")
    public String saveOrUpdate(@RequestParam("photo") MultipartFile multipartFile, UserBaseInfo userBaseInfo) {

        if(multipartFile.isEmpty()){
            System.out.println("上传失败,未选择文件");
        } else {
            String fileName = multipartFile.getOriginalFilename();
            System.out.println("fileName = " + fileName);

            String path = "";
            try {
                //文件上传的地址
                path = ResourceUtils.getURL("classpath:").getPath()+"static/upload/";
//                String realPath = path.replace('/', '\\').substring(1,path.length());
                if(SysUtils.isWindows()) {
                    //windows系统做特殊处理
                    path = path.replace('/', '\\').substring(1,path.length());
                }

                //用于查看路径是否正确
                System.out.println(path);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            File file = new File(path + fileName);

            try {
                multipartFile.transferTo(file);
                System.out.println("上传成功");
                userBaseInfo.setPhotoSrc(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("上传失败");
            }


            userBaseInfo.setUserId(sysUserService.getCurrentUser().getId());
//            File file = new File(filePath + fileName);
//            System.out.println(file.getName());
        }

        System.out.println(userBaseInfo);
        //1.保存用户表
        userBaseInfoService.saveOrUpdate(userBaseInfo);
        return "redirect:/resume/edit";
    }

    /**
     * 保存或更新技能
     * @param skill
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "/skill")
    public String saveOrUpdate(UserProfessionalSkill skill) {

        SysUser sysUser = getCurrentUser();
        skill.setUserId(sysUser.getId());
        System.out.println(skill);
        //1.保存用户表
        userProfessionalSkillService.saveOrUpdate(skill);
        return "redirect:/resume/edit";
    }

    /**
     * 保存或更新教育经历
     * @param educ
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "/educ")
    public String saveOrUpdate(UserEducationExperience educ) {

        SysUser sysUser = getCurrentUser();
        educ.setUserId(sysUser.getId());
        System.out.println(educ);
        //1.保存用户表
        userEducationExperienceService.saveOrUpdate(educ);
        return "redirect:/resume/edit";
    }

    /**
     * 保存或更新工作经历
     * @param work
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "/work")
    public String saveOrUpdate(UserWorkExperience work) {

        SysUser sysUser = getCurrentUser();
        work.setUserId(sysUser.getId());
        System.out.println(work);
        //1.保存用户表
        userWorkExperienceService.saveOrUpdate(work);
        return "redirect:/resume/edit";
    }

    /**
     * 保存或更新项目经历
     * @param project
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT}, value = "/project")
    public String saveOrUpdate(UserProjectExperience project) {

        SysUser sysUser = getCurrentUser();
        project.setUserId(sysUser.getId());
        System.out.println(project);
        //1.保存用户表
        userProjectExperienceService.saveOrUpdate(project);
        return "redirect:/resume/edit";
    }

    /**
     * 删除技能
     * @param id
     * @return
     */
    @DeleteMapping("/skill/{id}")
    @ResponseBody
    public BreezResult deleteSkillById(@PathVariable Long id) {
        //删除
        userProfessionalSkillService.removeById(id);
        return BreezResult.ok();
    }

    /**
     * 删除教育经历
     * @param id
     * @return
     */
    @DeleteMapping("/educ/{id}")
    @ResponseBody
    public BreezResult deleteEducById(@PathVariable Long id) {
        //删除
        userEducationExperienceService.removeById(id);
        return BreezResult.ok();
    }

    /**
     * 删除工作经历
     * @param id
     * @return
     */
    @DeleteMapping("/work/{id}")
    @ResponseBody
    public BreezResult deleteWorkById(@PathVariable Long id) {
        //删除
        userWorkExperienceService.removeById(id);
        return BreezResult.ok();
    }

    /**
     * 删除项目经历
     * @param id
     * @return
     */
    @DeleteMapping("/project/{id}")
    @ResponseBody
    public BreezResult deleteProjectById(@PathVariable Long id) {
        //删除
        userProjectExperienceService.removeById(id);
        return BreezResult.ok();
    }


    /**
     * 根据用户id获取简历
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = {"/{id}","/",""})
    public String getResume(@PathVariable(value = "id",required = false) Long id, boolean disableToolBar , Model model) {
        if(id == null) {
            id = getCurrentUser().getId();
        }
        Resume resume = resumeService.getResumeById(id);
        System.out.println(resume);

        model.addAttribute("resume", resume);

        if(disableToolBar) {
            model.addAttribute("disableToolBar", true);
        }

        return "/system/resume/index";
    }
}
