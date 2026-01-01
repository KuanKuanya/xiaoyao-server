package com.xiaoyao.logic.quest;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.QuestCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务模块 Action
 * 处理每日任务、成长任务相关请求
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(QuestCmd.cmd)
public class QuestAction {

    @Resource
    private DailyQuestService dailyQuestService;

    /**
     * 获取任务列表
     */
    @ActionMethod(QuestCmd.getList)
    public List<DailyQuestEntity> getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[任务] 获取列表 playerId={}", playerId);
        return dailyQuestService.getTodayQuests(playerId);
    }

    /**
     * 领取任务奖励
     */
    @ActionMethod(QuestCmd.claim)
    public boolean claim(String questCfgId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[任务] 领取奖励 playerId={}, questCfgId={}", playerId, questCfgId);

        try {
            dailyQuestService.claimReward(playerId, questCfgId);
            return true;
        } catch (Exception e) {
            log.error("[任务] 领取失败 playerId={}, questCfgId={}", playerId, questCfgId, e);
            return false;
        }
    }

    /**
     * 获取每日任务列表
     */
    @ActionMethod(QuestCmd.getDailyList)
    public List<DailyQuestEntity> getDailyList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[任务] 获取每日任务 playerId={}", playerId);
        return dailyQuestService.getTodayQuests(playerId);
    }
}
