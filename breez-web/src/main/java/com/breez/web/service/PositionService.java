package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.Position;

import java.io.Serializable;
import java.util.List;

public interface PositionService extends IService<Position> {
    IPage<Position> selectPage(Page<Position> page, Position position);

    Position findById(Long id);

    List<String> getAllLocation();

    boolean applyPosition(Long positionId, Long userId);

}
