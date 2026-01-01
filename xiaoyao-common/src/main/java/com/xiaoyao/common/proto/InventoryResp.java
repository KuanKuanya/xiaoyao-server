package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 背包列表响应
 */
@Data
@ProtobufClass
public class InventoryResp {
    /** 背包物品列表 */
    private List<BagItem> items;
    /** 已装备列表 */
    private List<EquippedItem> equipped;
    /** 背包容量 */
    private int capacity;
    /** 已用格子 */
    private int usedSlots;
}
