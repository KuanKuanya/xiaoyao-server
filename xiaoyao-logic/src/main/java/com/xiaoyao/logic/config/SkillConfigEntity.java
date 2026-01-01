package com.xiaoyao.logic.config;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 技能配置实体
 * 对应表: t_cfg_skill
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_skill")
@ProtobufClass
public class SkillConfigEntity {

    /** 技能ID */
    @Id(keyType = KeyType.None)
    private Integer id;

    /** 技能名称 */
    private String name;

    /** 类型: 1主动 2被动 3心法 */
    private Integer skillType;

    /** 品质: 1白 2绿 3蓝 4紫 5橙 6红 7金 */
    private Integer quality;

    /** 图标(emoji) */
    private String icon;

    /** 技能描述 */
    private String description;

    /** 伤害倍率 */
    private Double damageRate;

    /** 冷却时间(秒) */
    private Integer cooldown;

    /** 法力消耗 */
    private Integer mpCost;

    /** 被动属性加成 JSON {atkPct, defPct, hpPct, expMult} */
    private String passiveStats;

    /** 战斗效果 JSON [{type, value, duration}] */
    private String battleEffects;

    /** 学习需求境界 */
    private Integer reqRealmId;

    /** 最大等级 */
    private Integer maxLevel;

    /** 是否启用 */
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
