package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 装备响应
 */
@Data
@ProtobufClass
public class EquipResp {
    /** 是否成功 */
    private boolean success;
    /** 被替换的装备ID (0表示无) */
    private int unequippedItemId;
    /** 消息 */
    private String message;
}
