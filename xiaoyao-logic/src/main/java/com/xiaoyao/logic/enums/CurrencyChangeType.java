package com.xiaoyao.logic.enums;

import lombok.Getter;

/**
 * 货币变动类型枚举
 *
 * @author xiaoyao
 */
@Getter
public enum CurrencyChangeType {

    /** 增加 */
    ADD(1, "增加"),

    /** 减少 */
    REDUCE(2, "减少");

    private final int code;
    private final String desc;

    CurrencyChangeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
