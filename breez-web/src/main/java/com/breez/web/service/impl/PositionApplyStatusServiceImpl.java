package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Position;
import com.breez.web.entities.PositionApplyStatus;
import com.breez.web.mapper.PositionApplyStatusMapper;
import com.breez.web.service.PositionApplyStatusService;
import com.breez.web.service.PositionService;
import com.breez.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionApplyStatusServiceImpl extends ServiceImpl<PositionApplyStatusMapper, PositionApplyStatus> implements PositionApplyStatusService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PositionService positionService;

    public boolean applyPosition(Long positionId, Long userId) {
        Map<String,Object> map = new HashMap<>();

        map.put("position_id", positionId);
        map.put("user_id", userId);
        List<PositionApplyStatus> positionApplyStatuses = baseMapper.selectByMap(map);

        //已经存在该记录了,不做任何操作
        if(positionApplyStatuses != null && positionApplyStatuses.size() > 0) {
            return false;
        }

        PositionApplyStatus positionApplyStatus = new PositionApplyStatus();
        positionApplyStatus.setPositionId(positionId);
        positionApplyStatus.setUserId(userId);
        positionApplyStatus.setStatus(0);
        positionApplyStatus.setApplyTime(new Date());
        saveOrUpdate(positionApplyStatus);

        return true;
    }

    @Override
    public List<PositionApplyStatus> getMyApply() {
        Map<String, Object> map = new HashMap<>();

        map.put("user_id", sysUserService.getCurrentUser().getId());

        List<PositionApplyStatus> positionApplyStatuses = baseMapper.selectByMap(map);

        //获取职位详情
        for (PositionApplyStatus positionApplyStatus : positionApplyStatuses) {
            Position position = positionService.findById(positionApplyStatus.getPositionId());
            positionApplyStatus.setPosition(position);
        }

        return positionApplyStatuses;
    }

    @Override
    public IPage<PositionApplyStatus> selectPage(Page<PositionApplyStatus> page, Integer wayType, PositionApplyStatus positionApplyStatus) {
        return baseMapper.selectPage(page, wayType, positionApplyStatus);
    }

    @Override
    public PositionApplyStatus findById(Long id) {
        return baseMapper.findById(id);
    }
}
