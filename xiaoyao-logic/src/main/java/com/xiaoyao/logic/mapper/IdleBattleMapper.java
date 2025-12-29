package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.logic.entity.IdleBattleEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 挂机记录 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface IdleBattleMapper extends BaseMapper<IdleBattleEntity> {

    /**
     * 根据玩家ID查询进行中的挂机记录
     */
    default IdleBattleEntity selectActiveByPlayerId(Long playerId) {
        return selectOneByQuery(QueryWrapper.create()
                .where("player_id = ? AND is_active = ?", playerId, true)
                .limit(1));
    }
}
