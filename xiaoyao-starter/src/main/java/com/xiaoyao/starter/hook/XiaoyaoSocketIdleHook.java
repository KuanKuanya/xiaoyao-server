package com.xiaoyao.starter.hook;

import com.iohao.net.external.core.netty.hook.DefaultSocketIdleHook;
import com.iohao.net.external.core.netty.hook.SocketIdleHook;
import com.iohao.net.external.core.session.UserSession;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Socket 空闲钩子实现
 * <p>
 * 处理客户端心跳超时的回调逻辑
 *
 * @author xiaoyao
 */
@Slf4j
public final class XiaoyaoSocketIdleHook implements SocketIdleHook {

    private final DefaultSocketIdleHook defaultSocketIdleHook = new DefaultSocketIdleHook();

    @Override
    public boolean callback(UserSession userSession, IdleStateEvent event) {
        // 使用默认的心跳超时处理逻辑
        // 返回 true 表示关闭连接
        long userId = userSession.getUserId();
        log.warn("[心跳超时] 用户 {} 超时未发送心跳，将关闭连接", userId);

        return defaultSocketIdleHook.callback(userSession, event);
    }
}
