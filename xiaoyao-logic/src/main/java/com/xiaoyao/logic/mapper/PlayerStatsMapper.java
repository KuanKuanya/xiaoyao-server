package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.xiaoyao.logic.entity.PlayerStatsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 玩家属性 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface PlayerStatsMapper extends BaseMapper<PlayerStatsEntity> {
    // 继承 BaseMapper 已提供基础 CRUD 方法
    // 如需自定义SQL，可在此添加方法
}
