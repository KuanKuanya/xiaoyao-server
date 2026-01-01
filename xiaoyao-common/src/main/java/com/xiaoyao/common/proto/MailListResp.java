package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

import java.util.List;

/**
 * 邮件列表响应
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class MailListResp {
    /** 邮件列表 */
    private List<MailInfo> mails;
    /** 未读数量 */
    private int unreadCount;
    /** 可领取数量 */
    private int claimableCount;
}
