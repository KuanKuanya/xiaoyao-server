package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 购买请求
 */
@Data
public class BuyReq {
    /** 商品ID */
    private int shopItemId;
    /** 购买数量 */
    private int count;
}
