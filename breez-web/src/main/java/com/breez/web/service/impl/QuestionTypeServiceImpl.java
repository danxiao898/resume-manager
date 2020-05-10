package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.QuestionType;
import com.breez.web.mapper.QuestionTypeMapper;
import com.breez.web.service.QuestionTypeService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeMapper, QuestionType> implements QuestionTypeService {

    @Override
    public IPage<QuestionType> selectPage(Page<QuestionType> page, QuestionType questionType) {
        return baseMapper.selectPage(page, questionType);
    }

    @Override
    public QuestionType getById(Serializable id) {
        if(id == null) {
            return new QuestionType();
        }
        return super.getById(id);
    }
}
