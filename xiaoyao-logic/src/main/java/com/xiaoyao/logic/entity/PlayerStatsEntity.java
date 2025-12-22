package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩家属性实体
 * 存储玩家的战斗属性，与 t_player 一对一关系
 *
 * @author xiaoyao
 */
@Data
@TableName("t_player_stats")
public class PlayerStatsEntity extends BaseEntity {

    /** 玩家ID (与 t_player.id 一对一) */
    @TableId(type = IdType.INPUT)  // 不自增，直接使用 playerId
    private Long id;

    // ============ 基础属性 ============

    /** 基础攻击 */
    private Integer baseAtk;

    /** 基础防御 */
    private Integer baseDef;

    /** 基础生命 */
    private Integer baseHp;

    /** 基础法力 */
    private Integer baseMp;

    // ============ 战斗属性 ============

    /** 暴击率 (0.0500 = 5%) */
    private BigDecimal critRate;

    /** 暴击伤害倍率 (1.5000 = 150%) */
    private BigDecimal critDamage;

    /** 闪避率 */
    private BigDecimal dodgeRate;

    /** 命中率 */
    private BigDecimal hitRate;

    /** 格挡率 */
    private BigDecimal blockRate;

    /** 吸血率 */
    private BigDecimal lifestealRate;

    // ============ 计算属性 ============

    /** 战斗力 */
    private Long combatPower;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
