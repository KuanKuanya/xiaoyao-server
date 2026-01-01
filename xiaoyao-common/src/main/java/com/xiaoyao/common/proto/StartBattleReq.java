package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 开始战斗请求
 */
@Data
@ProtobufClass
public class StartBattleReq {
    /** 地图ID */
    private int mapId;
}
