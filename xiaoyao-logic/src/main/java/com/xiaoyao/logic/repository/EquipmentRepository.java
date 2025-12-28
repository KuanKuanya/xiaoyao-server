package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.EquipmentEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EquipmentRepository extends CrudRepository<EquipmentEntity, Long> {
    List<EquipmentEntity> findAllByPlayerId(Long playerId);
}
