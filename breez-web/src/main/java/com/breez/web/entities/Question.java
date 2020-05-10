package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Question implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 问题类型
     */
    @TableField(exist = false)
    private QuestionType questionType;

    /**
     * 问题描述
     */
    private String des;

    /**
     * 答案
     */
    private String answer;

    /**
     * 选项A
     */
    private String optionA;

    /**
     * 选项B
     */
    private String optionB;

    /**
     * 选项C
     */
    private String optionC;

    /**
     * 选项D
     */
    private String optionD;

    /**
     * 问题类别所对应的id
     */
    private Long QuestionTypeId;


}
