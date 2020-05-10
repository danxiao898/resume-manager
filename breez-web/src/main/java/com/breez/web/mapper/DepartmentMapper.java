package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.Department;
import com.breez.web.entities.QuestionType;
import org.apache.ibatis.annotations.Param;

/**
 * 问卷类型的mapper接口
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    IPage<Department> selectPage(Page<Department> page, @Param("d") Department department);
}
