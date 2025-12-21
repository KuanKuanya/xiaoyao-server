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
 * 玩家服务
 * <p>
 * 处理玩家相关业务逻辑
 * TODO: 接入数据库后替换内存存储
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class PlayerService {
    
    /** 玩家数据缓存 (临时内存存储，后续改为 Redis + MySQL) */
    private static final Map<Long, PlayerData> PLAYER_CACHE = new ConcurrentHashMap<>();
    
    /** 设备ID -> 玩家ID 映射 */
    private static final Map<String, Long> DEVICE_PLAYER_MAP = new ConcurrentHashMap<>();
    
    /**
     * 玩家登录
     *
     * @param req 登录请求
     * @return 登录响应
     */
    public LoginResp login(LoginReq req) {
        LoginResp resp = new LoginResp();
        resp.setServerTime(System.currentTimeMillis());
        
        String deviceId = req.getDeviceId();
        Long existingPlayerId = DEVICE_PLAYER_MAP.get(deviceId);
        
        if (existingPlayerId != null) {
            // 老玩家登录
            PlayerData playerData = PLAYER_CACHE.get(existingPlayerId);
            if (playerData != null) {
                // 更新最后登录时间
                playerData.setLastCultivateTime(System.currentTimeMillis());
                
                resp.setPlayerId(existingPlayerId);
                resp.setNew(false);
                resp.setPlayerData(playerData);
                resp.setToken(generateToken(existingPlayerId));
                return resp;
            }
        }
        
        // 新玩家注册
        long newPlayerId = IdUtil.getSnowflakeNextId();
        PlayerData newPlayer = createNewPlayer(newPlayerId, req);
        
        PLAYER_CACHE.put(newPlayerId, newPlayer);
        DEVICE_PLAYER_MAP.put(deviceId, newPlayerId);
        
        resp.setPlayerId(newPlayerId);
        resp.setNew(true);
        resp.setPlayerData(newPlayer);
        resp.setToken(generateToken(newPlayerId));
        
        log.info("[新玩家注册] playerId={}, nickname={}", newPlayerId, newPlayer.getNickname());
        
        return resp;
    }
    
    /**
     * 获取玩家数据
     *
     * @param playerId 玩家ID
     * @return 玩家数据
     */
    public PlayerData getPlayerData(long playerId) {
        return PLAYER_CACHE.get(playerId);
    }
    
    /**
     * 更新玩家数据
     *
     * @param playerData 玩家数据
     */
    public void updatePlayerData(PlayerData playerData) {
        PLAYER_CACHE.put(playerData.getPlayerId(), playerData);
    }
    
    /**
     * 创建新玩家
     */
    private PlayerData createNewPlayer(long playerId, LoginReq req) {
        PlayerData player = new PlayerData();
        player.setPlayerId(playerId);
        player.setNickname("道友" + RandomUtil.randomNumbers(6));
        player.setAvatar("");
        player.setRealmId(1);  // 练气期一层
        player.setRealmExp(0);
        player.setAscended(false);
        player.setSpiritStone(1000);  // 初始灵石
        player.setJade(100);  // 初始仙玉
        player.setBindJade(0);
        player.setVipLevel(0);
        player.setCombatPower(100);
        player.setLastCultivateTime(System.currentTimeMillis());
        return player;
    }
    
    /**
     * 生成登录Token
     */
    private String generateToken(long playerId) {
        // TODO: 使用 JWT 或其他安全方式生成 Token
        return IdUtil.fastSimpleUUID();
    }
}
