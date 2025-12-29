package com.xiaoyao.logic.skill;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.xiaoyao.logic.common.entity.BaseEntity;
import lombok.Data;

/**
 * 玩家技能实体
 *
 * @author xiaoyao
 */
@Data
@Table("t_player_skill")
public class PlayerSkillEntity extends BaseEntity {

    /** 自增ID */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /** 玩家ID */
    private Long playerId;

    /** 技能ID */
    private Integer skillId;

    /** 技能等级 */
    private Integer level;

    /** 当前经验 */
    private Integer exp;

    /** 是否装备 */
    private Boolean equipped;

    /** 装备槽位 (主动技能0-3, 心法99) */
    private Integer equipSlot;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
