package com.xiaoyao.logic.quest;

import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.logic.common.constants.GameConstants;
import com.xiaoyao.logic.common.exception.BusinessException;
import com.xiaoyao.logic.common.exception.ErrorCode;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 每日任务服务
 * 提供每日任务的进度跟踪和奖励领取功能
 *
 * <p>
 * 扩展点说明：
 * 1. 任务配置（目标进度、奖励等）应由配置管理器提供
 * 2. 实际的奖励发放应由RewardService实现
 * 3. 任务完成时可以触发回调通知其他系统
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class DailyQuestService {

    @Resource
    private DailyQuestMapper dailyQuestMapper;

    /**
     * 初始化今日任务记录
     * 一般在玩家首次访问任务系统时调用
     *
     * @param playerId       玩家ID
     * @param questCfgId     任务配置ID
     * @param targetProgress 目标进度（从配置读取）
     * @return 任务实体
     */
    @Transactional(rollbackFor = Exception.class)
    public DailyQuestEntity initTodayQuest(Long playerId, String questCfgId, Integer targetProgress) {
        validatePlayerId(playerId);
        validateQuestCfgId(questCfgId);

        if (targetProgress == null || targetProgress <= 0) {
            throw BusinessException.of(ErrorCode.QUEST_CONFIG_ERROR,
                    "任务目标进度必须大于0");
        }

        LocalDate today = LocalDate.now();

        // 检查今日任务是否已存在
        DailyQuestEntity existing = dailyQuestMapper.selectByQuestCfgId(playerId, today, questCfgId);
        if (existing != null) {
            return existing; // 已存在则直接返回
        }

        // 创建新记录
        DailyQuestEntity quest = new DailyQuestEntity();
        quest.setPlayerId(playerId);
        quest.setQuestDate(today);
        quest.setQuestCfgId(questCfgId);
        quest.setProgress(0);
        quest.setTarget(targetProgress);
        quest.setStatus(GameConstants.QUEST_STATUS_ONGOING);
        quest.setCreatedBy(playerId);

        dailyQuestMapper.insert(quest);

        log.info("[每日任务] 初始化任务 playerId={} cfgId={} date={} id={}",
                playerId, questCfgId, today, quest.getId());

        return quest;
    }

    /**
     * 增加任务进度
     *
     * @param playerId       玩家ID
     * @param questCfgId     任务配置ID
     * @param addProgress    增加的进度
     * @param targetProgress 目标进度（从配置读取）
     * @return 是否完成任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addProgress(Long playerId, String questCfgId, Integer addProgress, Integer targetProgress) {
        if (addProgress == null || addProgress <= 0) {
            return false;
        }

        // 获取今日任务记录（不存在则创建）
        DailyQuestEntity quest = getOrCreateTodayQuest(playerId, questCfgId, targetProgress);

        // 已完成或已领取的任务不再更新
        if (quest.getStatus() >= GameConstants.QUEST_STATUS_COMPLETED) {
            return false;
        }

        // 更新进度
        Integer oldProgress = quest.getProgress();
        Integer newProgress = Math.min(oldProgress + addProgress, quest.getTarget());

        quest.setProgress(newProgress);
        quest.setUpdatedBy(playerId);

        // 检查是否完成
        boolean completed = checkAndComplete(quest);

        dailyQuestMapper.update(quest);

        log.info("[每日任务] 增加进度 playerId={} cfgId={} add={} progress={}->{} completed={}",
                playerId, questCfgId, addProgress, oldProgress, newProgress, completed);

        return completed;
    }

    /**
     * 设置任务进度（绝对值）
     *
     * @param playerId       玩家ID
     * @param questCfgId     任务配置ID
     * @param progress       进度值
     * @param targetProgress 目标进度
     * @return 是否完成任务
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setProgress(Long playerId, String questCfgId, Integer progress, Integer targetProgress) {
        if (progress == null || progress < 0) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "进度值不能为负数");
        }

        // 获取今日任务记录（不存在则创建）
        DailyQuestEntity quest = getOrCreateTodayQuest(playerId, questCfgId, targetProgress);

        // 已完成或已领取的任务不再更新
        if (quest.getStatus() >= GameConstants.QUEST_STATUS_COMPLETED) {
            return false;
        }

        // 只有当新进度大于旧进度时才更新（防止倒退）
        if (progress <= quest.getProgress()) {
            return false;
        }

        Integer oldProgress = quest.getProgress();
        Integer newProgress = Math.min(progress, quest.getTarget());

        quest.setProgress(newProgress);
        quest.setUpdatedBy(playerId);

        // 检查是否完成
        boolean completed = checkAndComplete(quest);

        dailyQuestMapper.update(quest);

        log.info("[每日任务] 设置进度 playerId={} cfgId={} progress={}->{} completed={}",
                playerId, questCfgId, oldProgress, newProgress, completed);

        return completed;
    }

    /**
     * 领取任务奖励
     *
     * @param playerId   玩家ID
     * @param questCfgId 任务配置ID
     * @throws BusinessException 任务不存在、未完成或已领取
     */
    @Transactional(rollbackFor = Exception.class)
    public void claimReward(Long playerId, String questCfgId) {
        validatePlayerId(playerId);
        validateQuestCfgId(questCfgId);

        LocalDate today = LocalDate.now();

        // 查询今日任务记录
        DailyQuestEntity quest = dailyQuestMapper.selectByQuestCfgId(playerId, today, questCfgId);
        if (quest == null) {
            throw BusinessException.of(ErrorCode.QUEST_NOT_FOUND,
                    "任务不存在 playerId=%d cfgId=%s date=%s", playerId, questCfgId, today);
        }

        // 检查状态
        if (quest.getStatus() == GameConstants.QUEST_STATUS_ONGOING) {
            throw BusinessException.of(ErrorCode.QUEST_NOT_COMPLETED,
                    "任务未完成 playerId=%d cfgId=%s progress=%d/%d",
                    playerId, questCfgId, quest.getProgress(), quest.getTarget());
        }

        if (quest.getStatus() == GameConstants.QUEST_STATUS_CLAIMED) {
            throw BusinessException.of(ErrorCode.QUEST_ALREADY_CLAIMED,
                    "任务已领取 playerId=%d cfgId=%s", playerId, questCfgId);
        }

        // 更新状态
        quest.setStatus(GameConstants.QUEST_STATUS_CLAIMED);
        quest.setClaimTime(System.currentTimeMillis());
        quest.setUpdatedBy(playerId);

        dailyQuestMapper.update(quest);

        log.info("[每日任务] 领取奖励 playerId={} cfgId={} id={}",
                playerId, questCfgId, quest.getId());

        // TODO: 调用RewardService发放实际奖励
        // rewardService.grantQuestReward(playerId, questCfgId);
    }

    /**
     * 查询玩家今日所有任务
     *
     * @param playerId 玩家ID
     * @return 今日任务列表
     */
    public List<DailyQuestEntity> getTodayQuests(Long playerId) {
        validatePlayerId(playerId);
        LocalDate today = LocalDate.now();
        return dailyQuestMapper.selectByPlayerIdAndDate(playerId, today);
    }

    /**
     * 查询玩家指定日期的任务
     *
     * @param playerId  玩家ID
     * @param questDate 任务日期
     * @return 任务列表
     */
    public List<DailyQuestEntity> getQuestsByDate(Long playerId, LocalDate questDate) {
        validatePlayerId(playerId);
        if (questDate == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "任务日期不能为空");
        }
        return dailyQuestMapper.selectByPlayerIdAndDate(playerId, questDate);
    }

    /**
     * 查询玩家今日指定任务
     *
     * @param playerId   玩家ID
     * @param questCfgId 任务配置ID
     * @return 任务实体（不存在返回null）
     */
    public DailyQuestEntity getTodayQuest(Long playerId, String questCfgId) {
        validatePlayerId(playerId);
        validateQuestCfgId(questCfgId);

        LocalDate today = LocalDate.now();
        return dailyQuestMapper.selectByQuestCfgId(playerId, today, questCfgId);
    }

    /**
     * 统计今日任务完成情况
     *
     * @param playerId 玩家ID
     * @return [已完成数, 已领取数]
     */
    public int[] getTodayStatistics(Long playerId) {
        validatePlayerId(playerId);
        LocalDate today = LocalDate.now();

        Integer completedCount = dailyQuestMapper.countCompletedByDate(playerId, today);
        Integer claimedCount = dailyQuestMapper.countClaimedByDate(playerId, today);

        return new int[] {
                completedCount != null ? completedCount : 0,
                claimedCount != null ? claimedCount : 0
        };
    }

    /**
     * 检查今日是否有可领取的任务（用于红点）
     *
     * @param playerId 玩家ID
     * @return 是否有可领取任务
     */
    public boolean hasUnclaimedQuests(Long playerId) {
        validatePlayerId(playerId);
        LocalDate today = LocalDate.now();

        List<DailyQuestEntity> quests = dailyQuestMapper.selectByPlayerIdAndDate(playerId, today);
        if (quests == null || quests.isEmpty()) {
            return false;
        }

        // 检查是否有已完成但未领取的任务
        return quests.stream()
                .anyMatch(quest -> quest.getStatus() == GameConstants.QUEST_STATUS_COMPLETED);
    }

    /**
     * 清理过期任务记录（定时任务调用）
     * 建议每天凌晨执行，清理7天前的记录
     *
     * @param beforeDate 清理此日期之前的记录
     * @return 清理的记录数
     */
    @Transactional(rollbackFor = Exception.class)
    public int cleanExpiredQuests(LocalDate beforeDate) {
        if (beforeDate == null) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "清理日期不能为空");
        }

        // 使用字符串方式构建查询（TableDef 需要编译后生成）
        QueryWrapper query = QueryWrapper.create()
                .from(DailyQuestEntity.class)
                .where("quest_date < {0}", beforeDate);

        int count = dailyQuestMapper.deleteByQuery(query);

        log.info("[每日任务] 清理过期记录 beforeDate={} count={}", beforeDate, count);

        return count;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 获取或创建今日任务记录
     */
    private DailyQuestEntity getOrCreateTodayQuest(Long playerId, String questCfgId, Integer targetProgress) {
        LocalDate today = LocalDate.now();
        DailyQuestEntity quest = dailyQuestMapper.selectByQuestCfgId(playerId, today, questCfgId);

        if (quest == null) {
            quest = initTodayQuest(playerId, questCfgId, targetProgress);
        }

        return quest;
    }

    /**
     * 检查并完成任务
     *
     * @param quest 任务实体
     * @return 是否刚刚完成
     */
    private boolean checkAndComplete(DailyQuestEntity quest) {
        // 进度达标且当前状态为进行中，则标记为完成
        if (quest.getProgress() >= quest.getTarget()
                && quest.getStatus() == GameConstants.QUEST_STATUS_ONGOING) {

            quest.setStatus(GameConstants.QUEST_STATUS_COMPLETED);

            log.info("[每日任务] 完成任务 playerId={} cfgId={} progress={}/{}",
                    quest.getPlayerId(), quest.getQuestCfgId(),
                    quest.getProgress(), quest.getTarget());

            // TODO: 触发任务完成回调
            // onQuestCompleted(quest);

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
     * 校验任务配置ID
     */
    private void validateQuestCfgId(String questCfgId) {
        if (!StringUtils.hasText(questCfgId)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "任务配置ID不能为空");
        }
    }

    // ==================== 扩展点 ====================

    /**
     * 任务完成回调（扩展点）
     * 可在此触发其他业务逻辑，如更新成就进度等
     */
    @SuppressWarnings("unused")
    private void onQuestCompleted(DailyQuestEntity quest) {
        // TODO: 实现任务完成的回调逻辑
        // 例如：更新成就进度、触发活动任务等
    }
}
