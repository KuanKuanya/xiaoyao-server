package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志实体
 * 用于记录玩家登录信息，支持留存分析和异常检测
 *
 * @author xiaoyao
 */
@Data
@Table("t_login_log")
public class LoginLogEntity {

    /** 自增主键 */
    @Id
    private Long id;

    /** 玩家ID */
    private Long playerId;

    // ============ 登录信息 ============

    /** 登录类型: 0-正常登录 1-断线重连 2-切换账号 */
    private Integer loginType;

    /** 平台类型: 0-游客 1-微信 2-抖音 3-iOS 4-Android */
    private Integer platformType;

    /** 设备ID */
    private String deviceId;

    /** 设备型号 */
    private String deviceModel;

    /** 系统版本 */
    private String osVersion;

    /** App版本 */
    private String appVersion;

    /** 客户端IP */
    private String clientIp;

    // ============ 时间信息 ============

    /** 登录时间戳(毫秒) */
    private Long loginTime;

    /** 登出时间戳(毫秒) */
    private Long logoutTime;

    /** 在线时长(秒) */
    private Integer onlineDuration;

    // ============ 公共字段 ============
    // 日志表只记录创建时间

    /** 创建时间 */
    @CreatedDate
    private LocalDateTime createdAt;

    // 注意：登录日志表不继承 BaseEntity，因为日志表不需要更新/删除功能
}
