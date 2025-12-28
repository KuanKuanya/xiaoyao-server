package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 玩家账户实体
 * 对应表: t_player
 * 只包含账户核心信息，货币在 t_player_currency，进度在 t_player_progress
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_player")
public class PlayerEntity extends BaseEntity {

    /** 玩家ID (雪花算法) */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 设备ID */
    private String deviceId;

    /** 平台类型 0=游客 1=微信 2=抖音 3=苹果 4=安卓 */
    private Integer platform;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** VIP等级 */
    private Integer vipLevel;

    /** 当前佩戴的称号ID */
    private Integer currentTitleId;

    /** 上次登录时间 */
    private LocalDateTime lastLoginTime;
}
