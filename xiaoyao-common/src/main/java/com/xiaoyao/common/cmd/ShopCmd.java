package com.xiaoyao.common.cmd;

/**
 * 商店模块命令
 *
 * @author xiaoyao
 */
public interface ShopCmd {
    
    /** 主命令 */
    int cmd = CmdModule.SHOP;
    
    /** 获取商店列表 */
    int getList = 1;
    
    /** 购买物品 */
    int buy = 2;
    
    /** 刷新商店 */
    int refresh = 3;
    
    /** 获取购买记录 */
    int getRecord = 4;
}
