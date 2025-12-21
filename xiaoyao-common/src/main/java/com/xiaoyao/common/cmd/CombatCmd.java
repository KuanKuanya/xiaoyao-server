package com.xiaoyao.common.cmd;

/**
 * 战斗模块命令
 *
 * @author xiaoyao
 */
public interface CombatCmd {
    
    /** 主命令 */
    int cmd = CmdModule.COMBAT;
    
    /** 开始战斗 */
    int startBattle = 1;
    
    /** 战斗操作 (使用技能等) */
    int battleAction = 2;
    
    /** 撤退 */
    int retreat = 3;
    
    /** 获取战斗结果 */
    int getBattleResult = 4;
    
    /** 开始挂机战斗 */
    int startIdleBattle = 5;
    
    /** 停止挂机战斗 */
    int stopIdleBattle = 6;
    
    /** 领取挂机战斗奖励 */
    int claimIdleReward = 7;
}
