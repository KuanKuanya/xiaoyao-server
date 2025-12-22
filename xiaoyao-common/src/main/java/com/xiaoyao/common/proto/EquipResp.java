package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 装备响应
 */
@Data
public class EquipResp {
    /** 是否成功 */
    private boolean success;
    /** 被替换的装备ID (0表示无) */
    private int unequippedItemId;
    /** 消息 */
    private String message;
}
