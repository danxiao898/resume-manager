package com.breez.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.breez.web.entities.Question;
import com.breez.web.entities.QuestionRecord;
import com.breez.web.mapper.QuestionRecordMapper;
import com.breez.web.service.QuestionRecordService;
import com.breez.web.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionRecordServiceImpl extends ServiceImpl<QuestionRecordMapper, QuestionRecord> implements QuestionRecordService {

    @Autowired
    QuestionService questionService;

    @Override
    public List<QuestionRecord> selectByUserId(Long userId) {

        Map<String,Object> map = new LinkedHashMap<>();

        map.put("user_id",userId);

        List<QuestionRecord> questionRecords = baseMapper.selectByMap(map);

        for (QuestionRecord questionRecord : questionRecords) {
            Question question = questionService.findById(questionRecord.getQuestionId());

            questionRecord.setQuestion(question);
        }

        return questionRecords;
    }
}
