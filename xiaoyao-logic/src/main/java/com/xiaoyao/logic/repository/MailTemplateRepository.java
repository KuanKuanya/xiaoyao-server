package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.MailTemplateEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface MailTemplateRepository extends CrudRepository<MailTemplateEntity, Long> {

    @Query("SELECT * FROM t_mail_template WHERE status = 0 AND is_deleted = 0 ORDER BY created_at ASC")
    List<MailTemplateEntity> findPendingTemplates();

    @Query("SELECT * FROM t_mail_template WHERE status = 1 AND start_time <= :now AND end_time >= :now AND is_deleted = 0")
    List<MailTemplateEntity> findActiveTemplates(@Param("now") LocalDateTime now);

    @Query("SELECT * FROM t_mail_template WHERE created_at BETWEEN :startTime AND :endTime AND is_deleted = 0 ORDER BY created_at DESC")
    List<MailTemplateEntity> findByTimeRange(@Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<MailTemplateEntity> findByIsDeletedOrderByCreatedAtDesc(Integer isDeleted);

    List<MailTemplateEntity> findAllByOrderByCreatedAtDesc();

    @Query("SELECT * FROM t_mail_template WHERE target_type = :targetType AND is_deleted = 0 ORDER BY created_at DESC")
    List<MailTemplateEntity> findByTargetType(@Param("targetType") Integer targetType);
}
