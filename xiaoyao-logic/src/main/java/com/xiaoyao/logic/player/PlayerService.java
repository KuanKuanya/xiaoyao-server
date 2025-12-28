package com.xiaoyao.logic.player;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.xiaoyao.common.proto.LoginReq;
import com.xiaoyao.common.proto.LoginResp;
import com.xiaoyao.common.proto.PlayerData;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家服务 (简化版 - 内存存储)
 * <p>
 * 暂时使用内存存储，先验证前后端联调
 * TODO: 后续改为 MySQL 持久化
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class PlayerService {

    /** 玩家数据缓存 (deviceId -> PlayerData) */
    private final Map<String, PlayerData> playerCache = new ConcurrentHashMap<>();

    /** 玩家ID缓存 (deviceId -> playerId) */
    private final Map<String, Long> idCache = new ConcurrentHashMap<>();

    /**
     * 玩家登录
     * 如果玩家不存在则自动注册
     */
    public LoginResp login(LoginReq req) {
        String deviceId = req.getDeviceId();
        int platform = req.getPlatform();

        LoginResp resp = new LoginResp();

        // 检查是否已存在玩家
        Long existingId = idCache.get(deviceId);
        if (existingId != null) {
            // 已存在，返回登录信息
            PlayerData playerData = playerCache.get(deviceId);
            resp.setPlayerId(existingId);
            resp.setNew(false);
            resp.setToken(generateToken(existingId));
            resp.setPlayerData(playerData);
            resp.setServerTime(System.currentTimeMillis());
            log.info("[登录] 老玩家登录成功: playerId={}, deviceId={}", existingId, deviceId);
            return resp;
        }

        // 新玩家，自动注册
        long playerId = IdUtil.getSnowflakeNextId();

        // 创建玩家数据
        PlayerData playerData = createNewPlayer(playerId, deviceId, platform);

        // 存入缓存
        idCache.put(deviceId, playerId);
        playerCache.put(deviceId, playerData);

        // 返回登录响应
        resp.setPlayerId(playerId);
        resp.setNew(true);
        resp.setToken(generateToken(playerId));
        resp.setPlayerData(playerData);
        resp.setServerTime(System.currentTimeMillis());

        log.info("[登录] 新玩家注册成功: playerId={}, deviceId={}", playerId, deviceId);
        return resp;
    }

    /**
     * 获取玩家数据
     */
    public PlayerData getPlayerData(long playerId) {
        // 遍历查找
        for (Map.Entry<String, Long> entry : idCache.entrySet()) {
            if (entry.getValue() == playerId) {
                return playerCache.get(entry.getKey());
            }
        }
        log.warn("[获取玩家] 玩家不存在: playerId={}", playerId);
        return null;
    }

    /**
     * 创建新玩家 (使用 PlayerData 实际字段)
     */
    private PlayerData createNewPlayer(long playerId, String deviceId, int platform) {
        PlayerData data = new PlayerData();
        data.setPlayerId(playerId);
        data.setNickname("修士" + RandomUtil.randomNumbers(6));
        data.setAvatar("");
        data.setRealmId(1); // 凡人境界
        data.setRealmExp(0L);
        data.setAscended(false);
        data.setSpiritStone(1000L); // 初始灵石
        data.setJade(10L); // 初始仙玉
        data.setBindJade(0L);
        data.setVipLevel(0);
        data.setCombatPower(100L);
        data.setLastCultivateTime(System.currentTimeMillis());
        return data;
    }

    /**
     * 生成 token
     */
    private String generateToken(long playerId) {
        return "token_" + playerId + "_" + System.currentTimeMillis();
    }
}
