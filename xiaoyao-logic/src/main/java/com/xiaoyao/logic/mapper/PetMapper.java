package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.PetEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 战宠Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface PetMapper extends BaseMapper<PetEntity> {

    /**
     * 查询玩家的所有战宠
     *
     * @param playerId 玩家ID
     * @return 战宠列表
     */
    @Select("SELECT * FROM t_pet WHERE player_id = #{playerId} AND is_deleted = 0 ORDER BY pet_quality DESC, pet_level DESC")
    List<PetEntity> selectByPlayerId(@Param("playerId") Long playerId);

    /**
     * 查询玩家出战的战宠
     *
     * @param playerId 玩家ID
     * @return 出战战宠列表
     */
    @Select("SELECT * FROM t_pet WHERE player_id = #{playerId} AND is_active = 1 AND is_deleted = 0")
    List<PetEntity> selectActivePets(@Param("playerId") Long playerId);

    /**
     * 根据配置ID查询玩家的战宠
     *
     * @param playerId 玩家ID
     * @param petCfgId 战宠配置ID
     * @return 战宠实体
     */
    @Select("SELECT * FROM t_pet WHERE player_id = #{playerId} AND pet_cfg_id = #{petCfgId} AND is_deleted = 0 LIMIT 1")
    PetEntity selectByPetCfgId(@Param("playerId") Long playerId, @Param("petCfgId") String petCfgId);

    /**
     * 统计玩家战宠数量
     *
     * @param playerId 玩家ID
     * @return 战宠数量
     */
    @Select("SELECT COUNT(*) FROM t_pet WHERE player_id = #{playerId} AND is_deleted = 0")
    Integer countByPlayerId(@Param("playerId") Long playerId);
}
