package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data//lombok插件，让lombok帮忙写getter、setter、toString方法
public class QuestionType implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 问题类型表的类型名
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;
}