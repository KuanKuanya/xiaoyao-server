package com.xiaoyao.logic.exception;

import lombok.Getter;

/**
 * 业务异常基类
 * 用于所有可预期的业务错误场景
 *
 * @author xiaoyao
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int errorCode;

    /**
     * 错误消息
     */
    private final String errorMessage;

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param errorMessage 错误消息
     */
    public BusinessException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 构造函数（带原始异常）
     *
     * @param errorCode 错误码
     * @param errorMessage 错误消息
     * @param cause 原始异常
     */
    public BusinessException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 便捷工厂方法
     */
    public static BusinessException of(int errorCode, String errorMessage) {
        return new BusinessException(errorCode, errorMessage);
    }

    /**
     * 便捷工厂方法（格式化消息）
     */
    public static BusinessException of(int errorCode, String messageFormat, Object... args) {
        return new BusinessException(errorCode, String.format(messageFormat, args));
    }

    @Override
    public String toString() {
        return String.format("BusinessException[code=%d, message=%s]", errorCode, errorMessage);
    }
}
