package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 背包物品实体
 *
 * @author xiaoyao
 */
@Data
@Table("t_bag_item")
public class BagItemEntity extends BaseEntity {

    /** 自增ID */
    @Id
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
