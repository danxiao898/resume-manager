package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class UserWorkExperience {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 职位
     */
    private String jobTitle;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 描述
     */
    private String des;

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
