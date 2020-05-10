package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Department;
import com.breez.web.entities.QuestionType;
import com.breez.web.mapper.DepartmentMapper;
import com.breez.web.mapper.QuestionTypeMapper;
import com.breez.web.service.DepartmentService;
import com.breez.web.service.QuestionTypeService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public IPage<Department> selectPage(Page<Department> page, Department department) {
        return baseMapper.selectPage(page, department);
    }

    @Override
    public Department getById(Serializable id) {
        if(id == null) {
            return new Department();
        }
        return super.getById(id);
    }
}
