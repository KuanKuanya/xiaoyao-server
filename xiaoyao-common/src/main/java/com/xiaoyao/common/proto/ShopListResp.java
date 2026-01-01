package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

import java.util.List;

/**
 * 商店列表响应
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class ShopListResp {
    /** 商品列表 */
    private List<ShopItem> items;
    /** 下次刷新时间 (时间戳) */
    private long nextRefreshTime;
    /** 今日剩余刷新次数 */
    private int refreshRemain;
}
