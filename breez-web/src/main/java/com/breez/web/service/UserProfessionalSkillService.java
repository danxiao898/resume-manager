package com.breez.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.UserProfessionalSkill;

import java.io.Serializable;

public interface UserProfessionalSkillService extends IService<UserProfessionalSkill> {

    UserProfessionalSkill getByUserId(Long userId);
}
