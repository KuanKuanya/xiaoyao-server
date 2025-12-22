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
public class BagItemEntity extends BaseEntity {

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

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
