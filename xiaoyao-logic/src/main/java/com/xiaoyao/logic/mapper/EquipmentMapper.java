package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
        return selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<EquipmentEntity>()
                .eq(EquipmentEntity::getPlayerId, playerId));
    }
}
