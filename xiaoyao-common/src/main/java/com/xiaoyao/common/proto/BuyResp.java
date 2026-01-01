package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 购买响应
 */
@Data
@ProtobufClass
public class BuyResp {
    /** 是否成功 */
    private boolean success;
    /** 购买的物品ID */
    private int itemId;
    /** 购买数量 */
    private int count;
    /** 花费 */
    private int cost;
    /** 剩余货币 */
    private long remainCurrency;
    /** 消息 */
    private String message;
}
