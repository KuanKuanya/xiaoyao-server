package com.xiaoyao.logic.constants;

/**
 * 游戏业务常量
 * 集中管理所有游戏相关的配置常量
 *
 * @author xiaoyao
 */
public interface GameConstants {

    // ========== 战宠系统 ==========
    /** 玩家战宠数量上限 */
    int PET_MAX_COUNT = 50;
    /** 同时出战战宠数量上限 */
    int PET_ACTIVE_MAX_COUNT = 3;
    /** 饱食度上限 */
    int PET_HUNGER_MAX = 100;
    /** 心情值上限 */
    int PET_MOOD_MAX = 100;
    /** 战宠品质-白色 */
    int PET_QUALITY_WHITE = 1;
    /** 战宠品质-绿色 */
    int PET_QUALITY_GREEN = 2;
    /** 战宠品质-蓝色 */
    int PET_QUALITY_BLUE = 3;
    /** 战宠品质-紫色 */
    int PET_QUALITY_PURPLE = 4;
    /** 战宠品质-橙色 */
    int PET_QUALITY_ORANGE = 5;
    /** 战宠品质-红色 */
    int PET_QUALITY_RED = 6;
    /** 战宠品质-金色 */
    int PET_QUALITY_GOLD = 7;

    // ========== 宗门系统 ==========
    /** 宗门成员数量上限 */
    int SECT_MEMBER_MAX_COUNT = 100;
    /** 宗门职位-弟子 */
    int SECT_POSITION_DISCIPLE = 0;
    /** 宗门职位-内门弟子 */
    int SECT_POSITION_INNER = 1;
    /** 宗门职位-长老 */
    int SECT_POSITION_ELDER = 2;
    /** 宗门职位-副掌门 */
    int SECT_POSITION_VICE_LEADER = 3;
    /** 宗门职位-掌门 */
    int SECT_POSITION_LEADER = 4;
    /** 俸禄领取冷却时间(秒) - 24小时 */
    long SECT_SALARY_COOLDOWN_SECONDS = 86400L;

    // ========== 任务系统 ==========
    /** 每日任务状态-进行中 */
    int QUEST_STATUS_ONGOING = 0;
    /** 每日任务状态-已完成 */
    int QUEST_STATUS_COMPLETED = 1;
    /** 每日任务状态-已领取 */
    int QUEST_STATUS_CLAIMED = 2;
    /** 每日任务数量上限 */
    int DAILY_QUEST_MAX_COUNT = 20;

    // ========== 成就系统 ==========
    /** 成就状态-进行中 */
    int ACHIEVEMENT_STATUS_ONGOING = 0;
    /** 成就状态-已完成 */
    int ACHIEVEMENT_STATUS_COMPLETED = 1;
    /** 成就状态-已领取 */
    int ACHIEVEMENT_STATUS_CLAIMED = 2;

    // ========== 邮件系统 ==========
    /** 邮件模板状态-待发送 */
    int MAIL_TEMPLATE_STATUS_PENDING = 0;
    /** 邮件模板状态-已发送 */
    int MAIL_TEMPLATE_STATUS_SENT = 1;
    /** 邮件模板状态-已撤回 */
    int MAIL_TEMPLATE_STATUS_REVOKED = 2;
    /** 邮件目标类型-全服 */
    int MAIL_TARGET_ALL_PLAYERS = 1;
    /** 邮件目标类型-指定玩家 */
    int MAIL_TARGET_SPECIFIC_PLAYERS = 2;
    /** 邮件目标类型-条件筛选 */
    int MAIL_TARGET_CONDITION = 3;

    // ========== 公告系统 ==========
    /** 公告类型-系统公告 */
    int NOTICE_TYPE_SYSTEM = 1;
    /** 公告类型-活动公告 */
    int NOTICE_TYPE_ACTIVITY = 2;
    /** 公告类型-更新公告 */
    int NOTICE_TYPE_UPDATE = 3;
    /** 公告类型-紧急公告 */
    int NOTICE_TYPE_URGENT = 4;
    /** 平台-全部 */
    String PLATFORM_ALL = "all";
    /** 平台-微信 */
    String PLATFORM_WECHAT = "wechat";
    /** 平台-抖音 */
    String PLATFORM_DOUYIN = "douyin";
    /** 平台-iOS */
    String PLATFORM_IOS = "ios";
    /** 平台-Android */
    String PLATFORM_ANDROID = "android";

    // ========== 通用 ==========
    /** 逻辑删除-正常 */
    int DELETED_NO = 0;
    /** 逻辑删除-已删除 */
    int DELETED_YES = 1;
    /** 是-标志 */
    int YES = 1;
    /** 否-标志 */
    int NO = 0;
}
