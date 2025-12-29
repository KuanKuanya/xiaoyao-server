package com.xiaoyao.logic.combat;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.CombatCmd;
import com.xiaoyao.common.proto.BattleResultResp;
import com.xiaoyao.common.proto.IdleRewardResp;
import com.xiaoyao.common.proto.StartBattleReq;
import lombok.extern.slf4j.Slf4j;

/**
 * 战斗模块 Action
 * <p>
 * 处理战斗开始、挂机、奖励领取等请求
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@ActionController(CombatCmd.cmd)
public class CombatAction {

    private final CombatService combatService = new CombatService();

    /**
     * 开始战斗
     *
     * @param req 战斗请求
     * @param flowContext 请求上下文
     * @return 战斗结果
     */
    @ActionMethod(CombatCmd.startBattle)
    public BattleResultResp startBattle(StartBattleReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 开始战斗 playerId={}, mapId={}", playerId, req.getMapId());

        return combatService.startBattle(playerId, req.getMapId());
    }

    /**
     * 开始挂机战斗
     *
     * @param req 战斗请求
     * @param flowContext 请求上下文
     * @return 是否成功
     */
    @ActionMethod(CombatCmd.startIdleBattle)
    public boolean startIdleBattle(StartBattleReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 开始挂机 playerId={}, mapId={}", playerId, req.getMapId());

        return combatService.startIdleBattle(playerId, req.getMapId());
    }

    /**
     * 停止挂机战斗
     *
     * @param flowContext 请求上下文
     * @return 是否成功
     */
    @ActionMethod(CombatCmd.stopIdleBattle)
    public boolean stopIdleBattle(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 停止挂机 playerId={}", playerId);

        return combatService.stopIdleBattle(playerId);
    }

    /**
     * 领取挂机奖励
     *
     * @param flowContext 请求上下文
     * @return 挂机奖励
     */
    @ActionMethod(CombatCmd.claimIdleReward)
    public IdleRewardResp claimIdleReward(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战斗] 领取挂机奖励 playerId={}", playerId);

        return combatService.claimIdleReward(playerId);
    }
}
