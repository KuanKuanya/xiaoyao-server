package com.xiaoyao.logic.common.exception;

/**
 * 错误码常量
 * 按模块划分错误码段:
 * - 1000-1999: 通用错误
 * - 2000-2999: 战宠系统
 * - 3000-3999: 宗门系统
 * - 4000-4999: 任务系统
 * - 5000-5999: 成就系统
 * - 6000-6999: 邮件系统
 * - 7000-7999: 公告系统
 *
 * @author xiaoyao
 */
public interface ErrorCode {

    // ========== 通用错误 (1000-1999) ==========
    /** 参数错误 */
    int PARAM_INVALID = 1001;
    /** 数据不存在 */
    int DATA_NOT_FOUND = 1002;
    /** 数据已存在 */
    int DATA_ALREADY_EXISTS = 1003;
    /** 操作频繁 */
    int OPERATION_TOO_FREQUENT = 1004;
    /** 状态错误 */
    int STATE_INVALID = 1005;
    /** 权限不足 */
    int PERMISSION_DENIED = 1006;

    // ========== 战宠系统 (2000-2999) ==========
    /** 战宠不存在 */
    int PET_NOT_FOUND = 2001;
    /** 战宠数量已达上限 */
    int PET_COUNT_LIMIT = 2002;
    /** 战宠等级不足 */
    int PET_LEVEL_NOT_ENOUGH = 2003;
    /** 战宠品质不符 */
    int PET_QUALITY_MISMATCH = 2004;
    /** 战宠饱食度不足 */
    int PET_HUNGER_LOW = 2005;
    /** 战宠心情值不足 */
    int PET_MOOD_LOW = 2006;
    /** 已有出战战宠 */
    int PET_ALREADY_ACTIVE = 2007;

    // ========== 宗门系统 (3000-3999) ==========
    /** 宗门不存在 */
    int SECT_NOT_FOUND = 3001;
    /** 已加入宗门 */
    int SECT_ALREADY_JOINED = 3002;
    /** 未加入宗门 */
    int SECT_NOT_JOINED = 3003;
    /** 宗门人数已满 */
    int SECT_MEMBER_FULL = 3004;
    /** 贡献度不足 */
    int SECT_CONTRIBUTION_NOT_ENOUGH = 3005;
    /** 职位权限不足 */
    int SECT_POSITION_NOT_ENOUGH = 3006;
    /** 俸禄已领取 */
    int SECT_SALARY_ALREADY_CLAIMED = 3007;
    /** 俸禄领取冷却中 */
    int SECT_SALARY_COOLDOWN = 3008;

    // ========== 任务系统 (4000-4999) ==========
    /** 任务不存在 */
    int QUEST_NOT_FOUND = 4001;
    /** 任务未完成 */
    int QUEST_NOT_COMPLETED = 4002;
    /** 任务已领取 */
    int QUEST_ALREADY_CLAIMED = 4003;
    /** 任务进度已满 */
    int QUEST_PROGRESS_FULL = 4004;
    /** 任务配置错误 */
    int QUEST_CONFIG_ERROR = 4005;

    // ========== 成就系统 (5000-5999) ==========
    /** 成就不存在 */
    int ACHIEVEMENT_NOT_FOUND = 5001;
    /** 成就未完成 */
    int ACHIEVEMENT_NOT_COMPLETED = 5002;
    /** 成就已领取 */
    int ACHIEVEMENT_ALREADY_CLAIMED = 5003;
    /** 成就配置错误 */
    int ACHIEVEMENT_CONFIG_ERROR = 5004;

    // ========== 邮件系统 (6000-6999) ==========
    /** 邮件模板不存在 */
    int MAIL_TEMPLATE_NOT_FOUND = 6001;
    /** 邮件模板已发送 */
    int MAIL_TEMPLATE_ALREADY_SENT = 6002;
    /** 邮件模板已撤回 */
    int MAIL_TEMPLATE_REVOKED = 6003;
    /** 邮件附件格式错误 */
    int MAIL_ATTACHMENT_INVALID = 6004;
    /** 邮件目标条件错误 */
    int MAIL_TARGET_CONDITION_INVALID = 6005;

    // ========== 公告系统 (7000-7999) ==========
    /** 公告不存在 */
    int NOTICE_NOT_FOUND = 7001;
    /** 公告已过期 */
    int NOTICE_EXPIRED = 7002;
    /** 公告未生效 */
    int NOTICE_NOT_ACTIVE = 7003;
    /** 公告平台不匹配 */
    int NOTICE_PLATFORM_MISMATCH = 7004;
}
