package com.xiaoyao.common.error;

import com.iohao.game.action.skeleton.core.exception.MsgExceptionInfo;
import lombok.Getter;

/**
 * 系统级错误码 (01xx)
 *
 * @author xiaoyao
 */
@Getter
public enum SystemError implements ErrorCode, MsgExceptionInfo {
    
    /** 成功 */
    SUCCESS(0, "成功"),
    
    // ==================== 01-01: 通用错误 ====================
    
    /** 系统繁忙 */
    SYSTEM_ERROR(1010001, "系统繁忙，请稍后重试"),
    
    /** 参数错误 */
    PARAM_INVALID(1010002, "参数错误"),
    
    /** 操作过于频繁 */
    FREQ_LIMIT(1010003, "操作过于频繁"),
    
    /** 系统维护中 */
    MAINTENANCE(1010004, "系统维护中"),
    
    /** 版本过低 */
    VERSION_TOO_LOW(1010005, "版本过低，请更新"),
    
    // ==================== 01-02: 网络错误 ====================
    
    /** 连接超时 */
    CONNECT_TIMEOUT(1020001, "连接超时"),
    
    /** Token过期 */
    TOKEN_EXPIRED(1020002, "登录已过期，请重新登录"),
    
    /** Token无效 */
    TOKEN_INVALID(1020003, "登录信息无效"),
    
    /** 被踢下线 */
    KICKED_OFF(1020004, "您的账号已在其他设备登录"),
    ;
    
    private final int code;
    private final String message;
    
    SystemError(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public String getMsg() {
        return this.message;
    }
}
