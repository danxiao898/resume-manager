package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户基本信息表
 */
@Data
public class UserBaseInfo {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所关联的用户表的id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 目标职位
     */
    private String targetPosition;

    /**
     * 目标地点
     */
    private String targetLocation;

    /**
     * 性别1男，2女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 英语等级
     */
    private String cet;

    /**
     * 计算机等级
     */
    private String cct;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 自我阐述
     */
    private String narrate;

    /**
     * 头像资源信息
     */
    private String photoSrc;
}
