package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xiaoyao.logic.entity.LoginLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 登录日志 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogEntity> {

    /**
     * 查询玩家最近的登录记录
     *
     * @param playerId 玩家ID
     * @param limit 限制条数
     * @return 登录日志列表 (按登录时间倒序)
     */
    @Select("SELECT * FROM t_login_log " +
            "WHERE player_id = #{playerId} " +
            "ORDER BY login_time DESC " +
            "LIMIT #{limit}")
    List<LoginLogEntity> selectRecentLogins(@Param("playerId") Long playerId, @Param("limit") Integer limit);

    /**
     * 查询玩家最后一次登录记录
     *
     * @param playerId 玩家ID
     * @return 登录日志
     */
    @Select("SELECT * FROM t_login_log " +
            "WHERE player_id = #{playerId} " +
            "ORDER BY login_time DESC " +
            "LIMIT 1")
    LoginLogEntity selectLastLogin(@Param("playerId") Long playerId);

    /**
     * 更新登出时间和在线时长
     *
     * @param id 日志ID
     * @param logoutTime 登出时间戳
     * @param onlineDuration 在线时长(秒)
     * @return 影响行数
     */
    @Update("UPDATE t_login_log " +
            "SET logout_time = #{logoutTime}, online_duration = #{onlineDuration} " +
            "WHERE id = #{id}")
    int updateLogout(@Param("id") Long id,
                     @Param("logoutTime") Long logoutTime,
                     @Param("onlineDuration") Integer onlineDuration);

    /**
     * 统计指定日期的活跃用户数 (DAU)
     *
     * @param startTime 开始时间戳(毫秒)
     * @param endTime 结束时间戳(毫秒)
     * @return 活跃用户数
     */
    @Select("SELECT COUNT(DISTINCT player_id) FROM t_login_log " +
            "WHERE login_time >= #{startTime} AND login_time < #{endTime}")
    Long countDAU(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
