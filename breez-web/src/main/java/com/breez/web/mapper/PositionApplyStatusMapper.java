package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.PositionApplyStatus;
import org.apache.ibatis.annotations.Param;

public interface PositionApplyStatusMapper extends BaseMapper<PositionApplyStatus> {

    /**
     * 分页查找数据
     * @param page 分页对象
     * @param wayType 招聘途径，1校招,2社招
     * @param positionApplyStatus 职位申请进度对象
     * @return
     */
    IPage<PositionApplyStatus> selectPage(Page<PositionApplyStatus> page, @Param("wayType") Integer wayType, @Param("ps") PositionApplyStatus positionApplyStatus);

    PositionApplyStatus findById(@Param("id") Long id);
}
