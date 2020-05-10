package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 教育经历表
 */
@Data
public class UserEducationExperience {

    @TableId(type = IdType.AUTO)
    private Long id;


    private Long userId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 专业
     */
    private String major;

    /**
     * 学历（专科、本科、研究生、博士等等）
     */
    private String educationBackground;

    /**
     * 描述（获奖经历、突出贡献等）
     */
    private String des;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    @TableField(exist = false)
    private String stringStartDate;

    @TableField(exist = false)
    private String stringEndDate;

    public String getStringStartDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        if(startDate != null)
            return dateFormat.format(startDate);

        return "";
    }

    public String getStringEndDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        if(endDate != null)
            return dateFormat.format(endDate);

        return "至今";
    }
}
