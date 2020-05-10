package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Department;
import com.breez.web.entities.Position;
import com.breez.web.entities.PositionType;
import com.breez.web.mapper.PositionMapper;
import com.breez.web.service.PositionService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {
    @Override
    public IPage<Position> selectPage(Page<Position> page, Position position) {
        return baseMapper.selectPage(page, position);
    }


    @Override
    public Position findById(Long id) {
        /**
         * 不会返回NULL，id为空的时候new出一个对象，防止前端报错
         */
        if(id == null) {
            Position position = new Position();
            position.setPositionType(new PositionType());
            position.setDepartment(new Department());

            return position;
        }
        return baseMapper.findById(id);
    }

    @Override
    public List<String> getAllLocation() {
        return baseMapper.getAllLocation();
    }

    @Override
    public boolean applyPosition(Long positionId, Long userId) {
        return baseMapper.applyPosition(positionId, userId);
    }

}
