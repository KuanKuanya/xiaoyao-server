package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.SectMemberEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface SectMemberRepository extends CrudRepository<SectMemberEntity, Long> {

    Optional<SectMemberEntity> findFirstByPlayerIdAndIsDeleted(long playerId, int isDeleted);

    List<SectMemberEntity> findBySectIdAndIsDeletedOrderByPositionDescContributionDesc(long sectId, int isDeleted);

    List<SectMemberEntity> findBySectIdAndIsDeletedOrderByContributionDesc(long sectId, int isDeleted,
            Pageable pageable);

    long countBySectIdAndIsDeleted(long sectId, int isDeleted);

    // resetWeeklyContribution removed, logic moved to service
}
