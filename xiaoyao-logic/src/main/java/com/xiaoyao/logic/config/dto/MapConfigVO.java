package com.xiaoyao.logic.config.dto;

import com.xiaoyao.logic.config.MapConfigEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 地图配置 VO (包含关联数据)
 * <p>
 * 用于业务层数据组装，不再混用网络协议注解
 * </p>
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MapConfigVO extends MapConfigEntity {

    /** 关联的怪物 ID 列表 */
    private List<Integer> monsterIds;

    /** 关联的掉落物品 ID 列表 (仅预览用) */
    private List<Integer> drops;
}
