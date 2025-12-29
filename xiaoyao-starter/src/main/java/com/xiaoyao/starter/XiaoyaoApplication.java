package com.xiaoyao.starter;

import com.iohao.net.app.RunOne;
import com.iohao.net.common.kit.NetworkKit;
import com.iohao.net.extension.spring.ActionFactoryBeanForSpring;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.netty.ExternalMapper;
import com.xiaoyao.logic.HallLogicServer;
import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * 逍遥游服务器启动入口
 * <p>
 * 使用 Spring Boot 3 + ionet 框架
 * 参考官方文档: https://iohao.github.io/ionet/docs/manual/integration_spring
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {
                "com.xiaoyao.logic", // 逻辑层 (Action, Service, Config)
                "com.xiaoyao.starter" // 启动器
})
@org.mybatis.spring.annotation.MapperScan("com.xiaoyao.logic.**")
public class XiaoyaoApplication {

        public static void main(String[] args) {
                // 打印启动信息
                printBanner();

                // 1. 启动 Spring Boot (先启动 Spring，再启动 ionet)
                log.info("[Spring] 正在启动 Spring Boot...");
                var context = SpringApplication.run(XiaoyaoApplication.class, args);
                log.info("[Spring] Spring Boot 启动完成，Bean 数量: {}", context.getBeanDefinitionCount());

                // 2. 启动 ionet 服务器
                startIonetServer();
        }

        /**
         * 关键：将 ActionFactoryBeanForSpring 注册为 Spring Bean
         * 这样 ionet 就会从 Spring 容器中获取 Action 实例，支持 @Resource 注入
         */
        @Bean
        public ActionFactoryBeanForSpring<?> actionFactoryBean() {
                return new ActionFactoryBeanForSpring<>();
        }

        /**
         * 启动 ionet 服务器
         */
        private static void startIonetServer() {
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

                // 使用 RunOne 启动 ionet
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
                                ║     Powered by Spring Boot 3 + ionet Framework                ║
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

                                  ✅ 服务器启动成功！(Spring Boot 3 + ionet)

                                  📡 WebSocket 地址: %s
                                  🖥️  本机 IP: %s

                                  💡 客户端连接示例:
                                     new WebSocket("%s")

                                ═══════════════════════════════════════════════════════════════════
                                """.formatted(wsUrl, localIp, wsUrl);

                log.info(info);
        }
}
