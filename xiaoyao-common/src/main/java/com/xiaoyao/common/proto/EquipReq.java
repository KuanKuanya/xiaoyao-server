package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 装备请求
 */
@Data
@ProtobufClass
public class EquipReq {
    /** 物品ID */
    private int itemId;
}
