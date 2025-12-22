package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 技能信息
 */
@Data
public class SkillInfo {
    /** 技能ID */
    private int skillId;
    /** 技能等级 */
    private int level;
    /** 是否已装备 */
    private boolean equipped;
    /** 当前经验 */
    private int exp;
    /** 升级所需经验 */
    private int upgradeExp;
}
