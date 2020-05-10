package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.UserWorkExperience;
import com.breez.web.mapper.UserWorkExperienceMapper;
import com.breez.web.service.UserWorkExperienceService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserWorkExperienceServiceImpl extends ServiceImpl<UserWorkExperienceMapper, UserWorkExperience> implements UserWorkExperienceService {

    @Override
    public UserWorkExperience getById(Serializable id) {

        if (id == null) {
            return new UserWorkExperience();
        }

        return super.getById(id);
    }
}
