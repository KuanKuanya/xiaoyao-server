package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 玩家属性实体
 * 对应表: t_player_attribute
 * 存储战斗属性，与 t_player 一对一关系
 * 百分比属性使用万分比整型存储: 500=5%, 10000=100%, 15000=150%
 *
 * @author xiaoyao
 */
@Data
@Table("t_player_attribute")
public class PlayerStatsEntity {

    /** 玩家ID (与 t_player.id 一对一) */
    @Id
    private Long playerId;

    // ========== 战力 (高频更新) ==========

    /** 战斗力 */
    private Long combatPower;

    // ========== 基础属性 ==========

    /** 基础攻击 */
    private Integer baseAtk;

    /** 基础防御 */
    private Integer baseDef;

    /** 基础生命 */
    private Integer baseHp;

    /** 基础法力 */
    private Integer baseMp;

    // ========== 战斗属性 (万分比: 500=5%) ==========

    /** 暴击率 (万分比) */
    private Integer critRate;

    /** 暴击伤害 (万分比, 15000=150%) */
    private Integer critDamage;

    /** 闪避率 (万分比) */
    private Integer dodgeRate;

    /** 命中率 (万分比) */
    private Integer hitRate;

    /** 格挡率 (万分比) */
    private Integer blockRate;

    /** 吸血率 (万分比) */
    private Integer lifestealRate;

    /** 乐观锁版本号 */
    @Version
    private Integer version;

    /** 更新时间 */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
