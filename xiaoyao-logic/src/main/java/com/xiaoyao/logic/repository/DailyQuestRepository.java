package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.DailyQuestEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface DailyQuestRepository extends CrudRepository<DailyQuestEntity, Long> {

        @Query("SELECT * FROM t_daily_quest WHERE player_id = :playerId AND quest_date = :questDate AND is_deleted = 0")
        List<DailyQuestEntity> findByPlayerIdAndDate(@Param("playerId") Long playerId,
                        @Param("questDate") LocalDate questDate);

        @Query("SELECT COUNT(*) FROM t_daily_quest WHERE player_id = :playerId AND quest_date = :questDate AND status >= 1 AND is_deleted = 0")
        Integer countCompletedByDate(@Param("playerId") Long playerId, @Param("questDate") LocalDate questDate);

        @Query("SELECT COUNT(*) FROM t_daily_quest WHERE player_id = :playerId AND quest_date = :questDate AND status = 2 AND is_deleted = 0")
        Integer countClaimedByDate(@Param("playerId") Long playerId, @Param("questDate") LocalDate questDate);

        @Query("SELECT * FROM t_daily_quest WHERE player_id = :playerId AND quest_date = :questDate AND quest_cfg_id = :questCfgId AND is_deleted = 0 LIMIT 1")
        DailyQuestEntity findByQuestCfgId(@Param("playerId") Long playerId, @Param("questDate") LocalDate questDate,
                        @Param("questCfgId") String questCfgId);

        long deleteByQuestDateBefore(LocalDate questDate);
}
