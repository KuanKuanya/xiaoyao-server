package com.xiaoyao.common.cmd;

/**
 * 技能模块命令
 *
 * @author xiaoyao
 */
public interface SkillCmd {
    
    /** 主命令 */
    int cmd = CmdModule.SKILL;
    
    /** 获取技能列表 */
    int getList = 1;
    
    /** 学习技能 */
    int learn = 2;
    
    /** 升级技能 */
    int upgrade = 3;
    
    /** 装备技能 */
    int equip = 4;
    
    /** 卸下技能 */
    int unequip = 5;
    
    /** 获取技能详情 */
    int getDetail = 6;
}
