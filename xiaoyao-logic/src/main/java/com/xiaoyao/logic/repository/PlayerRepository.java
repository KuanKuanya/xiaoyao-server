package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.PlayerEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

    @Query("SELECT * FROM t_player WHERE device_id = :deviceId AND is_deleted = 0 LIMIT 1")
    PlayerEntity findByDeviceId(@Param("deviceId") String deviceId);
}
