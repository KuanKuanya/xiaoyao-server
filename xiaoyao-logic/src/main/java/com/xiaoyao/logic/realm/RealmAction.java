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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 境界模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(RealmCmd.cmd)
public class RealmAction {

    private final RealmService realmService;
    private final PlayerService playerService;

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
