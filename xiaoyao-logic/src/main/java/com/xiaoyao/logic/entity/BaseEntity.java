package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
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
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 逻辑删除标志
     * 0 = 正常, 1 = 已删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建人ID
     * 玩家操作时为玩家ID，系统/GM操作时为操作者ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除人ID
     * 主动删除时记录操作者，系统删除时为0
     */
    @TableField(value = "deleted_by")
    private Long deletedBy;

    /**
     * 删除时间
     */
    @TableField(value = "deleted_at")
    private LocalDateTime deletedAt;
}
