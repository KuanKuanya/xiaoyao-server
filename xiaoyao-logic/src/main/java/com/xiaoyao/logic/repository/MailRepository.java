package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.MailEntity;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MailRepository extends CrudRepository<MailEntity, Long> {
    List<MailEntity> findByPlayerIdAndExpireTimeAfterOrderByCreatedAtDesc(Long playerId, LocalDateTime now);
}
