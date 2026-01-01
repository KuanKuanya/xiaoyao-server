package com.xiaoyao.logic.achievement;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.CmdModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 成就模块 Action
 * 处理成就相关请求
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(CmdModule.ACHIEVEMENT)
public class AchievementAction {

    @Resource
    private AchievementService achievementService;

    /**
     * 获取成就列表
     */
    @ActionMethod(1)
    public List<AchievementEntity> getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[成就] 获取列表 playerId={}", playerId);
        return achievementService.getPlayerAchievements(playerId);
    }

    /**
     * 领取成就奖励
     */
    @ActionMethod(2)
    public boolean claim(String achievementId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[成就] 领取奖励 playerId={}, achievementId={}", playerId, achievementId);

        try {
            achievementService.claimReward(playerId, achievementId);
            return true;
        } catch (Exception e) {
            log.error("[成就] 领取失败", e);
            return false;
        }
    }

    /**
     * 获取成就详情
     */
    @ActionMethod(3)
    public AchievementEntity getDetail(String achievementId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[成就] 获取详情 playerId={}, achievementId={}", playerId, achievementId);
        return achievementService.getPlayerAchievement(playerId, achievementId);
    }
}
