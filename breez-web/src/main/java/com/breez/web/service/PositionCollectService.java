package com.breez.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.PositionApplyStatus;
import com.breez.web.entities.PositionCollect;

import java.util.List;

public interface PositionCollectService extends IService<PositionCollect> {

    /**
     * 收藏职位
     * @param positionId
     * @param userId
     * @return
     */
    boolean collectPosition(Long positionId, Long userId);

    /**
     * 获取收藏的所有职位
     * @return
     */
    List<PositionCollect> getMyCollect();
}
