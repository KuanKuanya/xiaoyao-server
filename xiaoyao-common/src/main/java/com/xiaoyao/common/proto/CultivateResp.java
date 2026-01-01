package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 修炼收益响应
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class CultivateResp {
    
    /** 获得的经验值 */
    private long expGain;
    
    /** 当前总经验 */
    private long currentExp;
    
    /** 当前境界ID */
    private int realmId;
    
    /** 挂机时长 (秒) */
    private long duration;
    
    /** 下次可领取时间戳 */
    private long nextCultivateTime;
}
