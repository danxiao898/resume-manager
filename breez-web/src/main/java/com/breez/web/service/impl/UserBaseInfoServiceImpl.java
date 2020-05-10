package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.UserBaseInfo;
import com.breez.web.mapper.UserBaseInfoMapper;
import com.breez.web.service.UserBaseInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserBaseInfoServiceImpl extends ServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements UserBaseInfoService {
    @Override
    public UserBaseInfo getByUserId(Long userId) {

        if(userId == null) {
            return new UserBaseInfo();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("user_id", userId);

        List<UserBaseInfo> userBaseInfos = baseMapper.selectByMap(map);

        if (userBaseInfos != null && userBaseInfos.size() > 0) {
            return userBaseInfos.get(0);
        }

        return new UserBaseInfo();
    }
}
