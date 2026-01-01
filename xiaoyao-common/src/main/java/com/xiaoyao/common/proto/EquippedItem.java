package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 装备信息
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class EquippedItem {
    /** 槽位 (WEAPON/ARMOR/HEAD/FEET/BELT/ARTIFACT) */
    private String slot;
    /** 物品ID */
    private int itemId;
    /** 强化等级 */
    private int enhanceLevel;
    /** 升星等级 */
    private int starLevel;
}
