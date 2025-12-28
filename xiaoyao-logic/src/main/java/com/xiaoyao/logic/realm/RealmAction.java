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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 境界模块 Action
 * <p>
 * 处理修炼、突破、飞升等请求
 * 使用 Spring @Resource 依赖注入
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(RealmCmd.cmd)
public class RealmAction {

    @Resource
    private RealmService realmService;

    @Resource
    private PlayerService playerService;

    /**
     * 领取修炼收益 (挂机经验)
     */
    @ActionMethod(RealmCmd.cultivate)
    public CultivateResp cultivate(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[修炼] playerId={}", playerId);

        PlayerData playerData = playerService.getPlayerData(playerId);
        if (playerData == null) {
            throw new MessageException(RealmError.PLAYER_NOT_FOUND);
        }

        return realmService.cultivate(playerData);
    }

    /**
     * 突破境界
     */
    @ActionMethod(RealmCmd.breakthrough)
    public BreakthroughResp breakthrough(FlowContext flowContext) throws MessageException {
        long playerId = flowContext.getUserId();
        log.info("[突破境界] playerId={}", playerId);

        PlayerData playerData = playerService.getPlayerData(playerId);
        if (playerData == null) {
            throw new MessageException(RealmError.PLAYER_NOT_FOUND);
        }

        return realmService.breakthrough(playerData);
    }

    /**
     * 飞升
     */
    @ActionMethod(RealmCmd.ascend)
    public BreakthroughResp ascend(FlowContext flowContext) throws MessageException {
        long playerId = flowContext.getUserId();
        log.info("[飞升] playerId={}", playerId);

        PlayerData playerData = playerService.getPlayerData(playerId);
        if (playerData == null) {
            throw new MessageException(RealmError.PLAYER_NOT_FOUND);
        }

        return realmService.ascend(playerData);
    }
}
