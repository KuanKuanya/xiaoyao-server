package com.xiaoyao.common.proto;

import lombok.Data;
import java.util.List;

/**
 * 邮件信息
 */
@Data
public class MailInfo {
    /** 邮件ID */
    private long mailId;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 发送者 */
    private String sender;
    /** 是否已读 */
    private boolean read;
    /** 是否有附件 */
    private boolean hasAttachment;
    /** 附件是否已领取 */
    private boolean claimed;
    /** 附件列表 */
    private List<BagItem> attachments;
    /** 发送时间 */
    private long sendTime;
    /** 过期时间 */
    private long expireTime;
}
