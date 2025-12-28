package com.xiaoyao.starter;

import com.iohao.net.app.RunOne;
import com.iohao.net.extension.spring.ActionFactoryBeanForSpring;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.config.ExternalGlobalConfig;
import com.iohao.net.external.core.netty.ExternalMapper;
import com.xiaoyao.logic.HallLogicServer;
import com.xiaoyao.logic.combat.CombatAction;
import com.xiaoyao.logic.combat.CombatService;
import com.xiaoyao.logic.inventory.InventoryAction;
import com.xiaoyao.logic.inventory.InventoryService;
import com.xiaoyao.logic.log.LoginLogService;
import com.xiaoyao.logic.player.PlayerAction;
import com.xiaoyao.logic.player.PlayerService;
import com.xiaoyao.logic.rank.RankAction;
import com.xiaoyao.logic.rank.RankService;
import com.xiaoyao.logic.realm.RealmAction;
import com.xiaoyao.logic.realm.RealmService;
import com.xiaoyao.logic.session.SessionService;
import com.xiaoyao.logic.shop.ShopAction;
import com.xiaoyao.logic.shop.ShopService;
import com.xiaoyao.logic.skill.SkillAction;
import com.xiaoyao.logic.skill.SkillService;
import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 逍遥游服务器启动入口
 * <p>
 * 架构说明：
 * 由于 JDK 25 (class version 69) 超出了当前 Spring ASM 支持范围，
 * 我们采用 "显式配置" (Explicit Configuration) 模式，手动注册业务 Bean。
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@SpringBootApplication
public class XiaoyaoApplication {

        public static void main(String[] args) {
                // 核心属性：允许 Spring 忽略无法解析的字节码版本
                System.setProperty("spring.classformat.ignore", "true");

                printBanner();

                log.info("[Spring] 正在启动 Spring Boot...");
                var context = SpringApplication.run(XiaoyaoApplication.class, args);
                log.info("[Spring] 启动完成. Bean总量: {}", context.getBeanDefinitionCount());

                startIonetServer(context);
        }

        // ==================== 1. 基础设施 Bean ====================

        @Bean
        public ActionFactoryBeanForSpring<?> actionFactoryBean() {
                return new ActionFactoryBeanForSpring<>();
        }

        // ==================== ionet 启动逻辑 ====================

        private static void startIonetServer(ApplicationContext context) {
                int port = ExternalGlobalConfig.externalPort;
                var externalServer = ExternalMapper.builder(port).build();

                var mediaDriverCtx = new MediaDriver.Context()
                                .dirDeleteOnStart(true)
                                .dirDeleteOnShutdown(true);
                var mediaDriver = MediaDriver.launchEmbedded(mediaDriverCtx);

                var aeronCtx = new Aeron.Context()
                                .aeronDirectoryName(mediaDriver.aeronDirectoryName());
                var aeron = Aeron.connect(aeronCtx);

                // 获取 Action 工厂 (Spring 管理)
                var actionFactoryBean = context.getBean(ActionFactoryBeanForSpring.class);

                new RunOne()
                                .setAeron(aeron)
                                .enableCenterServer()
                                .setExternalServer(externalServer)
                                .setLogicServerList(List.of(new HallLogicServer(actionFactoryBean)))
                                .startup();

                printStartupInfo(port);
        }

        private static void printBanner() {
                String banner = """

                                ╔═══════════════════════════════════════════════════════════════╗
                                ║                                                               ║
                                ║     逍遥游：自动悟道 - 游戏服务器                                 ║
                                ║     Xiaoyao Game Server                                       ║
                                ║                                                               ║
                                ║     JDK 25 + Spring Boot 3.4 + Manual Configuration           ║
                                ║                                                               ║
                                ╚═══════════════════════════════════════════════════════════════╝
                                """;
                log.info(banner);
        }

        private static void printStartupInfo(int port) {
                String wsUrl = "ws://127.0.0.1:" + port + "/websocket";
                log.info("\n✅ 服务器启动成功！WebSocket: {}", wsUrl);
        }
}
