package com.breez.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.breez.web.entities.QuestionType;
import org.apache.ibatis.annotations.Param;

/**
 * 问卷类型的mapper接口
 */
public interface QuestionTypeMapper extends BaseMapper<QuestionType> {

    IPage<QuestionType> selectPage(Page<QuestionType> page, @Param("qt") QuestionType questionType);
}
