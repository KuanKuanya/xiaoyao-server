package com.xiaoyao.common.error;

import com.iohao.net.framework.core.exception.ErrorInformation;
import lombok.Getter;

/**
 * 境界模块错误码 (11xx)
 *
 * @author xiaoyao
 */
@Getter
public enum RealmError implements ErrorInformation {
    
    // ==================== 11-01: 修炼 ====================
    
    /** 经验不足 */
    EXP_NOT_ENOUGH(11010001, "经验不足"),
    
    /** 已达最高境界 */
    REALM_MAX(11010002, "已达最高境界"),
    
    /** 修炼冷却中 */
    CULTIVATE_CD(11010003, "修炼冷却中"),
    
    // ==================== 11-02: 突破 ====================
    
    /** 突破材料不足 */
    BREAKTHROUGH_ITEM_NOT_ENOUGH(11020001, "突破材料不足"),
    
    /** 突破失败 */
    BREAKTHROUGH_FAILED(11020002, "突破失败"),
    
    /** 需要先飞升 */
    NEED_ASCEND_FIRST(11020003, "需要先飞升"),
    
    /** 境界经验未满 */
    EXP_NOT_FULL(11020004, "境界经验未满，无法突破"),
    ;
    
    private final int code;
    private final String message;
    
    RealmError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
