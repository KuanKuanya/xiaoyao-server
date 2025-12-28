package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.AchievementEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface AchievementRepository extends CrudRepository<AchievementEntity, Long> {

    List<AchievementEntity> findByPlayerIdAndIsDeletedOrderByStatusAscCompleteTimeDesc(long playerId, int isDeleted);

    Optional<AchievementEntity> findFirstByPlayerIdAndAchievementCfgIdAndIsDeleted(long playerId, int achievementCfgId,
            int isDeleted);

    List<AchievementEntity> findByPlayerIdAndStatusAndIsDeletedOrderByCompleteTimeAsc(long playerId, int status,
            int isDeleted);

    long countByPlayerIdAndStatusGreaterThanEqualAndIsDeleted(long playerId, int status, int isDeleted);

    long countByPlayerIdAndStatusAndIsDeleted(long playerId, int status, int isDeleted);
}
