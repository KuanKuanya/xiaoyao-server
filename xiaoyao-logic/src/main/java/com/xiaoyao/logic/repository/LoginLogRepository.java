package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.LoginLogEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoginLogRepository extends CrudRepository<LoginLogEntity, Long> {

    @Query("SELECT * FROM t_login_log WHERE player_id = :playerId ORDER BY login_time DESC LIMIT :limit")
    List<LoginLogEntity> findRecentLogins(@Param("playerId") Long playerId, @Param("limit") Integer limit);

    @Query("SELECT * FROM t_login_log WHERE player_id = :playerId ORDER BY login_time DESC LIMIT 1")
    LoginLogEntity findLastLogin(@Param("playerId") Long playerId);

    @Modifying
    @Query("UPDATE t_login_log SET logout_time = :logoutTime, online_duration = :onlineDuration WHERE id = :id")
    int updateLogout(@Param("id") Long id, @Param("logoutTime") Long logoutTime,
            @Param("onlineDuration") Integer onlineDuration);

    @Query("SELECT COUNT(DISTINCT player_id) FROM t_login_log WHERE login_time >= :startTime AND login_time < :endTime")
    Long countDAU(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
