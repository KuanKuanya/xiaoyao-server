package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 商店列表响应
 */
@Data
@ProtobufClass
public class ShopListResp {
    /** 商品列表 */
    private List<ShopItem> items;
    /** 下次刷新时间 (时间戳) */
    private long nextRefreshTime;
    /** 今日剩余刷新次数 */
    private int refreshRemain;
}
