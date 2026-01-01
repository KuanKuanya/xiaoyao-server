package com.xiaoyao.logic.config;

/**
 * 配置模块命令定义
 *
 * @author xiaoyao
 */
public interface ConfigCmd {
    /** 模块主命令 */
    int cmd = 20;

    /** 获取境界配置 */
    int getRealms = 1;
    /** 获取地图配置 */
    int getMaps = 2;
    /** 获取怪物配置 */
    int getMonsters = 3;
    /** 获取物品配置 */
    int getItems = 4;
    /** 获取技能配置 */
    int getSkills = 5;
    /** 获取所有配置 */
    int getAll = 10;
}
