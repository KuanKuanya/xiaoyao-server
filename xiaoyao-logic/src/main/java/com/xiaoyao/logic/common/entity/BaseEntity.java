package com.xiaoyao.logic.common.entity;

import com.mybatisflex.annotation.Column;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实体类公共基类
 * 包含所有表的公共字段
 *
 * @author xiaoyao
 */
@Data
public abstract class BaseEntity {

    /**
     * 乐观锁版本号
     * 用于防止并发更新冲突
     */
    @Column(value = "version", version = true)
    private Integer version;

    /**
     * 逻辑删除标志
     * 0 = 正常, 1 = 已删除
     */
    @Column(value = "is_deleted", isLogicDelete = true)
    private Integer isDeleted;

    /**
     * 创建人ID
     * 玩家操作时为玩家ID，系统/GM操作时为操作者ID
     * 注意: 需要在Service层手动设置
     */
    @Column(value = "created_by")
    private Long createdBy;

    /**
     * 创建时间
     * 自动填充为数据库当前时间
     */
    @Column(value = "created_at", onInsertValue = "now()")
    private LocalDateTime createdAt;

    /**
     * 更新人ID
     * 注意: 需要在Service层手动设置
     */
    @Column(value = "updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     * 插入和更新时自动填充为数据库当前时间
     */
    @Column(value = "updated_at", onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updatedAt;

    /**
     * 删除人ID
     * 主动删除时记录操作者，系统删除时为0
     */
    @Column(value = "deleted_by")
    private Long deletedBy;

    /**
     * 删除时间
     */
    @Column(value = "deleted_at")
    private LocalDateTime deletedAt;
}
