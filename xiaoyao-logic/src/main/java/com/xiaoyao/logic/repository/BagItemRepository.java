package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.BagItemEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BagItemRepository extends CrudRepository<BagItemEntity, Long> {

    // Equivalent to selectByPlayerId order by slot
    List<BagItemEntity> findAllByPlayerIdOrderBySlotAsc(Long playerId);
}
