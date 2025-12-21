package com.xiaoyao.common.error;

/**
 * 错误码接口
 *
 * @author xiaoyao
 */
public interface ErrorCode {
    
    /**
     * 获取错误码
     */
    int getCode();
    
    /**
     * 获取错误信息
     */
    String getMessage();
}
