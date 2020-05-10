package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.Department;
import com.breez.web.entities.PositionType;

public interface PositionTypeService extends IService<PositionType> {
    IPage<PositionType> selectPage(Page<PositionType> page, PositionType positionType);
}
