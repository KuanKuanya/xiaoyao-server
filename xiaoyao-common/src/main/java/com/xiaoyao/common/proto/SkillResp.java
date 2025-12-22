package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 技能操作响应
 */
@Data
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
