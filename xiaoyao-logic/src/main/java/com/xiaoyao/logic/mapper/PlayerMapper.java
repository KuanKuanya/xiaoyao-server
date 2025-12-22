package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.PlayerEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 玩家 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface PlayerMapper extends BaseMapper<PlayerEntity> {
    
}
