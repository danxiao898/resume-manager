package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.Position;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionMapper extends BaseMapper<Position> {

    IPage<Position> selectPage(Page<Position> page, @Param("p") Position position);

    Position findById(@Param("id") Long id);

    List<String> getAllLocation();

    boolean applyPosition(@Param("pid") Long positionId, @Param("uid") Long userId);
}
