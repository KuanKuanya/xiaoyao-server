package com.xiaoyao.logic.config.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 地图配置协议对象
 * <p>
 * 专门用于网络传输，包含客户端需要的地图配置信息
 * </p>
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
public class MapConfigProto {

    /** 地图ID */
    @Protobuf(order = 1)
    private int mapId;

    /** 地图名称 */
    @Protobuf(order = 2)
    private String name;

    /** 解锁需要的境界ID */
    @Protobuf(order = 3)
    private int reqRealmId;

    /** 推荐境界描述 */
    @Protobuf(order = 4)
    private String recommendRealm;

    /** 所属区域 */
    @Protobuf(order = 5)
    private String region;

    /** 子区域 */
    @Protobuf(order = 6)
    private String subRegion;

    /** 背景图URL */
    @Protobuf(order = 7)
    private String bgImage;

    /** 地图描述 */
    @Protobuf(order = 8)
    private String description;

    /** 是否仙界地图 */
    @Protobuf(order = 9)
    private boolean isImmortal;

    /** 排序 */
    @Protobuf(order = 10)
    private int sortOrder;

    /** 关联的怪物ID列表 */
    @Protobuf(order = 11)
    private List<Integer> monsterIds;

    /** 关联的掉落物品ID列表 */
    @Protobuf(order = 12)
    private List<Integer> drops;
}
