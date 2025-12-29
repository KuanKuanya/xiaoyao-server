package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.logic.entity.EquipmentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 装备 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<EquipmentEntity> {

    /**
     * 根据玩家ID查询已装备列表
     */
    default List<EquipmentEntity> selectByPlayerId(Long playerId) {
        return selectListByQuery(QueryWrapper.create()
                .where("player_id = ?", playerId));
    }
}
