package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
        return selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<IdleBattleEntity>()
                .eq(IdleBattleEntity::getPlayerId, playerId)
                .eq(IdleBattleEntity::getIsActive, true)
                .last("LIMIT 1"));
    }
}
