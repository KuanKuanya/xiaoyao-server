package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.PlayerStatsEntity;
import org.springframework.data.repository.CrudRepository;

public interface PlayerStatsRepository extends CrudRepository<PlayerStatsEntity, Long> {
}
