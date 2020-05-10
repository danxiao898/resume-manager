package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 职位申请状态实体类
 */
@Data
public class PositionApplyStatus implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 职位id
     */
    private Long positionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 状态:0待查看;1已查看;2.邀请一面;3.邀请二面;4.未通过;5.已通过
     */
    private Integer status;

    /**
     * 职位信息
     */
    @TableField(exist = false)
    private Position position;

    /**
     * 用户基本信息
     */
    @TableField(exist = false)
    private UserBaseInfo userBaseInfo;
}
