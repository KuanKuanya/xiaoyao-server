package com.xiaoyao.common.cmd;

/**
 * 背包模块命令
 *
 * @author xiaoyao
 */
public interface InventoryCmd {
    
    /** 主命令 */
    int cmd = CmdModule.INVENTORY;
    
    /** 获取背包列表 */
    int getList = 1;
    
    /** 使用物品 */
    int useItem = 2;
    
    /** 丢弃物品 */
    int dropItem = 3;
    
    /** 锁定/解锁物品 */
    int lockItem = 4;
    
    /** 出售物品 */
    int sellItem = 5;
    
    /** 装备穿戴 */
    int equip = 10;
    
    /** 卸下装备 */
    int unequip = 11;
    
    /** 装备强化 */
    int enhance = 12;
    
    /** 装备升星 */
    int starUp = 13;
}
