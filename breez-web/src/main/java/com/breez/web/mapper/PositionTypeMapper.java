package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.Department;
import com.breez.web.entities.PositionType;
import org.apache.ibatis.annotations.Param;

/**
 * 问卷类型的mapper接口
 */
public interface PositionTypeMapper extends BaseMapper<PositionType> {

    IPage<PositionType> selectPage(Page<PositionType> page, @Param("p") PositionType positionType);
}
