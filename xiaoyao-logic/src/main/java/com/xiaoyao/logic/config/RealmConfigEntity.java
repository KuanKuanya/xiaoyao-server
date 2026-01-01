package com.xiaoyao.logic.config;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 境界配置实体
 * 对应表: t_cfg_realm
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_realm")
@ProtobufClass
public class RealmConfigEntity {

    /** 境界ID (从1开始) */
    @Id(keyType = KeyType.None)
    private Integer id;

    /** 境界索引 (用于计算) */
    private Integer realmIndex;

    /** 境界名称 */
    private String name;

    /** 阶段: 初期/中期/后期/圆满 */
    private String stage;

    /** 大境界: 炼气/筑基/金丹等 */
    private String tier;

    /** 基础生命 */
    private Long baseHp;

    /** 基础攻击 */
    private Long baseAtk;

    /** 基础防御 */
    private Long baseDef;

    /** 升级所需经验 */
    private Long maxExp;

    /** 经验倍率 */
    private Double expRate;

    /** 突破成功率(百分比) */
    private Integer breakthroughRate;

    /** 突破消耗灵石 */
    private Long breakthroughCost;

    /** 是否仙界境界 */
    private Boolean isImmortal;

    /** 排序 */
    private Integer sortOrder;

    /** 是否启用 */
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
