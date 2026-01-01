package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 使用物品请求
 */
@Data
@ProtobufClass
public class UseItemReq {
    /** 物品ID */
    private int itemId;
    /** 使用数量 */
    private int count = 1;
}
