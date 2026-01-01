package com.xiaoyao.logic.player;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 玩家进度 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface PlayerProgressMapper extends BaseMapper<PlayerProgressEntity> {
    // 继承 BaseMapper 已提供基础 CRUD 方法
}
