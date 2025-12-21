package com.xiaoyao.common.cmd;

/**
 * 境界模块命令
 *
 * @author xiaoyao
 */
public interface RealmCmd {
    
    /** 主命令 */
    int cmd = CmdModule.REALM;
    
    /** 领取修炼收益 (挂机经验) */
    int cultivate = 1;
    
    /** 突破境界 */
    int breakthrough = 2;
    
    /** 飞升 */
    int ascend = 3;
    
    /** 获取境界信息 */
    int getRealmInfo = 4;
    
    /** 获取境界配置列表 */
    int getRealmList = 5;
}
