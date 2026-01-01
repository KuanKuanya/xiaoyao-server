package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 使用物品响应
 */
@Data
@ProtobufClass
public class UseItemResp {
    /** 是否成功 */
    private boolean success;
    /** 物品ID */
    private int itemId;
    /** 剩余数量 */
    private int remainCount;
    /** 获得经验 */
    private long expGained;
    /** 恢复生命 */
    private int hpRecovered;
    /** 消息 */
    private String message;
}
