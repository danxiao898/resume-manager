package com.breez.web.service.impl;

import com.breez.web.entities.*;
import com.breez.web.mapper.*;
import com.breez.web.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService {

    /**
     * 报错正常，因为Mapper接口是自动扫描的，在这里是误报
     */
    @Autowired
    private UserBaseInfoMapper userBaseInfoMapper;

    @Autowired
    private UserEducationExperienceMapper userEducationExperienceMapper;

    @Autowired
    private UserProfessionalSkillMapper userProfessionalSkillMapper;

    @Autowired
    private UserProjectExperienceMapper userProjectExperienceMapper;

    @Autowired
    private UserWorkExperienceMapper userWorkExperienceMapper;

    @Override
    public Resume getResumeById(Long userId) {

        if(userId == null) {
            Resume resume = new Resume();

            resume.setUserProjectExperience(new LinkedList<>());
            resume.setUserWorkExperience(new LinkedList<>());
            resume.setUserEducationExperiences(new LinkedList<>());
            resume.setUserProfessionalSkills(new LinkedList<>());
            resume.setUserBaseInfo(new UserBaseInfo());
            return new Resume();
        }

        Resume resume = new Resume();

        //定义检索条件
        Map<String,Object> map = new HashMap<>();
        map.put("user_id", userId);

        //1.获取基本信息
        List<UserBaseInfo> baseInfos = userBaseInfoMapper.selectByMap(map);
        if(baseInfos != null && baseInfos.size() > 0) {
            resume.setUserBaseInfo(baseInfos.get(0));
        } else {
            resume.setUserBaseInfo(new UserBaseInfo());
        }

        //2.获取技能信息List
        List<UserProfessionalSkill> professionalSkills = userProfessionalSkillMapper.selectByMap(map);
        if(professionalSkills != null && professionalSkills.size() > 0) {
            resume.setUserProfessionalSkills(professionalSkills);
        }

        //3.获取教育经历List
        List<UserEducationExperience> educationExperiences = userEducationExperienceMapper.selectByMap(map);
        if(educationExperiences != null && educationExperiences.size() > 0) {
            resume.setUserEducationExperiences(educationExperiences);
        }

        //4.获取工作经历List
        List<UserWorkExperience> workExperiences = userWorkExperienceMapper.selectByMap(map);
        if(workExperiences != null && workExperiences.size() > 0) {
            resume.setUserWorkExperience(workExperiences);
        }

        //5.获取项目经历List
        List<UserProjectExperience> projectExperiences = userProjectExperienceMapper.selectByMap(map);
        if(projectExperiences != null && projectExperiences.size() > 0) {
            resume.setUserProjectExperience(projectExperiences);
        }

        return resume;
    }
}
