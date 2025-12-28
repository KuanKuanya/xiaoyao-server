package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.RechargeOrderEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RechargeOrderRepository extends CrudRepository<RechargeOrderEntity, Long> {

    @Query("SELECT * FROM t_recharge_order WHERE order_no = :orderNo AND is_deleted = 0")
    RechargeOrderEntity findByOrderNo(@Param("orderNo") String orderNo);

    @Query("SELECT * FROM t_recharge_order WHERE platform_order_no = :platformOrderNo AND is_deleted = 0")
    RechargeOrderEntity findByPlatformOrderNo(@Param("platformOrderNo") String platformOrderNo);

    @Query("SELECT * FROM t_recharge_order WHERE player_id = :playerId AND is_deleted = 0 ORDER BY created_at DESC LIMIT :limit")
    List<RechargeOrderEntity> findByPlayerId(@Param("playerId") Long playerId, @Param("limit") Integer limit);

    @Query("SELECT COALESCE(SUM(pay_amount), 0) FROM t_recharge_order WHERE player_id = :playerId AND status = 3 AND is_deleted = 0")
    Long sumTotalRecharge(@Param("playerId") Long playerId);

    @Query("SELECT * FROM t_recharge_order WHERE status = 2 AND is_deleted = 0 ORDER BY pay_time ASC LIMIT :limit")
    List<RechargeOrderEntity> findPendingDeliver(@Param("limit") Integer limit);
}
