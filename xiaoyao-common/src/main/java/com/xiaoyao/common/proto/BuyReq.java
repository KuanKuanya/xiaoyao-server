package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 购买请求
 */
@Data
@ProtobufClass
public class BuyReq {
    /** 商品ID */
    private int shopItemId;
    /** 购买数量 */
    private int count;
}
