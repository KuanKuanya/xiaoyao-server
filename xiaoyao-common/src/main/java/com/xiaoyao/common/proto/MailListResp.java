package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 邮件列表响应
 */
@Data
@ProtobufClass
public class MailListResp {
    /** 邮件列表 */
    private List<MailInfo> mails;
    /** 未读数量 */
    private int unreadCount;
    /** 可领取数量 */
    private int claimableCount;
}
