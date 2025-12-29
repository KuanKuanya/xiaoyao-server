package com.xiaoyao.logic.achievement;

import com.xiaoyao.logic.common.constants.GameConstants;
import com.xiaoyao.logic.common.exception.BusinessException;
import com.xiaoyao.logic.common.exception.ErrorCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 成就服务
 * 提供成就进度跟踪和奖励领取功能
 *
 * <p>扩展点说明：
 * 1. 成就配置（目标进度等）应由配置管理器提供
 * 2. 实际的奖励发放应由RewardService实现
 * 3. 成就完成时可以触发回调通知其他系统
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class AchievementService {

    @Resource
    private AchievementMapper achievementMapper;

    /**
     * 初始化玩家的成就记录
     * 一般在玩家首次登录或触发成就系统时调用
     *
     * @param playerId 玩家ID
     * @param achievementCfgId 成就配置ID
     * @param targetProgress 目标进度（从配置读取）
     * @return 成就实体
     */
    @Transactional(rollbackFor = Exception.class)
    public AchievementEntity initAchievement(Long playerId, String achievementCfgId, Integer targetProgress) {
        validatePlayerId(playerId);
        validateAchievementCfgId(achievementCfgId);

        if (targetProgress == null || targetProgress <= 0) {
            throw BusinessException.of(ErrorCode.ACHIEVEMENT_CONFIG_ERROR,
                    "成就目标进度必须大于0");
        }

        // 检查是否已存在
        AchievementEntity existing = achievementMapper.selectByAchievementCfgId(playerId, achievementCfgId);
        if (existing != null) {
            return existing; // 已存在则直接返回
        }

        // 创建新记录
        AchievementEntity achievement = new AchievementEntity();
        achievement.setPlayerId(playerId);
        achievement.setAchievementCfgId(achievementCfgId);
        achievement.setProgress(0);
        achievement.setStatus(GameConstants.ACHIEVEMENT_STATUS_ONGOING);
        achievement.setCreatedBy(playerId);

        achievementMapper.insert(achievement);

        log.info("[成就服务] 初始化成就 playerId={} cfgId={} id={}",
                playerId, achievementCfgId, achievement.getId());

        return achievement;
    }

    /**
     * 增加成就进度
     *
     * @param playerId 玩家ID
     * @param achievementCfgId 成就配置ID
     * @param addProgress 增加的进度
     * @param targetProgress 目标进度（从配置读取）
     * @return 是否完成成就
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addProgress(Long playerId, String achievementCfgId, Integer addProgress, Integer targetProgress) {
        if (addProgress == null || addProgress <= 0) {
            return false;
        }

        // 获取成就记录（不存在则创建）
        AchievementEntity achievement = getOrCreateAchievement(playerId, achievementCfgId, targetProgress);

        // 已完成或已领取的成就不再更新
        if (achievement.getStatus() >= GameConstants.ACHIEVEMENT_STATUS_COMPLETED) {
            return false;
        }

        // 更新进度
        Integer oldProgress = achievement.getProgress();
        Integer newProgress = oldProgress + addProgress;

        achievement.setProgress(newProgress);
        achievement.setUpdatedBy(playerId);

        // 检查是否完成
        boolean completed = checkAndComplete(achievement, targetProgress);

        achievementMapper.update(achievement);

        log.info("[成就服务] 增加进度 playerId={} cfgId={} add={} progress={}->{} completed={}",
                playerId, achievementCfgId, addProgress, oldProgress, newProgress, completed);

        return completed;
    }

    /**
     * 设置成就进度（绝对值）
     *
     * @param playerId 玩家ID
     * @param achievementCfgId 成就配置ID
     * @param progress 进度值
     * @param targetProgress 目标进度
     * @return 是否完成成就
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setProgress(Long playerId, String achievementCfgId, Integer progress, Integer targetProgress) {
        if (progress == null || progress < 0) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "进度值不能为负数");
        }

        // 获取成就记录（不存在则创建）
        AchievementEntity achievement = getOrCreateAchievement(playerId, achievementCfgId, targetProgress);

        // 已完成或已领取的成就不再更新
        if (achievement.getStatus() >= GameConstants.ACHIEVEMENT_STATUS_COMPLETED) {
            return false;
        }

        // 只有当新进度大于旧进度时才更新（防止倒退）
        if (progress <= achievement.getProgress()) {
            return false;
        }

        Integer oldProgress = achievement.getProgress();
        achievement.setProgress(progress);
        achievement.setUpdatedBy(playerId);

        // 检查是否完成
        boolean completed = checkAndComplete(achievement, targetProgress);

        achievementMapper.update(achievement);

        log.info("[成就服务] 设置进度 playerId={} cfgId={} progress={}->{} completed={}",
                playerId, achievementCfgId, oldProgress, progress, completed);

        return completed;
    }

    /**
     * 领取成就奖励
     *
     * @param playerId 玩家ID
     * @param achievementCfgId 成就配置ID
     * @throws BusinessException 成就不存在、未完成或已领取
     */
    @Transactional(rollbackFor = Exception.class)
    public void claimReward(Long playerId, String achievementCfgId) {
        validatePlayerId(playerId);
        validateAchievementCfgId(achievementCfgId);

        // 查询成就记录
        AchievementEntity achievement = achievementMapper.selectByAchievementCfgId(playerId, achievementCfgId);
        if (achievement == null) {
            throw BusinessException.of(ErrorCode.ACHIEVEMENT_NOT_FOUND,
                    "成就不存在 playerId=%d cfgId=%s", playerId, achievementCfgId);
        }

        // 检查状态
        if (achievement.getStatus() == GameConstants.ACHIEVEMENT_STATUS_ONGOING) {
            throw BusinessException.of(ErrorCode.ACHIEVEMENT_NOT_COMPLETED,
                    "成就未完成 playerId=%d cfgId=%s", playerId, achievementCfgId);
        }

        if (achievement.getStatus() == GameConstants.ACHIEVEMENT_STATUS_CLAIMED) {
            throw BusinessException.of(ErrorCode.ACHIEVEMENT_ALREADY_CLAIMED,
                    "成就已领取 playerId=%d cfgId=%s", playerId, achievementCfgId);
        }

        // 更新状态
        achievement.setStatus(GameConstants.ACHIEVEMENT_STATUS_CLAIMED);
        achievement.setClaimTime(System.currentTimeMillis());
        achievement.setUpdatedBy(playerId);

        achievementMapper.update(achievement);

        log.info("[成就服务] 领取奖励 playerId={} cfgId={} id={}",
                playerId, achievementCfgId, achievement.getId());

        // TODO: 调用RewardService发放实际奖励
        // rewardService.grantAchievementReward(playerId, achievementCfgId);
    }

    /**
     * 查询玩家所有成就
     *
     * @param playerId 玩家ID
     * @return 成就列表
     */
    public List<AchievementEntity> getPlayerAchievements(Long playerId) {
        validatePlayerId(playerId);
        return achievementMapper.selectByPlayerId(playerId);
    }

    /**
     * 查询玩家指定成就
     *
     * @param playerId 玩家ID
     * @param achievementCfgId 成就配置ID
     * @return 成就实体（不存在返回null）
     */
    public AchievementEntity getPlayerAchievement(Long playerId, String achievementCfgId) {
        validatePlayerId(playerId);
        validateAchievementCfgId(achievementCfgId);

        return achievementMapper.selectByAchievementCfgId(playerId, achievementCfgId);
    }

    /**
     * 查询可领取的成就（用于红点提示）
     *
     * @param playerId 玩家ID
     * @return 可领取成就列表
     */
    public List<AchievementEntity> getUnclaimedAchievements(Long playerId) {
        validatePlayerId(playerId);
        return achievementMapper.selectUnclaimedAchievements(playerId);
    }

    /**
     * 统计玩家成就完成情况
     *
     * @param playerId 玩家ID
     * @return [已完成数, 已领取数]
     */
    public int[] getAchievementStatistics(Long playerId) {
        validatePlayerId(playerId);

        Integer completedCount = achievementMapper.countCompletedByPlayerId(playerId);
        Integer claimedCount = achievementMapper.countClaimedByPlayerId(playerId);

        return new int[]{
                completedCount != null ? completedCount : 0,
                claimedCount != null ? claimedCount : 0
        };
    }

    /**
     * 检查是否有可领取的成就（用于红点）
     *
     * @param playerId 玩家ID
     * @return 是否有可领取成就
     */
    public boolean hasUnclaimedAchievements(Long playerId) {
        validatePlayerId(playerId);
        List<AchievementEntity> unclaimed = achievementMapper.selectUnclaimedAchievements(playerId);
        return unclaimed != null && !unclaimed.isEmpty();
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 获取或创建成就记录
     */
    private AchievementEntity getOrCreateAchievement(Long playerId, String achievementCfgId, Integer targetProgress) {
        AchievementEntity achievement = achievementMapper.selectByAchievementCfgId(playerId, achievementCfgId);

        if (achievement == null) {
            achievement = initAchievement(playerId, achievementCfgId, targetProgress);
        }

        return achievement;
    }

    /**
     * 检查并完成成就
     *
     * @param achievement 成就实体
     * @param targetProgress 目标进度
     * @return 是否刚刚完成
     */
    private boolean checkAndComplete(AchievementEntity achievement, Integer targetProgress) {
        if (targetProgress == null || targetProgress <= 0) {
            log.warn("[成就服务] 目标进度无效 cfgId={} target={}",
                    achievement.getAchievementCfgId(), targetProgress);
            return false;
        }

        // 进度达标且当前状态为进行中，则标记为完成
        if (achievement.getProgress() >= targetProgress
                && achievement.getStatus() == GameConstants.ACHIEVEMENT_STATUS_ONGOING) {

            achievement.setStatus(GameConstants.ACHIEVEMENT_STATUS_COMPLETED);
            achievement.setCompleteTime(System.currentTimeMillis());

            log.info("[成就服务] 完成成就 playerId={} cfgId={} progress={}/{}",
                    achievement.getPlayerId(), achievement.getAchievementCfgId(),
                    achievement.getProgress(), targetProgress);

            // TODO: 触发成就完成回调
            // onAchievementCompleted(achievement);

            return true;
        }

        return false;
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
     * 校验成就配置ID
     */
    private void validateAchievementCfgId(String achievementCfgId) {
        if (!StringUtils.hasText(achievementCfgId)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "成就配置ID不能为空");
        }
    }

    // ==================== 扩展点 ====================

    /**
     * 成就完成回调（扩展点）
     * 可在此触发其他业务逻辑，如发送系统消息、触发称号等
     */
    @SuppressWarnings("unused")
    private void onAchievementCompleted(AchievementEntity achievement) {
        // TODO: 实现成就完成的回调逻辑
        // 例如：发送系统消息、更新称号、触发其他成就等
    }
}
