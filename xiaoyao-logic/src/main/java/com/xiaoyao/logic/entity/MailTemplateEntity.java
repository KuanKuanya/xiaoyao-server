package com.xiaoyao.logic.entity;


import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 全服邮件模板实体
 * 对应数据库表: t_mail_template
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("t_mail_template")
public class MailTemplateEntity extends BaseEntity {

    /**
     * 模板ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 发件人名称
     */
    private String senderName;

    /**
     * 附件物品 (JSON格式: [{itemId, count}, ...])
     */
    private String attachments;

    /**
     * 目标类型: 1-全服 2-指定玩家 3-条件筛选
     */
    private Integer targetType;

    /**
     * 筛选条件 (JSON格式)
     */
    private String targetCondition;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态: 0-待发送 1-已发送 2-已撤回
     */
    private Integer status;

    /**
     * 实际发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 发送数量
     */
    private Integer sendCount;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
