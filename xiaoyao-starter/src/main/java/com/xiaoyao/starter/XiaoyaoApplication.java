package com.xiaoyao.starter;

import com.iohao.game.action.skeleton.core.doc.BarSkeletonDoc;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.common.kit.NetworkKit;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.simple.SimpleHelper;
import com.xiaoyao.external.server.XiaoyaoExternalServer;
import com.xiaoyao.logic.LogicServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 修仙游戏服务器启动入口
 * <p>
 * 启动模式：多服单进程 (开发模式)
 * - 对外服 (WebSocket)
 * - 逻辑服
 * - Broker (游戏网关)
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class XiaoyaoApplication {
    
    public static void main(String[] args) {
        // 打印启动信息
        printBanner();
        
        // 创建对外服
        ExternalServer externalServer = XiaoyaoExternalServer.createExternalServer();
        
        // 创建逻辑服
        LogicServer logicServer = new LogicServer();
        
        // 使用 SimpleHelper 简化启动
        // 多服单进程：对外服、逻辑服、Broker 都在一个进程中启动
        SimpleHelper.run(externalServer, List.of(logicServer));
        
        // 生成接口文档
        generateDoc();
        
        // 打印启动完成信息
        printStartupInfo();
    }
    
    /**
     * 打印启动横幅
     */
    private static void printBanner() {
        String banner = """
                
                ╔═══════════════════════════════════════════════════════════════╗
                ║                                                               ║
                ║     水墨修仙：长生路 - 游戏服务器                                 ║
                ║     Xiaoyao Game Server                                       ║
                ║                                                               ║
                ║     Powered by ioGame Framework                               ║
                ║                                                               ║
                ╚═══════════════════════════════════════════════════════════════╝
                """;
        log.info(banner);
    }
    
    /**
     * 生成接口文档
     */
    private static void generateDoc() {
        // 开发阶段可以生成接口文档
        // BarSkeletonDoc.me().buildDoc();
    }
    
    /**
     * 打印启动完成信息
     */
    private static void printStartupInfo() {
        String localIp = NetworkKit.LOCAL_IP;
        String wsUrl = XiaoyaoExternalServer.getWebSocketUrl();
        
        String info = """
                
                ═══════════════════════════════════════════════════════════════════
                
                  ✅ 服务器启动成功！
                
                  📡 WebSocket 地址: %s
                  🖥️  本机 IP: %s
                
                  💡 客户端连接示例:
                     new WebSocket("%s")
                
                ═══════════════════════════════════════════════════════════════════
                """.formatted(wsUrl, localIp, wsUrl);
        
        log.info(info);
    }
}
