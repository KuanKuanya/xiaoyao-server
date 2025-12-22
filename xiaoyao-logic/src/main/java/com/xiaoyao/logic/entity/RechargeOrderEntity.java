package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 充值订单实体
 * 用于记录玩家充值订单，支持支付对账和收入统计
 *
 * @author xiaoyao
 */
@Data
@TableName("t_recharge_order")
public class RechargeOrderEntity extends BaseEntity {

    /** 订单ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单号 (唯一) */
    private String orderNo;

    // ============ 玩家信息 ============

    /** 玩家ID */
    private Long playerId;

    // ============ 商品信息 ============

    /** 商品ID */
    private String productId;

    /** 商品名称 */
    private String productName;

    /** 商品价格(分) */
    private Integer productPrice;

    /** 仙玉数量 */
    private Integer jadeAmount;

    /** 赠送仙玉 */
    private Integer bonusJade;

    // ============ 支付信息 ============

    /** 平台类型: 1-微信 2-抖音 3-苹果 4-安卓 */
    private Integer platformType;

    /** 平台订单号 */
    private String platformOrderNo;

    /** 实际支付金额(分) */
    private Integer payAmount;

    /** 支付渠道 */
    private String payChannel;

    // ============ 订单状态 ============

    /** 状态: 0-创建 1-支付中 2-支付成功 3-已发货 4-支付失败 5-已退款 */
    private Integer status;

    /** 支付时间戳(毫秒) */
    private Long payTime;

    /** 发货时间戳(毫秒) */
    private Long deliverTime;

    // ============ 客户端信息 ============

    /** 客户端IP */
    private String clientIp;

    /** 设备ID */
    private String deviceId;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
