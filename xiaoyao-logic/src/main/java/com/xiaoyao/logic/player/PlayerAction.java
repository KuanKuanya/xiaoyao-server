package com.xiaoyao.logic.player;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.xiaoyao.common.cmd.PlayerCmd;
import com.xiaoyao.common.proto.LoginReq;
import com.xiaoyao.common.proto.LoginResp;
import com.xiaoyao.common.proto.PlayerData;
import lombok.extern.slf4j.Slf4j;

/**
 * 玩家模块 Action
 * <p>
 * 处理玩家登录、信息获取等请求
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@ActionController(PlayerCmd.cmd)
public class PlayerAction {
    
    private final PlayerService playerService = new PlayerService();
    
    /**
     * 登录
     *
     * @param loginReq 登录请求
     * @param flowContext 请求上下文
     * @return 登录响应
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
            flowContext.setUserId(resp.getPlayerId());
            log.info("[登录成功] playerId={}, isNew={}", resp.getPlayerId(), resp.isNew());
        }
        
        return resp;
    }
    
    /**
     * 获取玩家信息
     *
     * @param flowContext 请求上下文
     * @return 玩家数据
     */
    @ActionMethod(PlayerCmd.getInfo)
    public PlayerData getInfo(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[获取玩家信息] playerId={}", playerId);
        
        return playerService.getPlayerData(playerId);
    }
    
    /**
     * 心跳
     *
     * @param flowContext 请求上下文
     * @return 服务器时间戳
     */
    @ActionMethod(PlayerCmd.heartbeat)
    public long heartbeat(FlowContext flowContext) {
        return System.currentTimeMillis();
    }
}
