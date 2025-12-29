package com.xiaoyao.logic.inventory;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

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
        return selectListByQuery(QueryWrapper.create()
                .where("player_id = ?", playerId)
                .orderBy("slot ASC"));
    }
}
