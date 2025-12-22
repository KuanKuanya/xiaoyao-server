package com.xiaoyao.logic.combat;

import com.xiaoyao.common.proto.*;
import com.xiaoyao.logic.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 战斗服务
 *
 * @author xiaoyao
 */
@Slf4j
public class CombatService {
    
    /** 敌人名称前缀 */
    private static final String[] ENEMY_PREFIX = {"小", "大", "妖", "凶", "狂", "魔"};
    
    /** 敌人类型 */
    private static final String[] ENEMY_TYPES = {"兔", "狼", "虎", "熊", "蛇", "龙", "凤"};
    
    /** 敌人图标 */
    private static final String[] ENEMY_ICONS = {"🐰", "🐺", "🐯", "🐻", "🐍", "🐲", "🦅"};
    
    /** 玩家挂机状态 */
    private static final Map<Long, IdleBattleState> playerIdleStates = new HashMap<>();
    
    private final InventoryService inventoryService = new InventoryService();
    private final Random random = new Random();
    
    /**
     * 开始战斗
     */
    public BattleResultResp startBattle(long playerId, int mapId) {
        BattleResultResp resp = new BattleResultResp();
        
        // 生成敌人
        EnemyInfo enemy = generateEnemy(mapId);
        resp.setEnemy(enemy);
        
        // TODO: 获取玩家属性，执行战斗逻辑
        // 这里简化为随机胜负
        boolean victory = random.nextFloat() > 0.2f;  // 80%胜率
        resp.setVictory(victory);
        resp.setRounds(random.nextInt(10) + 1);
        resp.setPlayerHpPercent(victory ? random.nextFloat() * 0.5f + 0.5f : 0);
        
        if (victory) {
            // 计算奖励
            int baseExp = 50 + mapId * 10;
            int baseStones = 10 + mapId * 2;
            
            resp.setExpGained(baseExp + random.nextInt(baseExp / 2));
            resp.setStonesGained(baseStones + random.nextInt(baseStones / 2));
            
            // 随机掉落
            List<BagItem> drops = new ArrayList<>();
            if (random.nextFloat() < 0.3f) {  // 30%掉落
                BagItem drop = new BagItem();
                drop.setItemId(101 + random.nextInt(5));  // 随机物品
                drop.setCount(1);
                drops.add(drop);
                
                // 添加到背包
                inventoryService.addItem(playerId, drop.getItemId(), drop.getCount());
            }
            resp.setDrops(drops);
            
            // TODO: 更新玩家经验和灵石
        }
        
        return resp;
    }
    
    /**
     * 开始挂机
     */
    public boolean startIdleBattle(long playerId, int mapId) {
        IdleBattleState state = new IdleBattleState();
        state.playerId = playerId;
        state.mapId = mapId;
        state.startTime = System.currentTimeMillis();
        state.isActive = true;
        
        playerIdleStates.put(playerId, state);
        return true;
    }
    
    /**
     * 停止挂机
     */
    public boolean stopIdleBattle(long playerId) {
        IdleBattleState state = playerIdleStates.get(playerId);
        if (state != null) {
            state.isActive = false;
        }
        return true;
    }
    
    /**
     * 领取挂机奖励
     */
    public IdleRewardResp claimIdleReward(long playerId) {
        IdleRewardResp resp = new IdleRewardResp();
        
        IdleBattleState state = playerIdleStates.get(playerId);
        if (state == null) {
            resp.setIdleSeconds(0);
            return resp;
        }
        
        // 计算挂机时长
        long endTime = state.isActive ? System.currentTimeMillis() : state.startTime;
        int seconds = (int) ((endTime - state.startTime) / 1000);
        resp.setIdleSeconds(seconds);
        
        // 每10秒一场战斗
        int battleCount = seconds / 10;
        int victoryCount = (int) (battleCount * 0.8f);  // 80%胜率
        
        resp.setBattleCount(battleCount);
        resp.setVictoryCount(victoryCount);
        
        // 计算奖励
        int expPerBattle = 50 + state.mapId * 10;
        int stonesPerBattle = 10 + state.mapId * 2;
        
        resp.setTotalExp((long) victoryCount * expPerBattle);
        resp.setTotalStones(victoryCount * stonesPerBattle);
        
        // 随机掉落
        List<BagItem> drops = new ArrayList<>();
        int dropCount = victoryCount / 10;  // 每10场掉落1个
        for (int i = 0; i < dropCount; i++) {
            BagItem drop = new BagItem();
            drop.setItemId(101 + random.nextInt(5));
            drop.setCount(1);
            drops.add(drop);
            inventoryService.addItem(playerId, drop.getItemId(), 1);
        }
        resp.setDrops(drops);
        
        // 清除状态
        playerIdleStates.remove(playerId);
        
        // TODO: 更新玩家数据
        
        return resp;
    }
    
    /**
     * 生成敌人
     */
    private EnemyInfo generateEnemy(int mapId) {
        EnemyInfo enemy = new EnemyInfo();
        
        int typeIndex = random.nextInt(ENEMY_TYPES.length);
        String prefix = ENEMY_PREFIX[random.nextInt(ENEMY_PREFIX.length)];
        
        enemy.setEnemyId(System.currentTimeMillis());
        enemy.setName(prefix + ENEMY_TYPES[typeIndex]);
        enemy.setIcon(ENEMY_ICONS[typeIndex]);
        enemy.setLevel(mapId);
        
        // 基于地图等级计算属性
        int baseHp = 100 + mapId * 50;
        int baseAtk = 10 + mapId * 5;
        int baseDef = 5 + mapId * 3;
        
        enemy.setMaxHp(baseHp);
        enemy.setHp(baseHp);
        enemy.setAtk(baseAtk);
        enemy.setDef(baseDef);
        enemy.setSpeed(100 + random.nextInt(20));
        
        return enemy;
    }
    
    /**
     * 挂机状态
     */
    private static class IdleBattleState {
        long playerId;
        int mapId;
        long startTime;
        boolean isActive;
    }
}
