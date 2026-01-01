package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 背包物品
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class BagItem {
    /** 物品ID */
    private int itemId;
    /** 数量 */
    private int count;
    /** 槽位 */
    private int slot;
    /** 是否锁定 */
    private boolean locked;
}
