package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 玩家实体
 *
 * @author xiaoyao
 */
@Data
@TableName("t_player")
public class PlayerEntity extends BaseEntity {

    /** 玩家ID (雪花算法) */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 设备ID */
    private String deviceId;

    /** 平台类型 0=游客 1=微信 2=抖音 */
    private Integer platform;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 境界ID */
    private Integer realmId;

    /** 当前境界经验 */
    private Long realmExp;

    /** 是否已飞升 */
    private Boolean ascended;

    /** 灵石 */
    private Long spiritStone;

    /** 仙玉 */
    private Integer jade;

    /** 绑定仙玉 */
    private Integer bindJade;

    /** VIP等级 */
    private Integer vipLevel;

    /** 战斗力 */
    private Long combatPower;

    /** 上次领取修炼时间 */
    private Long lastCultivateTime;

    /** 上次登录时间 */
    private LocalDateTime lastLoginTime;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
