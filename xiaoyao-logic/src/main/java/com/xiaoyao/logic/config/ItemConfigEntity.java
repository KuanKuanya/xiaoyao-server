package com.xiaoyao.logic.config;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物品配置实体
 * 对应表: t_cfg_item
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_item")
@ProtobufClass
public class ItemConfigEntity {

    /** 物品ID */
    @Id(keyType = KeyType.None)
    private Integer id;

    /** 物品名称 */
    private String name;

    /** 类型: 1装备 2丹药 3材料 4功法书 5宠物蛋 6消耗品 */
    private Integer itemType;

    /** 品质: 1白 2绿 3蓝 4紫 5橙 6红 7金 */
    private Integer quality;

    /** 图标(emoji) */
    private String icon;

    /** 图标URL */
    private String iconUrl;

    /** 物品描述 */
    private String description;

    /** 基础价值(售卖价) */
    private Integer value;

    /** 最大堆叠数 */
    private Integer maxStack;

    /** 绑定类型: 0不绑 1装备绑 2拾取绑 */
    private Integer bindType;

    /** 装备槽位: WEAPON/ARMOR/HEAD/FEET/ARTIFACT */
    private String equipSlot;

    /** 装备需求境界 */
    private Integer reqRealmId;

    /** 属性 JSON {atk, def, hp, speed, ...} */
    private String stats;

    /** 使用效果 JSON {type, value} */
    private String useEffect;

    /** 功法书关联技能ID */
    private Integer skillId;

    /** 是否启用 */
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
