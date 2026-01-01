package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 登录请求
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class LoginReq {
    
    /** 平台类型: 0-游客 1-微信 2-抖音 */
    private int platform;
    
    /** 平台授权 code */
    private String code;
    
    /** 设备ID */
    private String deviceId;
    
    /** 客户端版本号 */
    private String clientVersion;
    
    /** 协议版本号 */
    private int protocolVersion;
}
