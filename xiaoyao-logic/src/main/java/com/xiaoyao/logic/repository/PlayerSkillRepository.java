package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.PlayerSkillEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PlayerSkillRepository extends CrudRepository<PlayerSkillEntity, Long> {
    List<PlayerSkillEntity> findByPlayerIdOrderByLevelDesc(Long playerId);
}
