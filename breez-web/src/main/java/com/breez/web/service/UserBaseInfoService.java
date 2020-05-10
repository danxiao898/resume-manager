package com.breez.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.UserBaseInfo;

public interface UserBaseInfoService extends IService<UserBaseInfo> {

    UserBaseInfo getByUserId(Long userId);
}
