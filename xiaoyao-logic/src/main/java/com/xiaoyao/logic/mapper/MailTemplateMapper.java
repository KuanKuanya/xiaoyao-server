package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.MailTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 全服邮件模板Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface MailTemplateMapper extends BaseMapper<MailTemplateEntity> {

    /**
     * 查询待发送的邮件模板
     *
     * @return 邮件模板列表
     */
    @Select("SELECT * FROM t_mail_template WHERE status = 0 AND is_deleted = 0 ORDER BY created_at ASC")
    List<MailTemplateEntity> selectPendingTemplates();

    /**
     * 查询当前有效的邮件模板（在有效期内且已发送）
     *
     * @param now 当前时间
     * @return 邮件模板列表
     */
    @Select("SELECT * FROM t_mail_template WHERE status = 1 AND start_time <= #{now} AND end_time >= #{now} AND is_deleted = 0")
    List<MailTemplateEntity> selectActiveTemplates(@Param("now") LocalDateTime now);

    /**
     * 查询指定时间范围内的邮件模板
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 邮件模板列表
     */
    @Select("SELECT * FROM t_mail_template WHERE created_at BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 ORDER BY created_at DESC")
    List<MailTemplateEntity> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 查询指定目标类型的邮件模板
     *
     * @param targetType 目标类型
     * @return 邮件模板列表
     */
    @Select("SELECT * FROM t_mail_template WHERE target_type = #{targetType} AND is_deleted = 0 ORDER BY created_at DESC")
    List<MailTemplateEntity> selectByTargetType(@Param("targetType") Integer targetType);
}
