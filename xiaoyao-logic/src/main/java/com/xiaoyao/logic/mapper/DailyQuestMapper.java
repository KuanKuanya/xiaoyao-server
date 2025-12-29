package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xiaoyao.logic.entity.DailyQuestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 每日任务Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface DailyQuestMapper extends BaseMapper<DailyQuestEntity> {

    /**
     * 查询玩家指定日期的所有任务
     *
     * @param playerId 玩家ID
     * @param questDate 任务日期
     * @return 任务列表
     */
    @Select("SELECT * FROM t_daily_quest WHERE player_id = #{playerId} AND quest_date = #{questDate} AND is_deleted = 0")
    List<DailyQuestEntity> selectByPlayerIdAndDate(@Param("playerId") Long playerId, @Param("questDate") LocalDate questDate);

    /**
     * 查询玩家今日已完成的任务数
     *
     * @param playerId 玩家ID
     * @param questDate 任务日期
     * @return 已完成任务数
     */
    @Select("SELECT COUNT(*) FROM t_daily_quest WHERE player_id = #{playerId} AND quest_date = #{questDate} AND status >= 1 AND is_deleted = 0")
    Integer countCompletedByDate(@Param("playerId") Long playerId, @Param("questDate") LocalDate questDate);

    /**
     * 查询玩家今日已领取的任务数
     *
     * @param playerId 玩家ID
     * @param questDate 任务日期
     * @return 已领取任务数
     */
    @Select("SELECT COUNT(*) FROM t_daily_quest WHERE player_id = #{playerId} AND quest_date = #{questDate} AND status = 2 AND is_deleted = 0")
    Integer countClaimedByDate(@Param("playerId") Long playerId, @Param("questDate") LocalDate questDate);

    /**
     * 查询玩家指定任务的记录
     *
     * @param playerId 玩家ID
     * @param questDate 任务日期
     * @param questCfgId 任务配置ID
     * @return 任务实体
     */
    @Select("SELECT * FROM t_daily_quest WHERE player_id = #{playerId} AND quest_date = #{questDate} AND quest_cfg_id = #{questCfgId} AND is_deleted = 0 LIMIT 1")
    DailyQuestEntity selectByQuestCfgId(@Param("playerId") Long playerId,
                                        @Param("questDate") LocalDate questDate,
                                        @Param("questCfgId") String questCfgId);
}
