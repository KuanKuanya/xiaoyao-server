package com.xiaoyao.logic;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.action.skeleton.core.flow.internal.DebugInOut;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.xiaoyao.logic.player.PlayerAction;
import com.xiaoyao.logic.realm.RealmAction;
import lombok.extern.slf4j.Slf4j;

/**
 * 逻辑服启动器
 * <p>
 * 配置逻辑服的业务框架和 Action 扫描
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class LogicServer extends AbstractBrokerClientStartup {
    
    @Override
    public BarSkeleton createBarSkeleton() {
        // 创建业务框架构建器
        BarSkeletonBuilderParamConfig config = new BarSkeletonBuilderParamConfig()
                // 扫描 Action 类
                .scanActionPackage(PlayerAction.class)
                .scanActionPackage(RealmAction.class);
        
        // 构建业务框架
        BarSkeleton barSkeleton = config.createBuilder()
                // 添加调试插件 (生产环境可关闭)
                .addInOut(new DebugInOut())
                .build();
        
        log.info("[逻辑服] 业务框架初始化完成");
        
        return barSkeleton;
    }
    
    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        // 创建逻辑服客户端
        BrokerClientBuilder builder = BrokerClient.newBuilder()
                // 逻辑服的唯一标识
                .appName("xiaoyao-logic")
                // 逻辑服的ID (同类型逻辑服，ID 不同)
                .id("xiaoyao-logic-1");
        
        return builder;
    }
}
