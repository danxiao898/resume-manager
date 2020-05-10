package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Position;
import com.breez.web.entities.PositionCollect;
import com.breez.web.mapper.PositionCollectMapper;
import com.breez.web.service.PositionCollectService;
import com.breez.web.service.PositionService;
import com.breez.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PositionCollectServiceImpl extends ServiceImpl<PositionCollectMapper, PositionCollect> implements PositionCollectService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private PositionService positionService;

    public boolean collectPosition(Long positionId, Long userId) {
        Map<String,Object> map = new HashMap<>();

        map.put("position_id", positionId);
        map.put("user_id", userId);
        List<PositionCollect> positionCollects = baseMapper.selectByMap(map);

        //已经存在该记录了,不做任何操作
        if(positionCollects != null && positionCollects.size() > 0) {
            return true;
        }

        PositionCollect positionCollect = new PositionCollect();
        positionCollect.setPositionId(positionId);
        positionCollect.setUserId(userId);

        saveOrUpdate(positionCollect);

        return true;
    }

    @Override
    public List<PositionCollect> getMyCollect() {
        Map<String, Object> map = new HashMap<>();

        map.put("user_id", sysUserService.getCurrentUser().getId());

        List<PositionCollect> positionCollects = baseMapper.selectByMap(map);

        //获取职位详情
        for (PositionCollect positionCollect : positionCollects) {
            Position position = positionService.findById(positionCollect.getPositionId());
            positionCollect.setPosition(position);
        }

        return positionCollects;
    }
}
