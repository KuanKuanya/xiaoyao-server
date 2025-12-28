package com.xiaoyao.logic.combat;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.CombatCmd;
import com.xiaoyao.common.proto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 战斗模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(CombatCmd.cmd)
public class CombatAction {

    private final CombatService combatService;

    @ActionMethod(CombatCmd.startBattle)
    public BattleResultResp startBattle(StartBattleReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 开始战斗 playerId={}, mapId={}", playerId, req.getMapId());
        return combatService.startBattle(playerId, req.getMapId());
    }

    @ActionMethod(CombatCmd.startIdleBattle)
    public boolean startIdleBattle(StartBattleReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 开始挂机 playerId={}, mapId={}", playerId, req.getMapId());
        return combatService.startIdleBattle(playerId, req.getMapId());
    }

    @ActionMethod(CombatCmd.stopIdleBattle)
    public boolean stopIdleBattle(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 停止挂机 playerId={}", playerId);
        return combatService.stopIdleBattle(playerId);
    }

    @ActionMethod(CombatCmd.claimIdleReward)
    public IdleRewardResp claimIdleReward(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 领取挂机奖励 playerId={}", playerId);
        return combatService.claimIdleReward(playerId);
    }
}
