package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.CurrencyLogEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurrencyLogRepository extends CrudRepository<CurrencyLogEntity, Long> {

    // Dynamic query replacement: use :currencyType IS NULL check to simulate
    // optional parameter
    @Query("SELECT * FROM t_currency_log WHERE player_id = :playerId AND (:currencyType IS NULL OR currency_type = :currencyType) ORDER BY created_at DESC LIMIT :limit")
    List<CurrencyLogEntity> findRecentLogs(@Param("playerId") Long playerId,
            @Param("currencyType") Integer currencyType, @Param("limit") Integer limit);

    @Query("SELECT COALESCE(SUM(change_amount), 0) FROM t_currency_log WHERE player_id = :playerId AND currency_type = :currencyType AND change_type = 1")
    Long sumIncome(@Param("playerId") Long playerId, @Param("currencyType") Integer currencyType);

    @Query("SELECT COALESCE(SUM(change_amount), 0) FROM t_currency_log WHERE player_id = :playerId AND currency_type = :currencyType AND change_type = 2")
    Long sumExpense(@Param("playerId") Long playerId, @Param("currencyType") Integer currencyType);
}
