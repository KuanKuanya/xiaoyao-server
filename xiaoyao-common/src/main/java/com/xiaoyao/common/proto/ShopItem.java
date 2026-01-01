package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 商店商品
 */
@Data
@ProtobufClass
public class ShopItem {
    /** 商品ID */
    private int id;
    /** 物品ID */
    private int itemId;
    /** 数量 */
    private int count;
    /** 价格 */
    private int price;
    /** 货币类型 (1=灵石 2=仙玉) */
    private int currencyType;
    /** 限购数量 (0=不限) */
    private int limitCount;
    /** 已购买数量 */
    private int boughtCount;
    /** 折扣 (100=原价) */
    private int discount;
}
