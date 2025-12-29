package com.xiaoyao.logic.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 挂机记录实体
 *
 * @author xiaoyao
 */
@Data
@Table("t_idle_battle")
public class IdleBattleEntity extends BaseEntity {

    /** 自增ID */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /** 玩家ID */
    private Long playerId;

    /** 地图ID */
    private Integer mapId;

    /** 开始时间戳 */
    private Long startTime;

    /** 是否进行中 */
    private Boolean isActive;

    /** 领取时间戳 */
    private Long claimTime;

    /** 累计经验 */
    private Long totalExp;

    /** 累计灵石 */
    private Long totalStones;

    /** 战斗次数 */
    private Integer battleCount;

    /** 胜利次数 */
    private Integer victoryCount;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
