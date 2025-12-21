package com.xiaoyao.logic.realm;

import com.iohao.net.framework.core.exception.MessageException;
import com.xiaoyao.common.error.RealmError;
import com.xiaoyao.common.proto.BreakthroughResp;
import com.xiaoyao.common.proto.CultivateResp;
import com.xiaoyao.common.proto.PlayerData;
import lombok.extern.slf4j.Slf4j;

/**
 * 境界服务
 * <p>
 * 处理修炼、突破、飞升等业务逻辑
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class RealmService {
    
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
        
        // 根据境界计算经验倍率
        int realmId = playerData.getRealmId();
        double expRate = 1.0 + (realmId - 1) * 0.1; // 每个境界增加10%经验
        
        // 计算获得的经验
        long expGain = (long) (duration * BASE_EXP_PER_SECOND * expRate);
        
        // 更新玩家数据
        long newExp = playerData.getRealmExp() + expGain;
        playerData.setRealmExp(newExp);
        playerData.setLastCultivateTime(now);
        
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
        int atkBonus = getAtkBonus(newRealmId);
        int defBonus = getDefBonus(newRealmId);
        int hpBonus = getHpBonus(newRealmId);
        
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
     * 获取境界名称
     */
    public String getRealmName(int realmId) {
        // 大境界
        String[] majorRealms = {
            "练气", "筑基", "金丹", "元婴", "化神",
            "炼虚", "合体", "大乘", "渡劫",
            "地仙", "天仙", "金仙", "太乙金仙", "大罗金仙"
        };
        
        // 小境界
        String[] minorRealms = {"一层", "二层", "三层", "四层", "五层", "六层", "七层", "八层", "九层", "圆满"};
        
        int majorIndex = (realmId - 1) / 10;
        int minorIndex = (realmId - 1) % 10;
        
        if (majorIndex >= majorRealms.length) {
            return "大罗金仙·圆满";
        }
        
        return majorRealms[majorIndex] + "期·" + minorRealms[minorIndex];
    }
    
    /**
     * 获取突破所需经验
     */
    private long getRequiredExp(int realmId) {
        // 指数增长公式
        return (long) (100 * Math.pow(1.5, realmId - 1));
    }
    
    /**
     * 获取攻击力加成
     */
    private int getAtkBonus(int realmId) {
        return realmId * 10;
    }
    
    /**
     * 获取防御力加成
     */
    private int getDefBonus(int realmId) {
        return realmId * 5;
    }
    
    /**
     * 获取生命值加成
     */
    private int getHpBonus(int realmId) {
        return realmId * 50;
    }
    
    /**
     * 计算战斗力
     */
    private long calculateCombatPower(PlayerData playerData) {
        int realmId = playerData.getRealmId();
        int atk = getAtkBonus(realmId);
        int def = getDefBonus(realmId);
        int hp = getHpBonus(realmId);
        
        return (long) (atk * 2 + def * 1.5 + hp * 0.5);
    }
}
