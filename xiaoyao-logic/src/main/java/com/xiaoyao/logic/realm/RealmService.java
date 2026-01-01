package com.xiaoyao.logic.realm;

import com.iohao.net.framework.core.exception.MessageException;
import com.xiaoyao.common.error.RealmError;
import com.xiaoyao.common.proto.BreakthroughResp;
import com.xiaoyao.common.proto.CultivateResp;
import com.xiaoyao.common.proto.PlayerData;
import com.xiaoyao.logic.config.GameConfigService;
import com.xiaoyao.logic.config.RealmConfigEntity;
import com.xiaoyao.logic.player.PlayerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 境界服务
 * <p>
 * 处理修炼、突破、飞升等业务逻辑
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class RealmService {

    @Resource
    private PlayerService playerService;

    @Resource
    private GameConfigService configService;

    /** 最大境界数量 */
    private static final int MAX_REALM = 108;

    /** 每秒基础经验 */
    private static final long BASE_EXP_PER_SECOND = 10;

    /**
     * 修炼 - 计算并领取挂机经验
     *
     * @param playerData 玩家数据
     * @return 修炼收益
     */
    public CultivateResp cultivate(PlayerData playerData) {
        long now = System.currentTimeMillis();
        long lastTime = playerData.getLastCultivateTime();

        // 计算挂机时长 (秒)
        long duration = Math.max(0, (now - lastTime) / 1000);

        // 根据境界配置获取经验倍率
        int realmId = playerData.getRealmId();
        double expRate = configService.getExpRate(realmId);

        // 计算获得的经验
        long expGain = (long) (duration * BASE_EXP_PER_SECOND * expRate);

        // 更新玩家数据
        long newExp = playerData.getRealmExp() + expGain;
        playerData.setRealmExp(newExp);
        playerData.setLastCultivateTime(now);

        // 保存到数据库
        playerService.saveProgress(playerData.getPlayerId(), playerData);

        // 构建响应
        CultivateResp resp = new CultivateResp();
        resp.setExpGain(expGain);
        resp.setCurrentExp(newExp);
        resp.setRealmId(realmId);
        resp.setDuration(duration);
        resp.setNextCultivateTime(now);

        log.info("[修炼收益] playerId={}, duration={}s, expGain={}",
                playerData.getPlayerId(), duration, expGain);

        return resp;
    }

    /**
     * 突破境界
     *
     * @param playerData 玩家数据
     * @return 突破结果
     */
    public BreakthroughResp breakthrough(PlayerData playerData) throws MessageException {
        int currentRealmId = playerData.getRealmId();

        // 检查是否已达最高境界
        if (currentRealmId >= MAX_REALM) {
            throw new MessageException(RealmError.REALM_MAX);
        }

        // 获取突破所需经验
        long requiredExp = getRequiredExp(currentRealmId);

        // 检查经验是否足够
        if (playerData.getRealmExp() < requiredExp) {
            throw new MessageException(RealmError.EXP_NOT_FULL);
        }

        // 执行突破
        int newRealmId = currentRealmId + 1;
        playerData.setRealmId(newRealmId);
        playerData.setRealmExp(playerData.getRealmExp() - requiredExp);

        // 计算属性加成
        long atkBonus = getAtkBonus(newRealmId);
        long defBonus = getDefBonus(newRealmId);
        long hpBonus = getHpBonus(newRealmId);

        // 更新战斗力
        long newCombatPower = calculateCombatPower(playerData);
        playerData.setCombatPower(newCombatPower);

        // 构建响应
        BreakthroughResp resp = new BreakthroughResp();
        resp.setSuccess(true);
        resp.setNewRealmId(newRealmId);
        resp.setNewRealmName(getRealmName(newRealmId));
        resp.setAtkBonus(atkBonus);
        resp.setDefBonus(defBonus);
        resp.setHpBonus(hpBonus);
        resp.setCombatPower(newCombatPower);

        log.info("[突破成功] playerId={}, newRealm={} {}",
                playerData.getPlayerId(), newRealmId, resp.getNewRealmName());

        return resp;
    }

    /**
     * 飞升 (进入仙人境界)
     *
     * @param playerData 玩家数据
     * @return 飞升结果
     */
    public BreakthroughResp ascend(PlayerData playerData) throws MessageException {
        int currentRealmId = playerData.getRealmId();

        // 检查是否已飞升
        if (playerData.isAscended()) {
            throw new MessageException(RealmError.ALREADY_ASCENDED);
        }

        // 检查是否达到飞升条件 (渡劫期圆满，realmId=90)
        if (currentRealmId < 90) {
            throw new MessageException(RealmError.REALM_NOT_ENOUGH);
        }

        // 执行飞升
        playerData.setAscended(true);
        int newRealmId = 91; // 地仙一层
        playerData.setRealmId(newRealmId);
        playerData.setRealmExp(0);

        // 飞升奖励
        long bonusSpiritStone = 100000;
        long bonusJade = 1000;
        playerData.setSpiritStone(playerData.getSpiritStone() + bonusSpiritStone);
        playerData.setJade(playerData.getJade() + bonusJade);

        // 更新战斗力
        long newCombatPower = calculateCombatPower(playerData) * 10;
        playerData.setCombatPower(newCombatPower);

        // 构建响应
        BreakthroughResp resp = new BreakthroughResp();
        resp.setSuccess(true);
        resp.setNewRealmId(newRealmId);
        resp.setNewRealmName(getRealmName(newRealmId));
        resp.setAtkBonus(getAtkBonus(newRealmId));
        resp.setDefBonus(getDefBonus(newRealmId));
        resp.setHpBonus(getHpBonus(newRealmId));
        resp.setCombatPower(newCombatPower);

        log.info("[飞升成功] playerId={}, newRealm={} {}",
                playerData.getPlayerId(), newRealmId, resp.getNewRealmName());

        return resp;
    }

    /**
     * 获取境界名称
     */
    public String getRealmName(int realmId) {
        return configService.getRealmName(realmId);
    }

    /**
     * 获取突破所需经验
     */
    private long getRequiredExp(int realmId) {
        return configService.getRequiredExp(realmId);
    }

    /**
     * 获取攻击力加成
     */
    private long getAtkBonus(int realmId) {
        RealmConfigEntity cfg = configService.getRealmConfig(realmId);
        return cfg != null ? cfg.getBaseAtk() : realmId * 10L;
    }

    /**
     * 获取防御力加成
     */
    private long getDefBonus(int realmId) {
        RealmConfigEntity cfg = configService.getRealmConfig(realmId);
        return cfg != null ? cfg.getBaseDef() : realmId * 5L;
    }

    /**
     * 获取生命值加成
     */
    private long getHpBonus(int realmId) {
        RealmConfigEntity cfg = configService.getRealmConfig(realmId);
        return cfg != null ? cfg.getBaseHp() : realmId * 50L;
    }

    /**
     * 计算战斗力
     */
    private long calculateCombatPower(PlayerData playerData) {
        int realmId = playerData.getRealmId();
        long atk = getAtkBonus(realmId);
        long def = getDefBonus(realmId);
        long hp = getHpBonus(realmId);

        return (long) (atk * 2 + def * 1.5 + hp * 0.5);
    }
}
