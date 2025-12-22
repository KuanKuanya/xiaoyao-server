package com.xiaoyao.logic.service;

import com.xiaoyao.logic.constants.GameConstants;
import com.xiaoyao.logic.entity.PetEntity;
import com.xiaoyao.logic.exception.BusinessException;
import com.xiaoyao.logic.exception.ErrorCode;
import com.xiaoyao.logic.mapper.PetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 战宠服务
 * 提供战宠的获取、升级、出战管理等功能
 *
 * <p>扩展点说明：
 * 1. 战宠配置（成长率、属性等）应由配置管理器提供
 * 2. 战斗力计算应由CombatPowerCalculator实现
 * 3. 战宠升级/进化时可触发回调通知其他系统
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class PetService {

    @Resource
    private PetMapper petMapper;

    /**
     * 获得战宠
     *
     * @param playerId 玩家ID
     * @param petCfgId 战宠配置ID
     * @param obtainWay 获取途径（1-战斗捕获 2-商店 3-孵化）
     * @return 战宠实体
     * @throws BusinessException 战宠数量已达上限
     */
    @Transactional(rollbackFor = Exception.class)
    public PetEntity obtainPet(Long playerId, String petCfgId, Integer obtainWay) {
        validatePlayerId(playerId);
        validatePetCfgId(petCfgId);

        // 检查战宠数量上限
        Integer petCount = petMapper.countByPlayerId(playerId);
        if (petCount != null && petCount >= GameConstants.PET_MAX_COUNT) {
            throw BusinessException.of(ErrorCode.PET_COUNT_LIMIT,
                    "战宠数量已达上限 count=%d limit=%d", petCount, GameConstants.PET_MAX_COUNT);
        }

        // 创建战宠实体
        PetEntity pet = new PetEntity();
        pet.setPlayerId(playerId);
        pet.setPetCfgId(petCfgId);
        pet.setPetName(""); // 默认为空，玩家可自定义
        pet.setPetLevel(1);
        pet.setPetExp(0L);
        pet.setPetQuality(GameConstants.PET_QUALITY_WHITE); // 默认白色品质
        pet.setIsActive(GameConstants.NO);
        pet.setHunger(GameConstants.PET_HUNGER_MAX);
        pet.setMood(GameConstants.PET_MOOD_MAX);
        pet.setBonusAtk(0);
        pet.setBonusDef(0);
        pet.setBonusHp(0);
        pet.setObtainWay(obtainWay != null ? obtainWay : 0);
        pet.setObtainTime(System.currentTimeMillis());
        pet.setCreatedBy(playerId);

        // TODO: 从配置读取初始属性加成
        // PetConfig config = petConfigManager.getConfig(petCfgId);
        // pet.setBonusAtk(config.getInitAtk());
        // ...

        petMapper.insert(pet);

        log.info("[战宠服务] 获得战宠 playerId={} petCfgId={} way={} petId={}",
                playerId, petCfgId, obtainWay, pet.getId());

        return pet;
    }

    /**
     * 战宠升级
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @param addExp 增加的经验
     * @return 是否升级成功
     * @throws BusinessException 战宠不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean levelUp(Long playerId, Long petId, Long addExp) {
        if (addExp == null || addExp <= 0) {
            return false;
        }

        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 增加经验
        Long oldExp = pet.getPetExp();
        Long newExp = oldExp + addExp;
        Integer oldLevel = pet.getPetLevel();

        pet.setPetExp(newExp);

        // TODO: 根据配置计算是否升级
        // PetConfig config = petConfigManager.getConfig(pet.getPetCfgId());
        // Long levelUpExp = config.getLevelUpExp(oldLevel);
        // if (newExp >= levelUpExp) {
        //     pet.setPetLevel(oldLevel + 1);
        //     pet.setPetExp(newExp - levelUpExp);
        //     // 计算新的属性加成
        //     updateBonusAttributes(pet);
        // }

        pet.setUpdatedBy(playerId);
        petMapper.updateById(pet);

        boolean levelChanged = !pet.getPetLevel().equals(oldLevel);

        log.info("[战宠服务] 战宠升级 playerId={} petId={} addExp={} level={}->{} levelUp={}",
                playerId, petId, addExp, oldLevel, pet.getPetLevel(), levelChanged);

        if (levelChanged) {
            // TODO: 触发升级回调
            // onPetLevelUp(pet, oldLevel, pet.getPetLevel());
        }

        return levelChanged;
    }

    /**
     * 设置战宠出战
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @throws BusinessException 战宠不存在或出战数量已达上限
     */
    @Transactional(rollbackFor = Exception.class)
    public void setActive(Long playerId, Long petId) {
        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 已经出战则不处理
        if (pet.getIsActive() == GameConstants.YES) {
            return;
        }

        // 检查出战战宠数量
        List<PetEntity> activePets = petMapper.selectActivePets(playerId);
        if (activePets != null && activePets.size() >= GameConstants.PET_ACTIVE_MAX_COUNT) {
            throw BusinessException.of(ErrorCode.PET_ALREADY_ACTIVE,
                    "出战战宠数量已达上限 count=%d limit=%d",
                    activePets.size(), GameConstants.PET_ACTIVE_MAX_COUNT);
        }

        // 设置出战
        pet.setIsActive(GameConstants.YES);
        pet.setUpdatedBy(playerId);

        petMapper.updateById(pet);

        log.info("[战宠服务] 设置出战 playerId={} petId={} cfgId={}",
                playerId, petId, pet.getPetCfgId());
    }

    /**
     * 取消战宠出战
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @throws BusinessException 战宠不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void setInactive(Long playerId, Long petId) {
        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 已经休息则不处理
        if (pet.getIsActive() == GameConstants.NO) {
            return;
        }

        // 取消出战
        pet.setIsActive(GameConstants.NO);
        pet.setUpdatedBy(playerId);

        petMapper.updateById(pet);

        log.info("[战宠服务] 取消出战 playerId={} petId={} cfgId={}",
                playerId, petId, pet.getPetCfgId());
    }

    /**
     * 喂养战宠（恢复饱食度）
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @param addHunger 增加的饱食度
     * @throws BusinessException 战宠不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void feed(Long playerId, Long petId, Integer addHunger) {
        if (addHunger == null || addHunger <= 0) {
            return;
        }

        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 恢复饱食度
        Integer oldHunger = pet.getHunger();
        Integer newHunger = Math.min(oldHunger + addHunger, GameConstants.PET_HUNGER_MAX);

        pet.setHunger(newHunger);
        pet.setUpdatedBy(playerId);

        petMapper.updateById(pet);

        log.info("[战宠服务] 喂养战宠 playerId={} petId={} hunger={}->{}",
                playerId, petId, oldHunger, newHunger);
    }

    /**
     * 提升心情值
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @param addMood 增加的心情值
     * @throws BusinessException 战宠不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void improveMood(Long playerId, Long petId, Integer addMood) {
        if (addMood == null || addMood <= 0) {
            return;
        }

        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 提升心情
        Integer oldMood = pet.getMood();
        Integer newMood = Math.min(oldMood + addMood, GameConstants.PET_MOOD_MAX);

        pet.setMood(newMood);
        pet.setUpdatedBy(playerId);

        petMapper.updateById(pet);

        log.info("[战宠服务] 提升心情 playerId={} petId={} mood={}->{}",
                playerId, petId, oldMood, newMood);
    }

    /**
     * 重命名战宠
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @param newName 新名称
     * @throws BusinessException 战宠不存在或名称无效
     */
    @Transactional(rollbackFor = Exception.class)
    public void renamePet(Long playerId, Long petId, String newName) {
        if (!StringUtils.hasText(newName)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "战宠名称不能为空");
        }

        if (newName.length() > 16) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "战宠名称不能超过16个字符");
        }

        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 更新名称
        pet.setPetName(newName);
        pet.setUpdatedBy(playerId);

        petMapper.updateById(pet);

        log.info("[战宠服务] 重命名战宠 playerId={} petId={} name={}",
                playerId, petId, newName);
    }

    /**
     * 放生战宠（逻辑删除）
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @throws BusinessException 战宠不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void releasePet(Long playerId, Long petId) {
        // 查询战宠
        PetEntity pet = getPetById(playerId, petId);

        // 逻辑删除
        pet.setIsDeleted(GameConstants.DELETED_YES);
        pet.setDeletedBy(playerId);
        pet.setDeletedAt(LocalDateTime.now());

        petMapper.updateById(pet);

        log.info("[战宠服务] 放生战宠 playerId={} petId={} cfgId={}",
                playerId, petId, pet.getPetCfgId());
    }

    /**
     * 查询玩家所有战宠
     *
     * @param playerId 玩家ID
     * @return 战宠列表（按品质和等级降序）
     */
    public List<PetEntity> getPlayerPets(Long playerId) {
        validatePlayerId(playerId);
        return petMapper.selectByPlayerId(playerId);
    }

    /**
     * 查询玩家出战的战宠
     *
     * @param playerId 玩家ID
     * @return 出战战宠列表
     */
    public List<PetEntity> getActivePets(Long playerId) {
        validatePlayerId(playerId);
        return petMapper.selectActivePets(playerId);
    }

    /**
     * 根据ID查询战宠
     *
     * @param playerId 玩家ID
     * @param petId 战宠ID
     * @return 战宠实体
     * @throws BusinessException 战宠不存在
     */
    public PetEntity getPetById(Long playerId, Long petId) {
        validatePlayerId(playerId);

        if (petId == null || petId <= 0) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "战宠ID无效");
        }

        PetEntity pet = petMapper.selectById(petId);
        if (pet == null || pet.getIsDeleted() == GameConstants.DELETED_YES) {
            throw BusinessException.of(ErrorCode.PET_NOT_FOUND,
                    "战宠不存在 playerId=%d petId=%d", playerId, petId);
        }

        // 验证所有权
        if (!pet.getPlayerId().equals(playerId)) {
            throw BusinessException.of(ErrorCode.PERMISSION_DENIED,
                    "无权操作该战宠 playerId=%d petId=%d", playerId, petId);
        }

        return pet;
    }

    /**
     * 计算玩家所有出战战宠的属性加成总和
     *
     * @param playerId 玩家ID
     * @return [攻击加成, 防御加成, 生命加成]
     */
    public int[] calculateTotalBonusAttributes(Long playerId) {
        validatePlayerId(playerId);

        List<PetEntity> activePets = petMapper.selectActivePets(playerId);
        if (activePets == null || activePets.isEmpty()) {
            return new int[]{0, 0, 0};
        }

        int totalAtk = 0;
        int totalDef = 0;
        int totalHp = 0;

        for (PetEntity pet : activePets) {
            // 饱食度和心情值影响属性加成（低于50则打折扣）
            double factor = calculateAttributeFactor(pet.getHunger(), pet.getMood());

            totalAtk += (int) (pet.getBonusAtk() * factor);
            totalDef += (int) (pet.getBonusDef() * factor);
            totalHp += (int) (pet.getBonusHp() * factor);
        }

        return new int[]{totalAtk, totalDef, totalHp};
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 计算属性加成系数
     * 饱食度和心情值都低于50时属性打折扣
     *
     * @param hunger 饱食度
     * @param mood 心情值
     * @return 属性系数（0.5-1.0）
     */
    private double calculateAttributeFactor(Integer hunger, Integer mood) {
        if (hunger >= 50 && mood >= 50) {
            return 1.0; // 满状态
        } else if (hunger < 50 && mood < 50) {
            return 0.5; // 双低状态
        } else {
            return 0.75; // 单低状态
        }
    }

    /**
     * 更新战宠属性加成（升级时调用）
     */
    @SuppressWarnings("unused")
    private void updateBonusAttributes(PetEntity pet) {
        // TODO: 根据配置和等级计算属性加成
        // PetConfig config = petConfigManager.getConfig(pet.getPetCfgId());
        // pet.setBonusAtk(config.getBaseAtk() + pet.getPetLevel() * config.getAtkGrowth());
        // pet.setBonusDef(config.getBaseDef() + pet.getPetLevel() * config.getDefGrowth());
        // pet.setBonusHp(config.getBaseHp() + pet.getPetLevel() * config.getHpGrowth());
    }

    /**
     * 校验玩家ID
     */
    private void validatePlayerId(Long playerId) {
        if (playerId == null || playerId <= 0) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "玩家ID无效");
        }
    }

    /**
     * 校验战宠配置ID
     */
    private void validatePetCfgId(String petCfgId) {
        if (!StringUtils.hasText(petCfgId)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "战宠配置ID不能为空");
        }
    }

    // ==================== 扩展点 ====================

    /**
     * 战宠升级回调（扩展点）
     */
    @SuppressWarnings("unused")
    private void onPetLevelUp(PetEntity pet, Integer oldLevel, Integer newLevel) {
        // TODO: 实现战宠升级的回调逻辑
        // 例如：更新成就进度、发送系统消息、触发特殊事件等
    }
}
