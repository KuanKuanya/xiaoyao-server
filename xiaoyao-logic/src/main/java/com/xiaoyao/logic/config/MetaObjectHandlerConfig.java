package com.xiaoyao.logic.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 字段自动填充处理器
 * 自动填充公共字段：created_at, updated_at, created_by, updated_by
 *
 * @author xiaoyao
 */
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();

        // 填充创建时间
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);

        // 填充更新时间
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);

        // TODO: 从当前上下文获取操作人ID (玩家ID或GM ID)
        // 填充创建人 (示例：从 FlowContext 获取当前玩家ID)
        // Long userId = getCurrentUserId();
        // this.strictInsertFill(metaObject, "createdBy", Long.class, userId);
        // this.strictInsertFill(metaObject, "updatedBy", Long.class, userId);
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充更新时间
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());

        // TODO: 填充更新人
        // Long userId = getCurrentUserId();
        // this.strictUpdateFill(metaObject, "updatedBy", Long.class, userId);
    }

    /**
     * 获取当前操作用户ID
     * TODO: 实现从 ionet FlowContext 或 ThreadLocal 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // 示例实现：
        // FlowContext ctx = FlowContextHolder.get();
        // return ctx != null ? ctx.getUserId() : 0L;
        return 0L;
    }
}
