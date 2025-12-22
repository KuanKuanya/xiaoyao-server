package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 挂机记录实体
 *
 * @author xiaoyao
 */
@Data
@TableName("t_idle_battle")
public class IdleBattleEntity {
    
    /** 自增ID */
    @TableId(type = IdType.AUTO)
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
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
