package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

import java.util.List;

/**
 * 排行榜响应
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class RankListResp {
    /** 排行榜类型 */
    private int rankType;
    /** 排行榜列表 */
    private List<RankEntry> list;
    /** 我的排名 */
    private RankEntry myRank;
    /** 更新时间 */
    private long updateTime;
}
