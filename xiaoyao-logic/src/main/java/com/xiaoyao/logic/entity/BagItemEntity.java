package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 背包物品实体
 *
 * @author xiaoyao
 */
@Data
@TableName("t_bag_item")
public class BagItemEntity {
    
    /** 自增ID */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 玩家ID */
    private Long playerId;
    
    /** 物品ID */
    private Integer itemId;
    
    /** 数量 */
    private Integer count;
    
    /** 槽位 */
    private Integer slot;
    
    /** 是否锁定 */
    private Boolean locked;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
