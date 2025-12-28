package com.xiaoyao.logic.player;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.PlayerCmd;
import com.xiaoyao.common.proto.LoginReq;
import com.xiaoyao.common.proto.LoginResp;
import com.xiaoyao.common.proto.PlayerData;
import com.xiaoyao.logic.log.LoginLogService;
import com.xiaoyao.logic.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 玩家模块 Action
 * <p>
 * 采用构造函数注入，由 XiaoyaoApplication 手动注册
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(PlayerCmd.cmd)
public class PlayerAction {

    private final PlayerService playerService;
    private final LoginLogService loginLogService;
    private final SessionService sessionService;

    @ActionMethod(PlayerCmd.login)
    public LoginResp login(LoginReq loginReq, FlowContext flowContext) {
        log.info("[登录] platform={}, deviceId={}, version={}",
                loginReq.getPlatform(),
                loginReq.getDeviceId(),
                loginReq.getClientVersion());

        LoginResp resp = playerService.login(loginReq);

        if (resp.getPlayerId() > 0) {
            flowContext.bindingUserId(resp.getPlayerId());
            log.info("[登录成功] playerId={}, isNew={}", resp.getPlayerId(), resp.isNew());
        }

        return resp;
    }

    @ActionMethod(PlayerCmd.getInfo)
    public PlayerData getInfo(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        return playerService.getPlayerData(playerId);
    }

    @ActionMethod(PlayerCmd.heartbeat)
    public long heartbeat(FlowContext flowContext) {
        return System.currentTimeMillis();
    }

    @ActionMethod(PlayerCmd.logout)
    public void logout(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[登出] playerId={}", playerId);
    }
}
