package com.breez.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.Question;

public interface QuestionService extends IService<Question> {
    IPage<Question> selectPage(Page<Question> page, Question question);

    Question findById(Long id);
}
