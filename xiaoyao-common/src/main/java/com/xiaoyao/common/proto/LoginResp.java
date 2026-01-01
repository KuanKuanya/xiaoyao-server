package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 登录响应
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class LoginResp {
    
    /** 玩家ID */
    private long playerId;
    
    /** 会话Token */
    private String token;
    
    /** 是否新玩家 */
    private boolean isNew;
    
    /** 玩家基础数据 */
    private PlayerData playerData;
    
    /** 服务器时间戳 (毫秒) */
    private long serverTime;
}
