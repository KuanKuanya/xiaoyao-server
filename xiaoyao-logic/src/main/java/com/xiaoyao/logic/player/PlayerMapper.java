package com.xiaoyao.logic.player;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 玩家 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface PlayerMapper extends BaseMapper<PlayerEntity> {

    /**
     * 根据设备ID查询玩家
     */
    @Select("SELECT * FROM t_player WHERE device_id = #{deviceId} AND is_deleted = 0 LIMIT 1")
    PlayerEntity selectByDeviceId(@Param("deviceId") String deviceId);
}
