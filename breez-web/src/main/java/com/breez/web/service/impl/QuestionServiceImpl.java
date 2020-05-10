package com.breez.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Question;
import com.breez.web.entities.QuestionType;
import com.breez.web.mapper.QuestionMapper;
import com.breez.web.service.QuestionService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Override
    public IPage<Question> selectPage(Page<Question> page, Question question) {
        return baseMapper.selectPage(page, question);
    }

    @Override
    public Question findById(Long id) {
        if(id == null) {
            Question question = new Question();
            question.setQuestionType(new QuestionType());
            return question;
        }
        return baseMapper.findById(id);
    }

}
