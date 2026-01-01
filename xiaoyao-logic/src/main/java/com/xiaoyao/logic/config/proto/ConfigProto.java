package com.xiaoyao.logic.config.proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 配置响应协议对象
 * <p>
 * 用于网络传输的配置数据响应
 * 替代原来的 ConfigResp（Entity 混用）
 * </p>
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
public class ConfigProto {

    /** 地图配置列表 */
    @Protobuf(order = 1)
    private List<MapConfigProto> maps;

    // TODO: 后续添加其他配置类型的 Proto 对象
    // private List<RealmConfigProto> realms;
    // private List<MonsterConfigProto> monsters;
    // private List<ItemConfigProto> items;
    // private List<SkillConfigProto> skills;
}
