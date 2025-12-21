package com.xiaoyao.common.cmd;

/**
 * 玩家模块命令
 *
 * @author xiaoyao
 */
public interface PlayerCmd {
    
    /** 主命令 */
    int cmd = CmdModule.PLAYER;
    
    /** 登录 */
    int login = 1;
    
    /** 登出 */
    int logout = 2;
    
    /** 获取玩家信息 */
    int getInfo = 3;
    
    /** 同步玩家数据 */
    int syncData = 4;
    
    /** 心跳 */
    int heartbeat = 5;
    
    /** 修改昵称 */
    int changeNickname = 6;
    
    /** 获取离线收益 */
    int getOfflineReward = 7;
}
