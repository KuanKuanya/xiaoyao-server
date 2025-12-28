package com.xiaoyao.logic.entity;

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
     */
    @org.springframework.data.annotation.Version
    private Integer version;

    /**
     * 逻辑删除标志
     * 0 = 正常, 1 = 已删除
     * 注意：Spring Data JDBC 默认不支持逻辑删除自动过滤，需在查询中手动处理
     */
    private Integer isDeleted;

    /**
     * 创建人ID
     */
    @org.springframework.data.annotation.CreatedBy
    private Long createdBy;

    /**
     * 创建时间
     */
    @org.springframework.data.annotation.CreatedDate
    private LocalDateTime createdAt;

    /**
     * 更新人ID
     */
    @org.springframework.data.annotation.LastModifiedBy
    private Long updatedBy;

    /**
     * 更新时间
     */
    @org.springframework.data.annotation.LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 删除人ID
     */
    private Long deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedAt;
}
