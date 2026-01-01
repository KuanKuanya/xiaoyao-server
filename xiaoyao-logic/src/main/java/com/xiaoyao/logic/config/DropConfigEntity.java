package com.xiaoyao.logic.config;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 掉落配置实体
 * 对应表: t_cfg_drop
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_drop")
public class DropConfigEntity {

    /** 自增ID */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /** 来源类型: 1地图 2怪物 3BOSS */
    private Integer sourceType;

    /** 来源ID */
    private Integer sourceId;

    /** 物品ID */
    private Integer itemId;

    /** 掉落概率(万分比, 10000=100%) */
    private Integer dropRate;

    /** 最小数量 */
    private Integer minCount;

    /** 最大数量 */
    private Integer maxCount;

    private LocalDateTime createdAt;
}
