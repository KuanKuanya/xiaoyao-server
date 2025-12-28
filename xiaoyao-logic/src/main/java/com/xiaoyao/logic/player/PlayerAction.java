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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 玩家模块 Action
 * <p>
 * 处理玩家登录、信息获取等请求
 * 使用 Spring @Resource 依赖注入
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(PlayerCmd.cmd)
public class PlayerAction {

    @Resource
    private PlayerService playerService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private SessionService sessionService;

    /**
     * 登录
     */
    @ActionMethod(PlayerCmd.login)
    public LoginResp login(LoginReq loginReq, FlowContext flowContext) {
        log.info("[登录] platform={}, deviceId={}, version={}",
                loginReq.getPlatform(),
                loginReq.getDeviceId(),
                loginReq.getClientVersion());

        // 执行登录逻辑
        LoginResp resp = playerService.login(loginReq);

        // 绑定用户ID到连接
        if (resp.getPlayerId() > 0) {
            flowContext.bindingUserId(resp.getPlayerId());
            log.info("[登录成功] playerId={}, isNew={}", resp.getPlayerId(), resp.isNew());
        }

        return resp;
    }

    /**
     * 获取玩家信息
     */
    @ActionMethod(PlayerCmd.getInfo)
    public PlayerData getInfo(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[获取玩家信息] playerId={}", playerId);
        return playerService.getPlayerData(playerId);
    }

    /**
     * 心跳
     */
    @ActionMethod(PlayerCmd.heartbeat)
    public long heartbeat(FlowContext flowContext) {
        return System.currentTimeMillis();
    }

    /**
     * 登出
     */
    @ActionMethod(PlayerCmd.logout)
    public void logout(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[登出] playerId={}", playerId);
    }
}
