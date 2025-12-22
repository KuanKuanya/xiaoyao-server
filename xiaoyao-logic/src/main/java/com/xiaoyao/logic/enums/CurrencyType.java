package com.xiaoyao.logic.enums;

import lombok.Getter;

/**
 * 货币类型枚举
 *
 * @author xiaoyao
 */
@Getter
public enum CurrencyType {

    /** 灵石 */
    SPIRIT_STONE(1, "灵石"),

    /** 仙玉 (付费货币) */
    JADE(2, "仙玉"),

    /** 绑定仙玉 */
    BIND_JADE(3, "绑定仙玉");

    private final int code;
    private final String desc;

    CurrencyType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static CurrencyType fromCode(int code) {
        for (CurrencyType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的货币类型: " + code);
    }
}
