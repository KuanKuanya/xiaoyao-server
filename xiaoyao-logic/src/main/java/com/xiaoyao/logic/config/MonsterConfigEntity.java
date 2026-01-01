package com.xiaoyao.logic.config;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 怪物配置实体
 * 对应表: t_cfg_monster
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_monster")
@ProtobufClass
public class MonsterConfigEntity {

    /** 怪物ID */
    @Id(keyType = KeyType.None)
    private Integer id;

    /** 怪物名称 */
    private String name;

    /** 类型: 1普通 2精英 3领主 4世界BOSS */
    private Integer monsterType;

    /** 怪物境界等级 */
    private Integer realmLevel;

    /** 基础生命值 */
    private Long baseHp;

    /** 基础攻击 */
    private Long baseAtk;

    /** 基础防御 */
    private Long baseDef;

    /** 经验奖励 */
    private Long expReward;

    /** 灵石奖励 */
    private Long stoneReward;

    /** 怪物图标(emoji) */
    private String icon;

    /** 怪物头像URL */
    private String avatarUrl;

    /** 怪物描述 */
    private String description;

    /** 是否启用 */
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
