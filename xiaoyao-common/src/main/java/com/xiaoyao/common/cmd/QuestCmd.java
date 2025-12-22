package com.xiaoyao.common.cmd;

/**
 * 任务模块命令
 *
 * @author xiaoyao
 */
public interface QuestCmd {
    
    /** 主命令 */
    int cmd = CmdModule.QUEST;
    
    /** 获取任务列表 */
    int getList = 1;
    
    /** 领取任务奖励 */
    int claim = 2;
    
    /** 获取每日任务 */
    int getDailyList = 3;
    
    /** 获取成长任务 */
    int getGrowthList = 4;
    
    /** 获取任务进度 */
    int getProgress = 5;
}
