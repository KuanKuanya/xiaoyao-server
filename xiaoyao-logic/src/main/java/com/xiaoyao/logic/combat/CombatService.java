package com.xiaoyao.logic.combat;

import com.xiaoyao.common.proto.BagItem;
import com.xiaoyao.common.proto.BattleResultResp;
import com.xiaoyao.common.proto.EnemyInfo;
import com.xiaoyao.common.proto.IdleRewardResp;
import com.xiaoyao.logic.config.DropConfigEntity;
import com.xiaoyao.logic.config.GameConfigService;
import com.xiaoyao.logic.config.MapMonsterConfigEntity;
import com.xiaoyao.logic.config.MonsterConfigEntity;
import com.xiaoyao.logic.inventory.InventoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 战斗服务
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class CombatService {

    @Resource
    private InventoryService inventoryService;
    @Resource
    private GameConfigService configService;

    /** 玩家挂机状态 */
    private final Map<Long, IdleBattleState> playerIdleStates = new ConcurrentHashMap<>();

    /**
     * 开始战斗
     */
    public BattleResultResp startBattle(long playerId, int mapId) {
        BattleResultResp resp = new BattleResultResp();

        // 1. 生成敌人
        EnemyInfo enemy = generateEnemy(mapId);
        if (enemy == null) {
            log.warn("地图没有配置怪物: mapId={}", mapId);
            // 降级逻辑: 如果没有配置怪物，生成一个临时的
            enemy = generateFallbackEnemy(mapId);
        }
        resp.setEnemy(enemy);

        // 2. 战斗计算 (简化版: 80%胜率)
        boolean victory = ThreadLocalRandom.current().nextFloat() > 0.2f;
        resp.setVictory(victory);
        resp.setRounds(ThreadLocalRandom.current().nextInt(10) + 1);
        resp.setPlayerHpPercent(victory ? ThreadLocalRandom.current().nextFloat() * 0.5f + 0.5f : 0);

        if (victory) {
            // 3. 计算奖励
            long expGained = enemy.getExpReward();
            long stonesGained = enemy.getStoneReward();

            // 浮动 90% - 110%
            float floatRate = 0.9f + ThreadLocalRandom.current().nextFloat() * 0.2f;
            expGained = (long) (expGained * floatRate);
            stonesGained = (long) (stonesGained * floatRate);

            resp.setExpGained(expGained);
            resp.setStonesGained(stonesGained);

            // 4. 计算掉落
            List<BagItem> drops = calculateDrops(mapId, enemy.getEnemyId());
            resp.setDrops(drops);

            // 发放掉落物品
            for (BagItem item : drops) {
                inventoryService.addItem(playerId, item.getItemId(), item.getCount());
            }

            // TODO: 发放经验和灵石 (需调用 PlayerService)
        }

        return resp;
    }

    /**
     * 开始挂机
     */
    public boolean startIdleBattle(long playerId, int mapId) {
        IdleBattleState state = new IdleBattleState();

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
        int victoryCount = (int) (battleCount * 0.8f);

        resp.setBattleCount(battleCount);
        resp.setVictoryCount(victoryCount);

        // 获取地图参考怪物
        EnemyInfo refEnemy = generateEnemy(state.mapId);
        if (refEnemy == null)
            refEnemy = generateFallbackEnemy(state.mapId);

        // 计算总奖励
        long expPerBattle = refEnemy.getExpReward();
        long stonesPerBattle = refEnemy.getStoneReward();

        resp.setTotalExp(victoryCount * expPerBattle);
        resp.setTotalStones(victoryCount * stonesPerBattle);

        // 计算总掉落
        // 挂机掉率降低: 为单次战斗的 20%
        List<BagItem> allDrops = new ArrayList<>();
        // 为了性能，不模拟每一场，而是按批次计算
        // 比如 dropCount = victoryCount / 5;
        // 这里简单处理: 模拟 1/5 场次的掉落判定
        int dropRolls = victoryCount / 5;
        if (dropRolls > 0) {
            // 合并掉落
            Map<Integer, Integer> dropMap = new HashMap<>();

            // 地图掉落配置
            List<DropConfigEntity> mapDropConfigs = configService.getMapDrops(state.mapId);

            for (int i = 0; i < dropRolls; i++) {
                Map<Integer, Integer> roundDrops = configService.calculateDrops(mapDropConfigs);
                roundDrops.forEach((itemId, count) -> dropMap.merge(itemId, count, (a, b) -> a + b));
            }

            dropMap.forEach((itemId, count) -> {
                BagItem item = new BagItem();
                item.setItemId(itemId);
                item.setCount(count);
                allDrops.add(item);
                inventoryService.addItem(playerId, itemId, count);
            });
        }

        resp.setDrops(allDrops);

        // 清除状态
        playerIdleStates.remove(playerId);

        // TODO: 发放经验和灵石

        return resp;
    }

    /**
     * 根据配置生成怪物
     */
    private EnemyInfo generateEnemy(int mapId) {
        List<MapMonsterConfigEntity> mapMonsters = configService.getMapMonsters(mapId);
        if (mapMonsters == null || mapMonsters.isEmpty()) {
            return null;
        }

        // 简单的随机权重算法
        int totalWeight = mapMonsters.stream().mapToInt(MapMonsterConfigEntity::getSpawnRate).sum();
        int roll = ThreadLocalRandom.current().nextInt(totalWeight);
        int current = 0;

        MonsterConfigEntity targetInput = null;
        for (MapMonsterConfigEntity mm : mapMonsters) {
            current += mm.getSpawnRate();
            if (roll < current) {
                targetInput = configService.getMonsterConfig(mm.getMonsterId());
                break;
            }
        }

        if (targetInput == null) {
            // Should not happen if weights are correct
            targetInput = configService.getMonsterConfig(mapMonsters.get(0).getMonsterId());
        }

        return convertToEnemyInfo(targetInput);
    }

    private EnemyInfo convertToEnemyInfo(MonsterConfigEntity config) {
        if (config == null)
            return null;

        EnemyInfo info = new EnemyInfo();
        info.setEnemyId(config.getId());
        info.setName(config.getName());
        info.setLevel(config.getRealmLevel());
        info.setMaxHp(config.getBaseHp());
        info.setHp(config.getBaseHp());
        info.setAtk(config.getBaseAtk());
        info.setDef(config.getBaseDef());
        info.setSpeed(100); // 基础速度
        info.setIcon(config.getIcon() != null ? config.getIcon() : "👹");

        // 奖励预览
        info.setExpReward(config.getExpReward());
        info.setStoneReward(config.getStoneReward());

        return info;
    }

    /**
     * 降级怪物生成 (当没有配置时)
     */
    private EnemyInfo generateFallbackEnemy(int mapId) {
        EnemyInfo enemy = new EnemyInfo();
        enemy.setEnemyId(-1);
        enemy.setName("未知怪物");
        enemy.setIcon("👻");
        enemy.setLevel(mapId);

        long baseHp = 100 + mapId * 50L;
        long baseAtk = 10 + mapId * 5L;
        long baseDef = 5 + mapId * 3L;

        enemy.setMaxHp(baseHp);
        enemy.setHp(baseHp);
        enemy.setAtk(baseAtk);
        enemy.setDef(baseDef);
        enemy.setExpReward(50 + mapId * 10L);
        enemy.setStoneReward(10 + mapId * 2L);

        return enemy;
    }

    /**
     * 计算掉落
     */
    private List<BagItem> calculateDrops(int mapId, long monsterId) {
        List<BagItem> drops = new ArrayList<>();
        Map<Integer, Integer> dropMap = new HashMap<>();

        // 1. 地图掉落
        List<DropConfigEntity> mapDrops = configService.getMapDrops(mapId);
        Map<Integer, Integer> mapDropResult = configService.calculateDrops(mapDrops);
        mapDropResult.forEach((k, v) -> dropMap.merge(k, v, Integer::sum));

        // 2. 怪物掉落 (如果有)
        // monsterId 此时是 configId
        if (monsterId > 0) {
            List<DropConfigEntity> monsterDrops = configService.getMonsterDrops((int) monsterId);
            Map<Integer, Integer> monsterDropResult = configService.calculateDrops(monsterDrops);
            monsterDropResult.forEach((k, v) -> dropMap.merge(k, v, Integer::sum));
        }

        dropMap.forEach((itemId, count) -> {
            BagItem item = new BagItem();
            item.setItemId(itemId);
            item.setCount(count);
            drops.add(item);
        });

        return drops;
    }

    /**
     * 挂机状态
     */
    private static class IdleBattleState {
        int mapId;
        long startTime;
        boolean isActive;
    }
}
