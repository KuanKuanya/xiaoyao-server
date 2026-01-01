package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 装备响应
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class EquipResp {
    /** 是否成功 */
    private boolean success;
    /** 被替换的装备ID (0表示无) */
    private int unequippedItemId;
    /** 消息 */
    private String message;
}
