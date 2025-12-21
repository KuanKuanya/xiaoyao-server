package com.xiaoyao.logic.realm;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.exception.MessageException;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.RealmCmd;
import com.xiaoyao.common.error.RealmError;
import com.xiaoyao.common.proto.BreakthroughResp;
import com.xiaoyao.common.proto.CultivateResp;
import com.xiaoyao.common.proto.PlayerData;
import com.xiaoyao.logic.player.PlayerService;
import lombok.extern.slf4j.Slf4j;

/**
 * 境界模块 Action
 * <p>
 * 处理修炼、突破、飞升等请求
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@ActionController(RealmCmd.cmd)
public class RealmAction {
    
    private final RealmService realmService = new RealmService();
    private final PlayerService playerService = new PlayerService();
    
    /**
     * 领取修炼收益 (挂机经验)
     *
     * @param flowContext 请求上下文
     * @return 修炼收益
     */
    @ActionMethod(RealmCmd.cultivate)
    public CultivateResp cultivate(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[修炼] playerId={}", playerId);
        
        PlayerData playerData = playerService.getPlayerData(playerId);
        if (playerData == null) {
            throw new MessageException(RealmError.EXP_NOT_ENOUGH);
        }
        
        return realmService.cultivate(playerData);
    }
    
    /**
     * 突破境界
     *
     * @param flowContext 请求上下文
     * @return 突破结果
     */
    @ActionMethod(RealmCmd.breakthrough)
    public BreakthroughResp breakthrough(FlowContext flowContext) throws MessageException {
        long playerId = flowContext.getUserId();
        log.info("[突破境界] playerId={}", playerId);
        
        PlayerData playerData = playerService.getPlayerData(playerId);
        if (playerData == null) {
            throw new MessageException(RealmError.EXP_NOT_ENOUGH);
        }
        
        return realmService.breakthrough(playerData);
    }
}
