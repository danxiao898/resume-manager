package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Department;
import com.breez.web.entities.PositionType;
import com.breez.web.mapper.DepartmentMapper;
import com.breez.web.mapper.PositionTypeMapper;
import com.breez.web.service.DepartmentService;
import com.breez.web.service.PositionTypeService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PositionTypeServiceImpl extends ServiceImpl<PositionTypeMapper, PositionType> implements PositionTypeService {

    @Override
    public IPage<PositionType> selectPage(Page<PositionType> page, PositionType positionType) {
        return baseMapper.selectPage(page, positionType);
    }

    @Override
    public PositionType getById(Serializable id) {
        if(id == null) {
            return new PositionType();
        }
        return super.getById(id);
    }
}
