package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.BagItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 背包物品 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface BagItemMapper extends BaseMapper<BagItemEntity> {
    
    /**
     * 根据玩家ID查询背包物品
     */
    default List<BagItemEntity> selectByPlayerId(Long playerId) {
        return selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BagItemEntity>()
                .eq(BagItemEntity::getPlayerId, playerId)
                .orderByAsc(BagItemEntity::getSlot));
    }
}
