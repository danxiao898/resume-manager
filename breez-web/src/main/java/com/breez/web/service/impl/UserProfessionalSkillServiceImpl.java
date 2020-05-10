package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.UserBaseInfo;
import com.breez.web.entities.UserProfessionalSkill;
import com.breez.web.mapper.UserProfessionalSkillMapper;
import com.breez.web.service.UserProfessionalSkillService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserProfessionalSkillServiceImpl extends ServiceImpl<UserProfessionalSkillMapper, UserProfessionalSkill> implements UserProfessionalSkillService {
    @Override
    public UserProfessionalSkill getByUserId(Long userId) {

        if(userId == null) {
            return new UserProfessionalSkill();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("user_id", userId);

        List<UserProfessionalSkill> userBaseInfos = baseMapper.selectByMap(map);

        if (userBaseInfos != null && userBaseInfos.size() > 0) {
            return userBaseInfos.get(0);
        }

        return new UserProfessionalSkill();
    }

    @Override
    public UserProfessionalSkill getById(Serializable id) {

        if (id == null) {
            return new UserProfessionalSkill();
        }

        return super.getById(id);
    }
}
