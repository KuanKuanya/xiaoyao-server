package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 装备信息
 */
@Data
public class EquippedItem {
    /** 槽位 (WEAPON/ARMOR/HEAD/FEET/BELT/ARTIFACT) */
    private String slot;
    /** 物品ID */
    private int itemId;
    /** 强化等级 */
    private int enhanceLevel;
    /** 升星等级 */
    private int starLevel;
}
