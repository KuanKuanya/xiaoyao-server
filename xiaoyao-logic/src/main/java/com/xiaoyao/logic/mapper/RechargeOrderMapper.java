package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xiaoyao.logic.entity.RechargeOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 充值订单 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface RechargeOrderMapper extends BaseMapper<RechargeOrderEntity> {

    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单实体
     */
    @Select("SELECT * FROM t_recharge_order WHERE order_no = #{orderNo} AND is_deleted = 0")
    RechargeOrderEntity selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据平台订单号查询订单
     *
     * @param platformOrderNo 平台订单号
     * @return 订单实体
     */
    @Select("SELECT * FROM t_recharge_order WHERE platform_order_no = #{platformOrderNo} AND is_deleted = 0")
    RechargeOrderEntity selectByPlatformOrderNo(@Param("platformOrderNo") String platformOrderNo);

    /**
     * 查询玩家的充值订单列表
     *
     * @param playerId 玩家ID
     * @param limit 限制条数
     * @return 订单列表 (按创建时间倒序)
     */
    @Select("SELECT * FROM t_recharge_order " +
            "WHERE player_id = #{playerId} AND is_deleted = 0 " +
            "ORDER BY created_at DESC " +
            "LIMIT #{limit}")
    List<RechargeOrderEntity> selectByPlayerId(@Param("playerId") Long playerId, @Param("limit") Integer limit);

    /**
     * 统计玩家总充值金额
     *
     * @param playerId 玩家ID
     * @return 总充值金额(分)
     */
    @Select("SELECT COALESCE(SUM(pay_amount), 0) FROM t_recharge_order " +
            "WHERE player_id = #{playerId} AND status = 3 AND is_deleted = 0")
    Long sumTotalRecharge(@Param("playerId") Long playerId);

    /**
     * 查询待发货的订单列表
     *
     * @param limit 限制条数
     * @return 订单列表
     */
    @Select("SELECT * FROM t_recharge_order " +
            "WHERE status = 2 AND is_deleted = 0 " +
            "ORDER BY pay_time ASC " +
            "LIMIT #{limit}")
    List<RechargeOrderEntity> selectPendingDeliver(@Param("limit") Integer limit);
}
