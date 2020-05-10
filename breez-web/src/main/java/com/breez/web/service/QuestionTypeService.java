package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.QuestionType;

public interface QuestionTypeService extends IService<QuestionType> {
    IPage<QuestionType> selectPage(Page<QuestionType> page, QuestionType questionType);
}
