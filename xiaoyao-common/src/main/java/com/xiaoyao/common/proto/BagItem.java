package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 背包物品
 */
@Data
@ProtobufClass
public class BagItem {
    /** 物品ID */
    private int itemId;
    /** 数量 */
    private int count;
    /** 槽位 */
    private int slot;
    /** 是否锁定 */
    private boolean locked;
}
