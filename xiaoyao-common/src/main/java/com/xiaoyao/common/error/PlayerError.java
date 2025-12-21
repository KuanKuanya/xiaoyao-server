package com.xiaoyao.common.error;

import com.iohao.net.framework.core.exception.ErrorInformation;
import lombok.Getter;

/**
 * 玩家模块错误码 (02xx)
 *
 * @author xiaoyao
 */
@Getter
public enum PlayerError implements ErrorInformation {
    
    // ==================== 02-01: 登录 ====================
    
    /** 登录失败 */
    LOGIN_FAILED(2010001, "登录失败"),
    
    /** 账号已被封禁 */
    ACCOUNT_BANNED(2010002, "账号已被封禁"),
    
    /** 设备已被封禁 */
    DEVICE_BANNED(2010003, "设备已被封禁"),
    
    /** 玩家不存在 */
    PLAYER_NOT_FOUND(2010004, "玩家不存在"),
    
    // ==================== 02-02: 角色 ====================
    
    /** 昵称包含敏感词 */
    NICKNAME_INVALID(2020001, "昵称包含敏感词"),
    
    /** 昵称过长 */
    NICKNAME_TOO_LONG(2020002, "昵称过长"),
    
    /** 昵称已被使用 */
    NICKNAME_EXISTS(2020003, "昵称已被使用"),
    ;
    
    private final int code;
    private final String message;
    
    PlayerError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
