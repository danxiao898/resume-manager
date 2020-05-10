package com.breez.web.entities;

import lombok.Data;

import java.util.List;

/**
 * 简历表
 */
@Data
public class Resume {

    /**
     * 基本信息
     */
    private UserBaseInfo userBaseInfo;

    /**
     * 专业技能
     */
    private List<UserProfessionalSkill> userProfessionalSkills;

    /**
     * 教育经历
     */
    private List<UserEducationExperience> userEducationExperiences;

    /**
     * 工作经历
     */
    private List<UserWorkExperience> userWorkExperience;

    /**
     * 项目经历
     */
    private List<UserProjectExperience> userProjectExperience;
}
