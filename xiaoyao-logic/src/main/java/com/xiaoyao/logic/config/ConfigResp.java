package com.xiaoyao.logic.config;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.xiaoyao.logic.config.dto.MapConfigVO;
import lombok.Data;

import java.util.List;

/**
 * 配置响应对象
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
public class ConfigResp {
    /** 境界配置列表 */
    private List<RealmConfigEntity> realms;

    /** 地图配置列表 */
    private List<MapConfigVO> maps;

    /** 怪物配置列表 */
    private List<MonsterConfigEntity> monsters;

    /** 物品配置列表 */
    private List<ItemConfigEntity> items;

    /** 技能配置列表 */
    private List<SkillConfigEntity> skills;
}
