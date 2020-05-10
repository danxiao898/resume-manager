package com.breez.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户专业技能表
 */
@Data
public class UserProfessionalSkill {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;

    /**
     * 技能名
     */
    private String name;

    /**
     * 熟练程度1-100
     */
    private Integer masteryDegree;
}
