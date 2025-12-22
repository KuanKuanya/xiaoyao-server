package com.xiaoyao.common.cmd;

/**
 * 排行榜模块命令
 *
 * @author xiaoyao
 */
public interface RankCmd {
    
    /** 主命令 */
    int cmd = CmdModule.RANK;
    
    /** 获取战力排行 */
    int getCombatPowerRank = 1;
    
    /** 获取境界排行 */
    int getRealmRank = 2;
    
    /** 获取财富排行 */
    int getWealthRank = 3;
    
    /** 获取我的排名 */
    int getMyRank = 4;
}
