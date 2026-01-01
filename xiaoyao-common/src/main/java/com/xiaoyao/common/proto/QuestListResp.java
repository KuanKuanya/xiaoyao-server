package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 任务列表响应
 */
@Data
@ProtobufClass
public class QuestListResp {
    /** 任务列表 */
    private List<QuestInfo> quests;
    /** 可领取数量 */
    private int claimableCount;
    /** 每日任务重置时间 */
    private long dailyResetTime;
}
