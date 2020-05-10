package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 职位收藏实体类
 */
@Data
public class PositionCollect implements Serializable {

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
     * 职位信息
     */
    @TableField(exist = false)
    private Position position;
}
