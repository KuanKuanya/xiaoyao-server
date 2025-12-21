package com.xiaoyao.starter;

import com.iohao.net.app.RunOne;
import com.iohao.net.common.kit.NetworkKit;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.netty.ExternalMapper;
import com.xiaoyao.logic.HallLogicServer;
import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 逍遥游服务器启动入口
 * <p>
 * 启动模式：多服单进程 (开发模式)
 * - 对外服 (WebSocket)
 * - 逻辑服
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class XiaoyaoApplication {
    
    public static void main(String[] args) {
        // 打印启动信息
        printBanner();
        
        // 创建对外服 (WebSocket 端口 10100)
        int port = ExternalGlobalConfig.externalPort;
        var externalServer = ExternalMapper.builder(port).build();
        
        // 创建嵌入式 Aeron MediaDriver
        var mediaDriverCtx = new MediaDriver.Context()
                .dirDeleteOnStart(true)
                .dirDeleteOnShutdown(true);
        var mediaDriver = MediaDriver.launchEmbedded(mediaDriverCtx);
        
        // 创建 Aeron 连接
        var aeronCtx = new Aeron.Context()
                .aeronDirectoryName(mediaDriver.aeronDirectoryName());
        var aeron = Aeron.connect(aeronCtx);
        
        // 使用 RunOne 启动
        new RunOne()
                .setAeron(aeron)
                .enableCenterServer()
                .setExternalServer(externalServer)
                .setLogicServerList(List.of(new HallLogicServer()))
                .startup();
        
        // 打印启动完成信息
        printStartupInfo(port);
    }
    
    /**
     * 打印启动横幅
     */
    private static void printBanner() {
        String banner = """
                
                ╔═══════════════════════════════════════════════════════════════╗
                ║                                                               ║
                ║     逍遥游：自动悟道 - 游戏服务器                                 ║
                ║     Xiaoyao Game Server                                       ║
                ║                                                               ║
                ║     Powered by ionet Framework                                ║
                ║                                                               ║
                ╚═══════════════════════════════════════════════════════════════╝
                """;
        log.info(banner);
    }
    
    /**
     * 打印启动完成信息
     */
    private static void printStartupInfo(int port) {
        String localIp = NetworkKit.LOCAL_IP;
        String wsUrl = "ws://127.0.0.1:" + port + "/websocket";
        
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
