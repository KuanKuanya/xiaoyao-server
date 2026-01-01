package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 技能操作响应
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class SkillResp {
    /** 是否成功 */
    private boolean success;
    /** 技能ID */
    private int skillId;
    /** 新等级 (升级时) */
    private int newLevel;
    /** 消息 */
    private String message;
}
