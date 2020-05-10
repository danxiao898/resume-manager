package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Position implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所关联的职位类型的id
     */
    private Long positionTypeId;

    /**
     * 所关联的部门的id
     */
    private Long departmentId;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 途径，1校招，2社招
     */
    private Integer wayType;

    /**
     * 工作地点
     */
    private String location;

    /**
     * 要求的工作经验（年）
     */
    private Integer workExperience;

    /**
     * 工作职责
     */
    private String responsibility;

    /**
     * 工作要求
     */
    private String requirement;

    /**
     * 最低薪资
     */
    private Double lowerSalary;

    /**
     * 最高薪资
     */
    private Double upperSalary;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

    /**
     * 所在部门
     */
    @TableField(exist = false)
    private Department department;

    /**
     * 职位类别
     */
    @TableField(exist = false)
    private PositionType positionType;

    /**
     * 用于根据目标薪资筛选时用
     */
    @TableField(exist = false)
    private Double filterSalary;
}
