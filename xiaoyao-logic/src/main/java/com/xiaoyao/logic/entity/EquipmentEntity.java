package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 装备实体
 *
 * @author xiaoyao
 */
@Data
@TableName("t_equipment")
public class EquipmentEntity {
    
    /** 自增ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 玩家ID */
    private Long playerId;
    
    /** 槽位 WEAPON/ARMOR/HEAD/FEET/BELT/ARTIFACT */
    private String slot;
    
    /** 物品ID */
    private Integer itemId;
    
    /** 强化等级 */
    private Integer enhanceLevel;
    
    /** 升星等级 */
    private Integer starLevel;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
