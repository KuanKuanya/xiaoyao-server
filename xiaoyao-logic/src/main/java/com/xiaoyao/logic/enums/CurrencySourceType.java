package com.xiaoyao.logic.enums;

import lombok.Getter;

/**
 * 货币来源类型枚举
 *
 * @author xiaoyao
 */
@Getter
public enum CurrencySourceType {

    /** 战斗 */
    COMBAT(1, "战斗"),

    /** 任务 */
    QUEST(2, "任务"),

    /** 商店 */
    SHOP(3, "商店"),

    /** 充值 */
    RECHARGE(4, "充值"),

    /** GM操作 */
    GM(5, "GM操作"),

    /** 邮件 */
    MAIL(6, "邮件"),

    /** 成就 */
    ACHIEVEMENT(7, "成就"),

    /** 宗门俸禄 */
    SECT_SALARY(8, "宗门俸禄"),

    /** 修炼收益 */
    CULTIVATE(9, "修炼收益"),

    /** 装备强化 */
    ENHANCE(10, "装备强化"),

    /** 技能升级 */
    SKILL_UPGRADE(11, "技能升级"),

    /** 系统补偿 */
    COMPENSATION(99, "系统补偿");

    private final int code;
    private final String desc;

    CurrencySourceType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static CurrencySourceType fromCode(int code) {
        for (CurrencySourceType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
