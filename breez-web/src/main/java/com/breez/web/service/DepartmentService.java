package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.Department;
import com.breez.web.entities.QuestionType;

public interface DepartmentService extends IService<Department> {
    IPage<Department> selectPage(Page<Department> page, Department department);
}
