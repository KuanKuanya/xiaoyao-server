package com.xiaoyao.logic.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 背包物品实体
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("t_bag_item")
public class BagItemEntity extends BaseEntity {

    /** 自增ID */
    @Id(keyType = KeyType.Auto)
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
