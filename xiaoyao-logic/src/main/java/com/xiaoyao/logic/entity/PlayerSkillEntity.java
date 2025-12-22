package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 玩家技能实体
 *
 * @author xiaoyao
 */
@Data
@TableName("t_player_skill")
public class PlayerSkillEntity {
    
    /** 自增ID */
    @TableId(type = IdType.AUTO)
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
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
