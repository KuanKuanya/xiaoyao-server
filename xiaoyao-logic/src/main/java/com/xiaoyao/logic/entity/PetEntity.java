package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 战宠实体
 * 对应数据库表: t_pet
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("t_pet")
public class PetEntity extends BaseEntity {

    /**
     * 自增主键
     */
    @Id
    private Long id;

    /**
     * 玩家ID
     */
    private Long playerId;

    /**
     * 战宠配置ID
     */
    private String petCfgId;

    /**
     * 战宠名称(玩家自定义)
     */
    private String petName;

    /**
     * 等级
     */
    private Integer petLevel;

    /**
     * 经验
     */
    private Long petExp;

    /**
     * 品质: 1白 2绿 3蓝 4紫 5橙 6红 7金
     */
    private Integer petQuality;

    /**
     * 是否出战: 0-否 1-是
     */
    private Integer isActive;

    /**
     * 饱食度 (0-100)
     */
    private Integer hunger;

    /**
     * 心情值 (0-100)
     */
    private Integer mood;

    /**
     * 攻击加成
     */
    private Integer bonusAtk;

    /**
     * 防御加成
     */
    private Integer bonusDef;

    /**
     * 生命加成
     */
    private Integer bonusHp;

    /**
     * 获取途径: 0-未知 1-战斗捕获 2-商店 3-孵化
     */
    private Integer obtainWay;

    /**
     * 获取时间戳(毫秒)
     */
    private Long obtainTime;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
