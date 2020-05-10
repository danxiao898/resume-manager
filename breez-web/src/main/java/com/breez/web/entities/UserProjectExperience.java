package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目经历表
 */
@Data
public class UserProjectExperience {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 用到的关键技术
     */
    private String technology;

    /**
     * 开发目标
     */
    private String target;

    /**
     * 项目成员
     */
    private String member;

    /**
     * 个人职责/贡献
     */
    private String contribution;

    /**
     * 项目结果
     */
    private String result;

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
