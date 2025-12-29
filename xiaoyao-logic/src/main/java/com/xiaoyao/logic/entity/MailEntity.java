package com.xiaoyao.logic.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 邮件实体
 *
 * @author xiaoyao
 */
@Data
@Table("t_mail")
public class MailEntity extends BaseEntity {

    /** 邮件ID */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /** 玩家ID */
    private Long playerId;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 发送者 */
    private String sender;

    /** 附件 JSON [{itemId, count}] */
    private String attachments;

    /** 是否已读 */
    private Boolean isRead;

    /** 附件是否已领取 */
    private Boolean isClaimed;

    /** 过期时间 */
    private LocalDateTime expireTime;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
