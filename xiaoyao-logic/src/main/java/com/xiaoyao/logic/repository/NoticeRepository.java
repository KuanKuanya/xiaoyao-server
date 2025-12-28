package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.NoticeEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends CrudRepository<NoticeEntity, Long> {

    @Query("SELECT * FROM t_notice WHERE start_time <= :now AND end_time >= :now AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> findActiveNotices(@Param("now") LocalDateTime now);

    @Query("SELECT * FROM t_notice WHERE start_time <= :now AND end_time >= :now AND (platforms = 'all' OR platforms LIKE CONCAT('%', :platform, '%')) AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> findActiveNoticesByPlatform(@Param("now") LocalDateTime now, @Param("platform") String platform);

    @Query("SELECT * FROM t_notice WHERE start_time <= :now AND end_time >= :now AND is_popup = 1 AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> findPopupNotices(@Param("now") LocalDateTime now);

    @Query("SELECT * FROM t_notice WHERE notice_type = :noticeType AND is_deleted = 0 ORDER BY priority DESC")
    List<NoticeEntity> findByType(@Param("noticeType") Integer noticeType);

    List<NoticeEntity> findByIsDeletedOrderByPriorityDescCreatedAtDesc(Integer isDeleted);

    List<NoticeEntity> findAllByOrderByPriorityDescCreatedAtDesc();

    @Query("SELECT * FROM t_notice WHERE start_time <= :now AND end_time >= :now AND is_force_read = 1 AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> findForceReadNotices(@Param("now") LocalDateTime now);
}
