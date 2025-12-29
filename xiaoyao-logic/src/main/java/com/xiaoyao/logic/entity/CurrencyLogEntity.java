package com.xiaoyao.logic.entity;

import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 货币变动日志实体
 * 用于记录玩家货币的所有变动，支持对账和审计
 *
 * @author xiaoyao
 */
@Data
@Table("t_currency_log")
public class CurrencyLogEntity {

    /** 自增主键 */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /** 玩家ID */
    private Long playerId;

    // ============ 货币信息 ============

    /** 货币类型: 1-灵石 2-仙玉 3-绑定仙玉 */
    private Integer currencyType;

    /** 变动类型: 1-增加 2-减少 */
    private Integer changeType;

    /** 变动数量 */
    private Long changeAmount;

    /** 变动前数量 */
    private Long beforeAmount;

    /** 变动后数量 */
    private Long afterAmount;

    // ============ 来源信息 ============

    /** 来源类型: 1-战斗 2-任务 3-商店 4-充值 5-GM 6-邮件 7-成就 */
    private Integer sourceType;

    /** 来源ID (如任务ID、商品ID等) */
    private String sourceId;

    /** 备注说明 */
    private String remark;

    // ============ 公共字段 ============
    // 日志表只记录创建信息，不需要更新和删除

    /** 操作人ID (玩家ID或GM ID) */
    private Long createdBy;

    /** 创建时间 */
    private LocalDateTime createdAt;

    // 注意：货币日志表不继承 BaseEntity，因为日志表不需要更新/删除功能
}
