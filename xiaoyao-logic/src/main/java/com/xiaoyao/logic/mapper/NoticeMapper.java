package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xiaoyao.logic.entity.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface NoticeMapper extends BaseMapper<NoticeEntity> {

    /**
     * 查询当前有效的公告（在有效期内）
     *
     * @param now 当前时间
     * @return 公告列表（按优先级降序）
     */
    @Select("SELECT * FROM t_notice WHERE start_time <= #{now} AND end_time >= #{now} AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> selectActiveNotices(@Param("now") LocalDateTime now);

    /**
     * 查询指定平台的有效公告
     *
     * @param now 当前时间
     * @param platform 平台类型（如 'wechat', 'douyin', 'ios', 'android'）
     * @return 公告列表
     */
    @Select("SELECT * FROM t_notice WHERE start_time <= #{now} AND end_time >= #{now} " +
            "AND (platforms = 'all' OR platforms LIKE CONCAT('%', #{platform}, '%')) " +
            "AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> selectActiveNoticesByPlatform(@Param("now") LocalDateTime now,
                                                      @Param("platform") String platform);

    /**
     * 查询弹窗公告
     *
     * @param now 当前时间
     * @return 公告列表
     */
    @Select("SELECT * FROM t_notice WHERE start_time <= #{now} AND end_time >= #{now} " +
            "AND is_popup = 1 AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> selectPopupNotices(@Param("now") LocalDateTime now);

    /**
     * 查询指定类型的公告
     *
     * @param noticeType 公告类型
     * @return 公告列表
     */
    @Select("SELECT * FROM t_notice WHERE notice_type = #{noticeType} AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> selectByType(@Param("noticeType") Integer noticeType);

    /**
     * 查询强制阅读的有效公告
     *
     * @param now 当前时间
     * @return 公告列表
     */
    @Select("SELECT * FROM t_notice WHERE start_time <= #{now} AND end_time >= #{now} " +
            "AND is_force_read = 1 AND is_deleted = 0 ORDER BY priority DESC, created_at DESC")
    List<NoticeEntity> selectForceReadNotices(@Param("now") LocalDateTime now);
}
