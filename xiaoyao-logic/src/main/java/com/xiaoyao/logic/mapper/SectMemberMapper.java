package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.SectMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 宗门成员Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface SectMemberMapper extends BaseMapper<SectMemberEntity> {

    /**
     * 查询玩家当前的宗门
     *
     * @param playerId 玩家ID
     * @return 宗门成员实体
     */
    @Select("SELECT * FROM t_sect_member WHERE player_id = #{playerId} AND is_deleted = 0 LIMIT 1")
    SectMemberEntity selectByPlayerId(@Param("playerId") Long playerId);

    /**
     * 查询指定宗门的所有成员
     *
     * @param sectId 宗门配置ID
     * @return 成员列表
     */
    @Select("SELECT * FROM t_sect_member WHERE sect_id = #{sectId} AND is_deleted = 0 ORDER BY position DESC, contribution DESC")
    List<SectMemberEntity> selectBySectId(@Param("sectId") String sectId);

    /**
     * 查询宗门贡献度排行榜
     *
     * @param sectId 宗门配置ID
     * @param limit 限制数量
     * @return 成员列表（按贡献度降序）
     */
    @Select("SELECT * FROM t_sect_member WHERE sect_id = #{sectId} AND is_deleted = 0 ORDER BY contribution DESC LIMIT #{limit}")
    List<SectMemberEntity> selectTopContributors(@Param("sectId") String sectId, @Param("limit") Integer limit);

    /**
     * 统计宗门成员数量
     *
     * @param sectId 宗门配置ID
     * @return 成员数量
     */
    @Select("SELECT COUNT(*) FROM t_sect_member WHERE sect_id = #{sectId} AND is_deleted = 0")
    Integer countBySectId(@Param("sectId") String sectId);

    /**
     * 重置宗门所有成员的周贡献
     *
     * @param sectId 宗门配置ID
     * @return 更新行数
     */
    @Update("UPDATE t_sect_member SET weekly_contribution = 0, updated_at = NOW() WHERE sect_id = #{sectId} AND is_deleted = 0")
    Integer resetWeeklyContribution(@Param("sectId") String sectId);
}
