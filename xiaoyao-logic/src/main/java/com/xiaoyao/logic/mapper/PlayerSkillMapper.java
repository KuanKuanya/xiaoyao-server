package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.logic.entity.PlayerSkillEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 玩家技能 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface PlayerSkillMapper extends BaseMapper<PlayerSkillEntity> {

    /**
     * 根据玩家ID查询技能列表
     */
    default List<PlayerSkillEntity> selectByPlayerId(Long playerId) {
        return selectListByQuery(QueryWrapper.create()
                .where("player_id = ?", playerId)
                .orderBy("level DESC"));
    }
}
