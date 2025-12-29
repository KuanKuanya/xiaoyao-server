package com.xiaoyao.logic.sect;

import com.xiaoyao.logic.common.constants.GameConstants;
import com.xiaoyao.logic.common.exception.BusinessException;
import com.xiaoyao.logic.common.exception.ErrorCode;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宗门服务
 * 提供宗门成员管理、贡献度、俸禄等功能
 *
 * <p>职位权限设计：
 * - 掌门（4）：可以管理所有成员
 * - 副掌门（3）：可以管理长老及以下
 * - 长老（2）：可以管理内门及以下
 * - 内门（1）：可以踢出弟子
 * - 弟子（0）：无管理权限
 *
 * <p>扩展点说明：
 * 1. 宗门配置（人数上限、俸禄规则等）应由配置管理器提供
 * 2. 俸禄奖励发放应由RewardService实现
 * 3. 宗门事件可触发回调通知系统
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class SectService {

    @Resource
    private SectMemberMapper sectMemberMapper;

    /**
     * 加入宗门
     *
     * @param playerId 玩家ID
     * @param sectId 宗门配置ID
     * @return 成员实体
     * @throws BusinessException 已加入宗门或宗门人数已满
     */
    @Transactional(rollbackFor = Exception.class)
    public SectMemberEntity joinSect(Long playerId, String sectId) {
        validatePlayerId(playerId);
        validateSectId(sectId);

        // 检查是否已加入宗门
        SectMemberEntity existingMember = sectMemberMapper.selectByPlayerId(playerId);
        if (existingMember != null) {
            throw BusinessException.of(ErrorCode.SECT_ALREADY_JOINED,
                    "已加入宗门 playerId=%d sectId=%s", playerId, existingMember.getSectId());
        }

        // 检查宗门人数上限
        Integer memberCount = sectMemberMapper.countBySectId(sectId);
        if (memberCount != null && memberCount >= GameConstants.SECT_MEMBER_MAX_COUNT) {
            throw BusinessException.of(ErrorCode.SECT_MEMBER_FULL,
                    "宗门人数已满 sectId=%s count=%d limit=%d",
                    sectId, memberCount, GameConstants.SECT_MEMBER_MAX_COUNT);
        }

        // 创建成员记录
        SectMemberEntity member = new SectMemberEntity();
        member.setPlayerId(playerId);
        member.setSectId(sectId);
        member.setPosition(GameConstants.SECT_POSITION_DISCIPLE); // 新成员默认为弟子
        member.setContribution(0L);
        member.setWeeklyContribution(0L);
        member.setLastSalaryTime(0L);
        member.setTotalSalaryCount(0);
        member.setCreatedBy(playerId);

        sectMemberMapper.insert(member);

        log.info("[宗门服务] 加入宗门 playerId={} sectId={} memberId={}",
                playerId, sectId, member.getId());

        // TODO: 触发加入宗门回调
        // onPlayerJoinSect(member);

        return member;
    }

    /**
     * 退出宗门
     *
     * @param playerId 玩家ID
     * @throws BusinessException 未加入宗门或掌门不能直接退出
     */
    @Transactional(rollbackFor = Exception.class)
    public void quitSect(Long playerId) {
        validatePlayerId(playerId);

        // 查询成员记录
        SectMemberEntity member = getPlayerSectMember(playerId);

        // 掌门不能直接退出，需要先转让掌门
        if (member.getPosition() == GameConstants.SECT_POSITION_LEADER) {
            throw BusinessException.of(ErrorCode.PERMISSION_DENIED,
                    "掌门不能直接退出，请先转让掌门职位 playerId=%d", playerId);
        }

        // 逻辑删除
        member.setIsDeleted(GameConstants.DELETED_YES);
        member.setDeletedBy(playerId); // 自己退出
        member.setDeletedAt(LocalDateTime.now());

        sectMemberMapper.update(member);

        log.info("[宗门服务] 退出宗门 playerId={} sectId={} position={}",
                playerId, member.getSectId(), member.getPosition());

        // TODO: 触发退出宗门回调
        // onPlayerQuitSect(member);
    }

    /**
     * 踢出成员
     *
     * @param operatorId 操作者ID
     * @param targetPlayerId 目标玩家ID
     * @throws BusinessException 权限不足或目标玩家不存在
     */
    @Transactional(rollbackFor = Exception.class)
    public void kickMember(Long operatorId, Long targetPlayerId) {
        validatePlayerId(operatorId);
        validatePlayerId(targetPlayerId);

        if (operatorId.equals(targetPlayerId)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "不能踢出自己");
        }

        // 查询操作者和目标玩家的成员记录
        SectMemberEntity operator = getPlayerSectMember(operatorId);
        SectMemberEntity target = getPlayerSectMember(targetPlayerId);

        // 检查是否在同一宗门
        if (!operator.getSectId().equals(target.getSectId())) {
            throw BusinessException.of(ErrorCode.PERMISSION_DENIED,
                    "目标玩家不在同一宗门 operatorId=%d targetId=%d", operatorId, targetPlayerId);
        }

        // 检查权限（职位必须高于目标玩家）
        if (operator.getPosition() <= target.getPosition()) {
            throw BusinessException.of(ErrorCode.SECT_POSITION_NOT_ENOUGH,
                    "职位不足，无法踢出该成员 operator=%d target=%d",
                    operator.getPosition(), target.getPosition());
        }

        // 逻辑删除
        target.setIsDeleted(GameConstants.DELETED_YES);
        target.setDeletedBy(operatorId); // 记录是谁踢出的
        target.setDeletedAt(LocalDateTime.now());

        sectMemberMapper.update(target);

        log.info("[宗门服务] 踢出成员 operatorId={} targetId={} sectId={}",
                operatorId, targetPlayerId, target.getSectId());

        // TODO: 触发踢出成员回调
        // onMemberKicked(operator, target);
    }

    /**
     * 晋升/降职
     *
     * @param operatorId 操作者ID
     * @param targetPlayerId 目标玩家ID
     * @param newPosition 新职位
     * @throws BusinessException 权限不足或参数无效
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePosition(Long operatorId, Long targetPlayerId, Integer newPosition) {
        validatePlayerId(operatorId);
        validatePlayerId(targetPlayerId);
        validatePosition(newPosition);

        if (operatorId.equals(targetPlayerId) && newPosition == GameConstants.SECT_POSITION_LEADER) {
            // 允许掌门自己设置自己为掌门（转让掌门时可能需要）
        } else if (operatorId.equals(targetPlayerId)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "不能给自己晋升/降职");
        }

        // 查询操作者和目标玩家的成员记录
        SectMemberEntity operator = getPlayerSectMember(operatorId);
        SectMemberEntity target = getPlayerSectMember(targetPlayerId);

        // 检查是否在同一宗门
        if (!operator.getSectId().equals(target.getSectId())) {
            throw BusinessException.of(ErrorCode.PERMISSION_DENIED,
                    "目标玩家不在同一宗门");
        }

        // 只有掌门可以任命副掌门和其他掌门
        if (newPosition >= GameConstants.SECT_POSITION_VICE_LEADER
                && operator.getPosition() < GameConstants.SECT_POSITION_LEADER) {
            throw BusinessException.of(ErrorCode.SECT_POSITION_NOT_ENOUGH,
                    "只有掌门才能任命副掌门或转让掌门");
        }

        // 检查权限（操作者职位必须高于目标和新职位）
        if (operator.getPosition() <= target.getPosition()
                || operator.getPosition() <= newPosition) {
            throw BusinessException.of(ErrorCode.SECT_POSITION_NOT_ENOUGH,
                    "职位不足，无法进行该操作");
        }

        Integer oldPosition = target.getPosition();

        // 更新职位
        target.setPosition(newPosition);
        target.setUpdatedBy(operatorId);

        sectMemberMapper.update(target);

        log.info("[宗门服务] 变更职位 operatorId={} targetId={} position={}->{} sectId={}",
                operatorId, targetPlayerId, oldPosition, newPosition, target.getSectId());

        // TODO: 触发职位变更回调
        // onPositionChanged(operator, target, oldPosition, newPosition);
    }

    /**
     * 增加贡献度
     *
     * @param playerId 玩家ID
     * @param addContribution 增加的贡献度
     * @throws BusinessException 未加入宗门
     */
    @Transactional(rollbackFor = Exception.class)
    public void addContribution(Long playerId, Long addContribution) {
        if (addContribution == null || addContribution <= 0) {
            return;
        }

        // 查询成员记录
        SectMemberEntity member = getPlayerSectMember(playerId);

        // 增加贡献度
        Long oldContribution = member.getContribution();
        Long newContribution = oldContribution + addContribution;

        member.setContribution(newContribution);
        member.setWeeklyContribution(member.getWeeklyContribution() + addContribution);
        member.setUpdatedBy(playerId);

        sectMemberMapper.update(member);

        log.info("[宗门服务] 增加贡献度 playerId={} add={} contribution={}->{} sectId={}",
                playerId, addContribution, oldContribution, newContribution, member.getSectId());
    }

    /**
     * 领取俸禄
     *
     * @param playerId 玩家ID
     * @throws BusinessException 未加入宗门或冷却中
     */
    @Transactional(rollbackFor = Exception.class)
    public void claimSalary(Long playerId) {
        validatePlayerId(playerId);

        // 查询成员记录
        SectMemberEntity member = getPlayerSectMember(playerId);

        // 检查冷却时间
        long now = System.currentTimeMillis();
        long lastClaimTime = member.getLastSalaryTime() != null ? member.getLastSalaryTime() : 0;
        long cooldownMillis = GameConstants.SECT_SALARY_COOLDOWN_SECONDS * 1000;

        if (now - lastClaimTime < cooldownMillis) {
            long remainingSeconds = (cooldownMillis - (now - lastClaimTime)) / 1000;
            throw BusinessException.of(ErrorCode.SECT_SALARY_COOLDOWN,
                    "俸禄领取冷却中，剩余%d秒", remainingSeconds);
        }

        // 更新领取时间
        member.setLastSalaryTime(now);
        member.setTotalSalaryCount(member.getTotalSalaryCount() + 1);
        member.setUpdatedBy(playerId);

        sectMemberMapper.update(member);

        log.info("[宗门服务] 领取俸禄 playerId={} position={} count={} sectId={}",
                playerId, member.getPosition(), member.getTotalSalaryCount(), member.getSectId());

        // TODO: 调用RewardService发放实际奖励
        // rewardService.grantSectSalary(playerId, member.getPosition());
    }

    /**
     * 重置宗门所有成员的周贡献（定时任务调用）
     *
     * @param sectId 宗门配置ID
     * @return 重置的成员数
     */
    @Transactional(rollbackFor = Exception.class)
    public int resetWeeklyContribution(String sectId) {
        validateSectId(sectId);

        Integer count = sectMemberMapper.resetWeeklyContribution(sectId);

        log.info("[宗门服务] 重置周贡献 sectId={} count={}", sectId, count);

        return count != null ? count : 0;
    }

    /**
     * 查询玩家的宗门信息
     *
     * @param playerId 玩家ID
     * @return 成员实体（未加入返回null）
     */
    public SectMemberEntity getPlayerSect(Long playerId) {
        validatePlayerId(playerId);
        return sectMemberMapper.selectByPlayerId(playerId);
    }

    /**
     * 查询宗门所有成员
     *
     * @param sectId 宗门配置ID
     * @return 成员列表（按职位和贡献度降序）
     */
    public List<SectMemberEntity> getSectMembers(String sectId) {
        validateSectId(sectId);
        return sectMemberMapper.selectBySectId(sectId);
    }

    /**
     * 查询宗门贡献度排行榜
     *
     * @param sectId 宗门配置ID
     * @param limit 限制数量
     * @return 成员列表（按贡献度降序）
     */
    public List<SectMemberEntity> getContributionRank(String sectId, Integer limit) {
        validateSectId(sectId);

        if (limit == null || limit <= 0) {
            limit = 10; // 默认显示前10名
        }

        return sectMemberMapper.selectTopContributors(sectId, limit);
    }

    /**
     * 统计宗门成员数量
     *
     * @param sectId 宗门配置ID
     * @return 成员数量
     */
    public int countSectMembers(String sectId) {
        validateSectId(sectId);
        Integer count = sectMemberMapper.countBySectId(sectId);
        return count != null ? count : 0;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 查询玩家的宗门成员记录
     *
     * @throws BusinessException 未加入宗门
     */
    private SectMemberEntity getPlayerSectMember(Long playerId) {
        SectMemberEntity member = sectMemberMapper.selectByPlayerId(playerId);
        if (member == null) {
            throw BusinessException.of(ErrorCode.SECT_NOT_JOINED,
                    "未加入宗门 playerId=%d", playerId);
        }
        return member;
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
     * 校验宗门配置ID
     */
    private void validateSectId(String sectId) {
        if (!StringUtils.hasText(sectId)) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "宗门配置ID不能为空");
        }
    }

    /**
     * 校验职位
     */
    private void validatePosition(Integer position) {
        if (position == null || position < 0 || position > 4) {
            throw BusinessException.of(ErrorCode.PARAM_INVALID, "职位无效: %d", position);
        }
    }

    // ==================== 扩展点 ====================

    /**
     * 加入宗门回调（扩展点）
     */
    @SuppressWarnings("unused")
    private void onPlayerJoinSect(SectMemberEntity member) {
        // TODO: 实现加入宗门的回调逻辑
        // 例如：发送系统消息、更新成就进度等
    }

    /**
     * 退出宗门回调（扩展点）
     */
    @SuppressWarnings("unused")
    private void onPlayerQuitSect(SectMemberEntity member) {
        // TODO: 实现退出宗门的回调逻辑
    }

    /**
     * 踢出成员回调（扩展点）
     */
    @SuppressWarnings("unused")
    private void onMemberKicked(SectMemberEntity operator, SectMemberEntity target) {
        // TODO: 实现踢出成员的回调逻辑
        // 例如：记录操作日志、发送系统消息等
    }

    /**
     * 职位变更回调（扩展点）
     */
    @SuppressWarnings("unused")
    private void onPositionChanged(SectMemberEntity operator, SectMemberEntity target,
                                   Integer oldPosition, Integer newPosition) {
        // TODO: 实现职位变更的回调逻辑
        // 例如：全服公告、发送系统消息等
    }
}
