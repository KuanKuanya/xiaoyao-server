package com.xiaoyao.common.proto;

import lombok.Data;

import java.util.List;

/**
 * 任务信息
 */
@Data
public class QuestInfo {
    /** 任务ID */
    private int questId;
    /** 任务类型 (1=每日 2=成长 3=主线) */
    private int type;
    /** 任务名称 */
    private String name;
    /** 任务描述 */
    private String desc;
    /** 当前进度 */
    private int progress;
    /** 目标进度 */
    private int target;
    /** 状态 (0=进行中 1=可领取 2=已完成) */
    private int status;
    /** 奖励预览 */
    private List<BagItem> rewards;
}
