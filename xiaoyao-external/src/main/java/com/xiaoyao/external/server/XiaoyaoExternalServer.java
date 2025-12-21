package com.xiaoyao.external.server;

import com.iohao.game.bolt.broker.core.ExternalJoinEnum;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.netty.DefaultExternalServer;
import com.iohao.game.external.core.netty.DefaultExternalServerBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 对外服启动器
 * <p>
 * 配置 WebSocket 网关，处理客户端连接
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class XiaoyaoExternalServer {
    
    /** WebSocket 端口 */
    private static final int WS_PORT = 10100;
    
    /**
     * 创建对外服
     *
     * @return 对外服实例
     */
    public static ExternalServer createExternalServer() {
        // 游戏对外服端口
        int port = WS_PORT;
        
        // 创建对外服构建器
        DefaultExternalServerBuilder builder = DefaultExternalServer.newBuilder(port)
                // 连接方式：WebSocket
                .externalJoinEnum(ExternalJoinEnum.WEBSOCKET);
        
        // 全局配置
        ExternalGlobalConfig.accessAuthenticationHook.setVerifyIdentity(false);
        
        log.info("[对外服] 配置完成 - WebSocket端口: {}", port);
        
        return builder.build();
    }
    
    /**
     * 获取 WebSocket 连接地址
     */
    public static String getWebSocketUrl() {
        return "ws://127.0.0.1:" + WS_PORT + "/websocket";
    }
}
