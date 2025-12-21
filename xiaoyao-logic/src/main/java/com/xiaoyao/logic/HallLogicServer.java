package com.xiaoyao.logic;

import com.iohao.net.framework.core.BarSkeletonBuilder;
import com.iohao.net.framework.core.flow.internal.DebugInOut;
import com.iohao.net.framework.protocol.ServerBuilder;
import com.iohao.net.server.LogicServer;
import com.xiaoyao.logic.player.PlayerAction;
import com.xiaoyao.logic.realm.RealmAction;
import lombok.extern.slf4j.Slf4j;

/**
 * 逻辑服
 * <p>
 * 配置逻辑服的业务框架和 Action 扫描
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class HallLogicServer implements LogicServer {
    
    @Override
    public void settingBarSkeletonBuilder(BarSkeletonBuilder builder) {
        // 扫描 Action 类所在包
        builder.scanActionPackage(PlayerAction.class);
        builder.scanActionPackage(RealmAction.class);
        
        // 添加调试插件 (生产环境可关闭)
        builder.addInOut(new DebugInOut());
        
        log.info("[逻辑服] 业务框架配置完成");
    }
    
    @Override
    public void settingServerBuilder(ServerBuilder builder) {
        // 设置逻辑服名称
        builder.setName("HallLogicServer");
    }
}
