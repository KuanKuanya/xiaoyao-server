package com.xiaoyao.logic.config;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地图怪物关联配置实体
 * 对应表: t_cfg_map_monster
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_map_monster")
public class MapMonsterConfigEntity {

    /** 自增ID */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /** 地图ID */
    private Integer mapId;

    /** 怪物ID */
    private Integer monsterId;

    /** 刷新权重 */
    private Integer spawnRate;

    /** 是否为地图BOSS */
    private Boolean isBoss;

    private LocalDateTime createdAt;
}
