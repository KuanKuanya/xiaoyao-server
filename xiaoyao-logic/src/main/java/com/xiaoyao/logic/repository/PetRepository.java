package com.xiaoyao.logic.repository;

import com.xiaoyao.logic.entity.PetEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends CrudRepository<PetEntity, Long> {

    @Query("SELECT * FROM t_pet WHERE player_id = :playerId AND is_deleted = 0 ORDER BY pet_quality DESC, pet_level DESC")
    List<PetEntity> findByPlayerId(@Param("playerId") Long playerId);

    @Query("SELECT * FROM t_pet WHERE player_id = :playerId AND is_active = 1 AND is_deleted = 0")
    List<PetEntity> findActivePets(@Param("playerId") Long playerId);

    @Query("SELECT * FROM t_pet WHERE player_id = :playerId AND pet_cfg_id = :petCfgId AND is_deleted = 0 LIMIT 1")
    PetEntity findByPetCfgId(@Param("playerId") Long playerId, @Param("petCfgId") String petCfgId);

    @Query("SELECT COUNT(*) FROM t_pet WHERE player_id = :playerId AND is_deleted = :isDeleted")
    Integer countByPlayerIdAndIsDeleted(@Param("playerId") Long playerId, @Param("isDeleted") Integer isDeleted);

    @Query("SELECT COUNT(*) FROM t_pet WHERE player_id = :playerId AND is_deleted = 0")
    Integer countByPlayerId(@Param("playerId") Long playerId);
}
