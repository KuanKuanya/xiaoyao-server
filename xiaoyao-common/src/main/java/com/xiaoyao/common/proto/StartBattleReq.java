package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 开始战斗请求
 */
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class StartBattleReq {
    /** 地图ID */
    @Protobuf(order = 1, description = "地图ID")
    public int mapId;

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
