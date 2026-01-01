package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

import java.util.List;

/**
 * 任务列表响应
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class QuestListResp {
    /** 任务列表 */
    private List<QuestInfo> quests;
    /** 可领取数量 */
    private int claimableCount;
    /** 每日任务重置时间 */
    private long dailyResetTime;
}
