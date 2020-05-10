package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.UserEducationExperience;
import com.breez.web.mapper.UserEducationExperienceMapper;
import com.breez.web.service.UserEducationExperienceService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserEducationExperienceServiceImpl extends ServiceImpl<UserEducationExperienceMapper, UserEducationExperience> implements UserEducationExperienceService {

    @Override
    public UserEducationExperience getById(Serializable id) {

        if (id == null) {
            return new UserEducationExperience();
        }

        return super.getById(id);
    }
}
