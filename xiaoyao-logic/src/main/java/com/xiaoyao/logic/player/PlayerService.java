package com.xiaoyao.logic.player;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.xiaoyao.common.proto.LoginReq;
import com.xiaoyao.common.proto.LoginResp;
import com.xiaoyao.common.proto.PlayerData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家服务
 * <p>
 * 使用 MySQL 数据库持久化 + 内存缓存
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class PlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private PlayerProgressMapper progressMapper;

    @Resource
    private PlayerCurrencyMapper currencyMapper;

    /** 玩家数据缓存 (playerId -> PlayerData) */
    private final Map<Long, PlayerData> playerCache = new ConcurrentHashMap<>();

    /**
     * 玩家登录
     * 如果玩家不存在则自动注册
     */
    @Transactional
    public LoginResp login(LoginReq req) {
        String deviceId = req.getDeviceId();
        int platform = req.getPlatform();

        LoginResp resp = new LoginResp();

        // 先查数据库
        PlayerEntity existingPlayer = playerMapper.selectByDeviceId(deviceId);
        if (existingPlayer != null) {
            // 已存在玩家，加载数据
            Long playerId = existingPlayer.getId();
            PlayerData playerData = loadPlayerData(playerId, existingPlayer);

            // 更新登录时间
            existingPlayer.setLastLoginTime(LocalDateTime.now());
            playerMapper.update(existingPlayer);

            // 缓存
            playerCache.put(playerId, playerData);

            resp.setPlayerId(playerId);
            resp.setNew(false);
            resp.setToken(generateToken(playerId));
            resp.setPlayerData(playerData);
            resp.setServerTime(System.currentTimeMillis());
            log.info("[登录] 老玩家登录成功: playerId={}, deviceId={}", playerId, deviceId);
            return resp;
        }

        // 新玩家，自动注册
        long playerId = IdUtil.getSnowflakeNextId();

        // 创建玩家账户
        PlayerEntity player = new PlayerEntity();
        player.setId(playerId);
        player.setDeviceId(deviceId);
        player.setPlatform(platform);
        player.setNickname("修士" + RandomUtil.randomNumbers(6));
        player.setAvatar("");
        player.setVipLevel(0);
        player.setLastLoginTime(LocalDateTime.now());
        playerMapper.insert(player);

        // 创建玩家进度
        PlayerProgressEntity progress = new PlayerProgressEntity();
        progress.setPlayerId(playerId);
        progress.setRealmId(1);
        progress.setRealmExp(0L);
        progress.setTotalExp(0L);
        progress.setAscended(false);
        progress.setTowerFloor(0);
        progress.setUnlockedMapId(1);
        progress.setSpiritArrayLevel(1);
        progress.setLastCultivateTime(System.currentTimeMillis());
        progressMapper.insert(progress);

        // 创建玩家货币
        PlayerCurrencyEntity currency = new PlayerCurrencyEntity();
        currency.setPlayerId(playerId);
        currency.setSpiritStone(1000L);
        currency.setJade(10);
        currency.setBindJade(0);
        currencyMapper.insert(currency);

        // 构建玩家数据
        PlayerData playerData = buildPlayerData(player, progress, currency);
        playerCache.put(playerId, playerData);

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
     * 获取玩家数据 (优先从缓存)
     */
    public PlayerData getPlayerData(long playerId) {
        // 先查缓存
        PlayerData cached = playerCache.get(playerId);
        if (cached != null) {
            return cached;
        }

        // 缓存未命中，查数据库
        PlayerEntity player = playerMapper.selectOneById(playerId);
        if (player == null) {
            log.warn("[获取玩家] 玩家不存在: playerId={}", playerId);
            return null;
        }

        PlayerData playerData = loadPlayerData(playerId, player);
        playerCache.put(playerId, playerData);
        return playerData;
    }

    /**
     * 保存玩家进度到数据库
     */
    @Transactional
    public void saveProgress(long playerId, PlayerData playerData) {
        // 更新进度表
        PlayerProgressEntity progress = progressMapper.selectOneById(playerId);
        if (progress != null) {
            progress.setRealmId(playerData.getRealmId());
            progress.setRealmExp(playerData.getRealmExp());
            progress.setAscended(playerData.isAscended());
            progress.setLastCultivateTime(playerData.getLastCultivateTime());
            progressMapper.update(progress);
            log.debug("[保存进度] playerId={}, realmId={}, exp={}", playerId, progress.getRealmId(),
                    progress.getRealmExp());
        }
    }

    /**
     * 保存玩家货币到数据库
     */
    @Transactional
    public void saveCurrency(long playerId, PlayerData playerData) {
        PlayerCurrencyEntity currency = currencyMapper.selectOneById(playerId);
        if (currency != null) {
            currency.setSpiritStone(playerData.getSpiritStone());
            currency.setJade((int) playerData.getJade());
            currency.setBindJade((int) playerData.getBindJade());
            currencyMapper.update(currency);
            log.debug("[保存货币] playerId={}, spiritStone={}", playerId, currency.getSpiritStone());
        }
    }

    /**
     * 从数据库加载玩家数据
     */
    private PlayerData loadPlayerData(long playerId, PlayerEntity player) {
        PlayerProgressEntity progress = progressMapper.selectOneById(playerId);
        PlayerCurrencyEntity currency = currencyMapper.selectOneById(playerId);
        return buildPlayerData(player, progress, currency);
    }

    /**
     * 构建 PlayerData 对象
     */
    private PlayerData buildPlayerData(PlayerEntity player, PlayerProgressEntity progress,
            PlayerCurrencyEntity currency) {
        PlayerData data = new PlayerData();
        data.setPlayerId(player.getId());
        data.setNickname(player.getNickname());
        data.setAvatar(player.getAvatar() != null ? player.getAvatar() : "");
        data.setVipLevel(player.getVipLevel() != null ? player.getVipLevel() : 0);

        if (progress != null) {
            data.setRealmId(progress.getRealmId() != null ? progress.getRealmId() : 1);
            data.setRealmExp(progress.getRealmExp() != null ? progress.getRealmExp() : 0L);
            data.setAscended(progress.getAscended() != null && progress.getAscended());
            data.setLastCultivateTime(progress.getLastCultivateTime() != null ? progress.getLastCultivateTime()
                    : System.currentTimeMillis());
        } else {
            data.setRealmId(1);
            data.setRealmExp(0L);
            data.setAscended(false);
            data.setLastCultivateTime(System.currentTimeMillis());
        }

        if (currency != null) {
            data.setSpiritStone(currency.getSpiritStone() != null ? currency.getSpiritStone() : 0L);
            data.setJade(currency.getJade() != null ? currency.getJade() : 0L);
            data.setBindJade(currency.getBindJade() != null ? currency.getBindJade() : 0L);
        } else {
            data.setSpiritStone(0L);
            data.setJade(0L);
            data.setBindJade(0L);
        }

        data.setCombatPower(100L);
        return data;
    }

    /**
     * 生成 token
     */
    private String generateToken(long playerId) {
        return "token_" + playerId + "_" + System.currentTimeMillis();
    }
}
