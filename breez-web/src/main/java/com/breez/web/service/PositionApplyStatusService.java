package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.PositionApplyStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionApplyStatusService  extends IService<PositionApplyStatus> {
    boolean applyPosition(Long positionId, Long userId);

    List<PositionApplyStatus> getMyApply();

    IPage<PositionApplyStatus> selectPage(Page<PositionApplyStatus> page, Integer wayType, PositionApplyStatus positionApplyStatus);

    PositionApplyStatus findById(Long id);
}
