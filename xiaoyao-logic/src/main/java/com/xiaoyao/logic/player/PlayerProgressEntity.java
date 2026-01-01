package com.xiaoyao.logic.player;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 玩家进度实体
 * 对应表: t_player_progress
 * 存储境界、地图、洞府进度
 *
 * @author xiaoyao
 */
@Data
@Table("t_player_progress")
public class PlayerProgressEntity {

    /** 玩家ID (与 t_player.id 一对一) */
    @Id(keyType = KeyType.None)
    private Long playerId;

    // ========== 境界进度 ==========

    /** 境界ID */
    private Integer realmId;

    /** 当前境界经验 */
    private Long realmExp;

    /** 累计总修为 */
    private Long totalExp;

    /** 是否已飞升 */
    private Boolean ascended;

    // ========== 地图进度 ==========

    /** 镇妖塔当前层数 */
    private Integer towerFloor;

    /** 已解锁最高地图ID */
    private Integer unlockedMapId;

    // ========== 洞府进度 ==========

    /** 聚灵阵等级 */
    private Integer spiritArrayLevel;

    /** 上次领取修炼时间 */
    private Long lastCultivateTime;

    /** 乐观锁版本号 */
    @Column(version = true)
    private Integer version;

    /** 更新时间 */
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updatedAt;
}
