package com.xiaoyao.logic.config;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 地图配置实体
 * 对应表: t_cfg_map
 * <p>
 * 纯粹的数据库实体，只关注数据库映射
 * 不再混用网络协议注解
 * </p>
 *
 * @author xiaoyao
 */
@Data
@Table("t_cfg_map")
public class MapConfigEntity {

    /** 地图ID */
    @Id(keyType = KeyType.None)
    private Integer id;

    /** 地图名称 */
    private String name;

    /** 解锁需要的境界ID */
    private Integer reqRealmId;

    /** 推荐境界描述 */
    private String recommendRealm;

    /** 所属区域 */
    private String region;

    /** 子区域 */
    private String subRegion;

    /** 背景图URL */
    private String bgImage;

    /** 地图描述 */
    private String description;

    /** 是否仙界地图 */
    private Boolean isImmortal;

    /** 排序 */
    private Integer sortOrder;

    /** 是否启用 */
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
