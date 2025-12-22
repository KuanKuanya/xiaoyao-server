package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.AchievementEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 成就记录Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface AchievementMapper extends BaseMapper<AchievementEntity> {

    /**
     * 查询玩家所有成就
     *
     * @param playerId 玩家ID
     * @return 成就列表
     */
    @Select("SELECT * FROM t_achievement WHERE player_id = #{playerId} AND is_deleted = 0 ORDER BY status ASC, complete_time DESC")
    List<AchievementEntity> selectByPlayerId(@Param("playerId") Long playerId);

    /**
     * 查询玩家指定成就
     *
     * @param playerId 玩家ID
     * @param achievementCfgId 成就配置ID
     * @return 成就实体
     */
    @Select("SELECT * FROM t_achievement WHERE player_id = #{playerId} AND achievement_cfg_id = #{achievementCfgId} AND is_deleted = 0 LIMIT 1")
    AchievementEntity selectByAchievementCfgId(@Param("playerId") Long playerId, @Param("achievementCfgId") String achievementCfgId);

    /**
     * 查询玩家已完成但未领取的成就
     *
     * @param playerId 玩家ID
     * @return 成就列表
     */
    @Select("SELECT * FROM t_achievement WHERE player_id = #{playerId} AND status = 1 AND is_deleted = 0 ORDER BY complete_time ASC")
    List<AchievementEntity> selectUnclaimedAchievements(@Param("playerId") Long playerId);

    /**
     * 统计玩家已完成成就数
     *
     * @param playerId 玩家ID
     * @return 已完成成就数
     */
    @Select("SELECT COUNT(*) FROM t_achievement WHERE player_id = #{playerId} AND status >= 1 AND is_deleted = 0")
    Integer countCompletedByPlayerId(@Param("playerId") Long playerId);

    /**
     * 统计玩家已领取成就数
     *
     * @param playerId 玩家ID
     * @return 已领取成就数
     */
    @Select("SELECT COUNT(*) FROM t_achievement WHERE player_id = #{playerId} AND status = 2 AND is_deleted = 0")
    Integer countClaimedByPlayerId(@Param("playerId") Long playerId);
}
