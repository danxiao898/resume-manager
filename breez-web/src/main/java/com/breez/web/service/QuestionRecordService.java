package com.breez.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.breez.web.entities.QuestionRecord;

import java.util.List;

public interface QuestionRecordService extends IService<QuestionRecord> {

    List<QuestionRecord> selectByUserId(Long userId);
}
