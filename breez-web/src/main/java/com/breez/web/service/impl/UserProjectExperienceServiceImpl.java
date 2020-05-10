package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.UserProfessionalSkill;
import com.breez.web.entities.UserProjectExperience;
import com.breez.web.mapper.UserProjectExperienceMapper;
import com.breez.web.service.UserProjectExperienceService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserProjectExperienceServiceImpl extends ServiceImpl<UserProjectExperienceMapper, UserProjectExperience> implements UserProjectExperienceService {

    @Override
    public UserProjectExperience getById(Serializable id) {

        if (id == null) {
            return new UserProjectExperience();
        }

        return super.getById(id);
    }
}
