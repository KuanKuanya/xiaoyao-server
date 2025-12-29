package com.xiaoyao.logic.inventory;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.xiaoyao.logic.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 装备实体
 *
 * @author xiaoyao
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("t_equipment")
public class EquipmentEntity extends BaseEntity {

    /** 自增ID */
    @Id(keyType = KeyType.Auto)
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

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
