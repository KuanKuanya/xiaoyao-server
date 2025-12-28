package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.IdleBattleEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IdleBattleRepository extends CrudRepository<IdleBattleEntity, Long> {

    @Query("SELECT * FROM t_idle_battle WHERE player_id = :playerId AND is_active = 1 LIMIT 1")
    IdleBattleEntity findActiveByPlayerId(@Param("playerId") Long playerId);
}
