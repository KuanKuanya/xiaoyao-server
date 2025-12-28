package com.xiaoyao.logic.session;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理服务
 * 单实例部署使用内存存储（ConcurrentHashMap）
 * 如需分布式部署，请改用 Redis
 *
 * @author xiaoyao
 */
@Slf4j
public class SessionService {

    /** 登录日志ID缓存: playerId -> loginLogId */
    private final Map<Long, Long> loginLogIdCache = new ConcurrentHashMap<>();

    /** Token缓存: playerId -> token */
    private final Map<Long, String> tokenCache = new ConcurrentHashMap<>();

    /**
     * 保存玩家的登录日志ID
     */
    public void saveLoginLogId(Long playerId, Long loginLogId) {
        if (playerId == null || loginLogId == null) {
            return;
        }
        loginLogIdCache.put(playerId, loginLogId);
        log.debug("[会话管理] 保存loginLogId playerId={} loginLogId={}", playerId, loginLogId);
    }

    /**
     * 获取玩家的登录日志ID
     */
    public Long getLoginLogId(Long playerId) {
        if (playerId == null) {
            return null;
        }
        return loginLogIdCache.get(playerId);
    }

    /**
     * 移除玩家的登录日志ID
     */
    public void removeLoginLogId(Long playerId) {
        if (playerId == null) {
            return;
        }
        loginLogIdCache.remove(playerId);
        log.debug("[会话管理] 移除loginLogId playerId={}", playerId);
    }

    /**
     * 保存玩家Token
     */
    public void saveToken(Long playerId, String token) {
        if (playerId == null || token == null) {
            return;
        }
        tokenCache.put(playerId, token);
        log.debug("[会话管理] 保存Token playerId={}", playerId);
    }

    /**
     * 获取玩家Token
     */
    public String getToken(Long playerId) {
        if (playerId == null) {
            return null;
        }
        return tokenCache.get(playerId);
    }

    /**
     * 验证Token
     */
    public boolean validateToken(Long playerId, String token) {
        if (playerId == null || token == null) {
            return false;
        }
        String savedToken = getToken(playerId);
        return token.equals(savedToken);
    }

    /**
     * 移除玩家会话（登出时调用）
     */
    public void removeSession(Long playerId) {
        if (playerId == null) {
            return;
        }
        loginLogIdCache.remove(playerId);
        tokenCache.remove(playerId);
        log.info("[会话管理] 移除会话 playerId={}", playerId);
    }

    /**
     * 刷新会话过期时间 (内存存储不需要刷新)
     */
    public void refreshSession(Long playerId) {
        // 内存存储不需要刷新TTL
        log.debug("[会话管理] 刷新会话 playerId={}", playerId);
    }
}
