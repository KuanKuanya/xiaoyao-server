package com.xiaoyao.logic.config;

import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import com.xiaoyao.logic.config.dto.MapConfigVO;
import org.springframework.beans.BeanUtils;

/**
 * 游戏配置服务
 * <p>
 * 负责加载和缓存所有游戏配置数据
 * 提供配置查询接口
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class GameConfigService {

    @Resource
    private RealmConfigMapper realmConfigMapper;
    @Resource
    private MapConfigMapper mapConfigMapper;
    @Resource
    private MonsterConfigMapper monsterConfigMapper;
    @Resource
    private ItemConfigMapper itemConfigMapper;
    @Resource
    private SkillConfigMapper skillConfigMapper;
    @Resource
    private DropConfigMapper dropConfigMapper;
    @Resource
    private MapMonsterConfigMapper mapMonsterConfigMapper;

    // 配置缓存
    private final Map<Integer, RealmConfigEntity> realmCache = new ConcurrentHashMap<>();
    private final Map<Integer, MapConfigEntity> mapCache = new ConcurrentHashMap<>();
    private final Map<Integer, MonsterConfigEntity> monsterCache = new ConcurrentHashMap<>();
    private final Map<Integer, ItemConfigEntity> itemCache = new ConcurrentHashMap<>();
    private final Map<Integer, SkillConfigEntity> skillCache = new ConcurrentHashMap<>();
    private final Map<Integer, List<DropConfigEntity>> dropCache = new ConcurrentHashMap<>(); // sourceId -> drops
    private final Map<Integer, List<MapMonsterConfigEntity>> mapMonsterCache = new ConcurrentHashMap<>(); // mapId ->
                                                                                                          // monsters

    /**
     * 启动时加载所有配置
     */
    @PostConstruct
    public void init() {
        log.info("开始加载游戏配置...");
        long start = System.currentTimeMillis();

        loadRealmConfigs();
        loadMapConfigs();
        loadMonsterConfigs();
        loadItemConfigs();
        loadSkillConfigs();
        loadDropConfigs();
        loadMapMonsterConfigs();

        long cost = System.currentTimeMillis() - start;
        log.info("游戏配置加载完成! 耗时: {}ms", cost);
        log.info("配置统计: 境界={}, 地图={}, 怪物={}, 物品={}, 技能={}, 掉落={}, 关联={}",
                realmCache.size(), mapCache.size(), monsterCache.size(),
                itemCache.size(), skillCache.size(), dropCache.size(), mapMonsterCache.size());
    }

    /**
     * 重新加载配置 (热更新)
     */
    public void reload() {
        log.info("重新加载游戏配置...");
        realmCache.clear();
        mapCache.clear();
        monsterCache.clear();
        itemCache.clear();
        skillCache.clear();
        dropCache.clear();
        mapMonsterCache.clear();
        init();
    }

    // ==================== 配置加载 ====================

    private void loadRealmConfigs() {
        QueryWrapper query = QueryWrapper.create()
                .where("is_active = 1")
                .orderBy("id", true);
        List<RealmConfigEntity> list = realmConfigMapper.selectListByQuery(query);
        list.forEach(e -> realmCache.put(e.getId(), e));
        log.info("加载境界配置: {} 条", list.size());
    }

    private void loadMapConfigs() {
        QueryWrapper query = QueryWrapper.create()
                .where("is_active = 1")
                .orderBy("sort_order", true);
        List<MapConfigEntity> list = mapConfigMapper.selectListByQuery(query);
        list.forEach(e -> mapCache.put(e.getId(), e));
        log.info("加载地图配置: {} 条", list.size());
    }

    private void loadMonsterConfigs() {
        QueryWrapper query = QueryWrapper.create()
                .where("is_active = 1")
                .orderBy("id", true);
        List<MonsterConfigEntity> list = monsterConfigMapper.selectListByQuery(query);
        list.forEach(e -> monsterCache.put(e.getId(), e));
        log.info("加载怪物配置: {} 条", list.size());
    }

    private void loadItemConfigs() {
        QueryWrapper query = QueryWrapper.create()
                .where("is_active = 1")
                .orderBy("id", true);
        List<ItemConfigEntity> list = itemConfigMapper.selectListByQuery(query);
        list.forEach(e -> itemCache.put(e.getId(), e));
        log.info("加载物品配置: {} 条", list.size());
    }

    private void loadSkillConfigs() {
        QueryWrapper query = QueryWrapper.create()
                .where("is_active = 1")
                .orderBy("id", true);
        List<SkillConfigEntity> list = skillConfigMapper.selectListByQuery(query);
        list.forEach(e -> skillCache.put(e.getId(), e));
        log.info("加载技能配置: {} 条", list.size());
    }

    private void loadDropConfigs() {
        List<DropConfigEntity> list = dropConfigMapper.selectAll();
        // 按 sourceType_sourceId 分组
        for (DropConfigEntity drop : list) {
            int key = drop.getSourceType() * 100000 + drop.getSourceId();
            dropCache.computeIfAbsent(key, k -> new ArrayList<>()).add(drop);
        }
        log.info("加载掉落配置: {} 条", list.size());
    }

    private void loadMapMonsterConfigs() {
        List<MapMonsterConfigEntity> list = mapMonsterConfigMapper.selectAll();
        for (MapMonsterConfigEntity mm : list) {
            mapMonsterCache.computeIfAbsent(mm.getMapId(), k -> new ArrayList<>()).add(mm);
        }
        log.info("加载地图怪物: {} 条", list.size());
    }

    // ==================== 境界配置 ====================

    public RealmConfigEntity getRealmConfig(int realmId) {
        return realmCache.get(realmId);
    }

    public List<RealmConfigEntity> getAllRealmConfigs() {
        return new ArrayList<>(realmCache.values());
    }

    public String getRealmName(int realmId) {
        RealmConfigEntity cfg = realmCache.get(realmId);
        return cfg != null ? cfg.getName() : "未知境界";
    }

    public long getRequiredExp(int realmId) {
        RealmConfigEntity cfg = realmCache.get(realmId);
        return cfg != null ? cfg.getMaxExp() : 0;
    }

    public double getExpRate(int realmId) {
        RealmConfigEntity cfg = realmCache.get(realmId);
        return cfg != null ? cfg.getExpRate() : 1.0;
    }

    // ==================== 地图配置 ====================

    public MapConfigEntity getMapConfig(int mapId) {
        return mapCache.get(mapId);
    }

    public List<MapConfigEntity> getAllMapConfigs() {
        return mapCache.values().stream()
                .sorted(Comparator.comparingInt(MapConfigEntity::getSortOrder))
                .collect(Collectors.toList());
    }

    public List<MapConfigEntity> getMapsByRegion(String region) {
        return mapCache.values().stream()
                .filter(m -> region.equals(m.getRegion()))
                .sorted(Comparator.comparingInt(MapConfigEntity::getSortOrder))
                .collect(Collectors.toList());
    }

    public List<MapConfigEntity> getUnlockedMaps(int realmId) {
        return mapCache.values().stream()
                .filter(m -> m.getReqRealmId() <= realmId)
                .sorted(Comparator.comparingInt(MapConfigEntity::getSortOrder))
                .collect(Collectors.toList());
    }

    public List<MapConfigVO> getAllMapConfigVOs() {
        return mapCache.values().stream()
                .sorted(Comparator.comparingInt(MapConfigEntity::getSortOrder))
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private MapConfigVO convertToVO(MapConfigEntity entity) {
        MapConfigVO vo = new MapConfigVO();
        BeanUtils.copyProperties(entity, vo);

        List<MapMonsterConfigEntity> mapMonsters = mapMonsterCache.get(entity.getId());
        if (mapMonsters != null) {
            vo.setMonsterIds(mapMonsters.stream()
                    .map(MapMonsterConfigEntity::getMonsterId)
                    .collect(Collectors.toList()));
        } else {
            vo.setMonsterIds(Collections.emptyList());
        }

        // Drops with SourceType=1 (Map)
        int key = 1 * 100000 + entity.getId();
        List<DropConfigEntity> drops = dropCache.get(key);
        if (drops != null) {
            vo.setDrops(drops.stream()
                    .map(DropConfigEntity::getItemId)
                    .distinct()
                    .collect(Collectors.toList()));
        } else {
            vo.setDrops(Collections.emptyList());
        }
        return vo;
    }

    // ==================== 怪物配置 ====================

    public MonsterConfigEntity getMonsterConfig(int monsterId) {
        return monsterCache.get(monsterId);
    }

    public List<MonsterConfigEntity> getAllMonsterConfigs() {
        return new ArrayList<>(monsterCache.values());
    }

    public List<MonsterConfigEntity> getMonstersByType(int type) {
        return monsterCache.values().stream()
                .filter(m -> m.getMonsterType() == type)
                .collect(Collectors.toList());
    }

    public List<MapMonsterConfigEntity> getMapMonsters(int mapId) {
        return mapMonsterCache.getOrDefault(mapId, Collections.emptyList());
    }

    // ==================== 物品配置 ====================

    public ItemConfigEntity getItemConfig(int itemId) {
        return itemCache.get(itemId);
    }

    public List<ItemConfigEntity> getAllItemConfigs() {
        return new ArrayList<>(itemCache.values());
    }

    public List<ItemConfigEntity> getItemsByType(int type) {
        return itemCache.values().stream()
                .filter(i -> i.getItemType() == type)
                .collect(Collectors.toList());
    }

    public List<ItemConfigEntity> getItemsByQuality(int quality) {
        return itemCache.values().stream()
                .filter(i -> i.getQuality() == quality)
                .collect(Collectors.toList());
    }

    // ==================== 技能配置 ====================

    public SkillConfigEntity getSkillConfig(int skillId) {
        return skillCache.get(skillId);
    }

    public List<SkillConfigEntity> getAllSkillConfigs() {
        return new ArrayList<>(skillCache.values());
    }

    public List<SkillConfigEntity> getSkillsByType(int type) {
        return skillCache.values().stream()
                .filter(s -> s.getSkillType() == type)
                .collect(Collectors.toList());
    }

    // ==================== 掉落配置 ====================

    /**
     * 获取地图掉落配置
     * 
     * @param mapId 地图ID
     * @return 掉落配置列表
     */
    public List<DropConfigEntity> getMapDrops(int mapId) {
        int key = 1 * 100000 + mapId; // sourceType=1 地图
        return dropCache.getOrDefault(key, Collections.emptyList());
    }

    /**
     * 获取怪物掉落配置
     * 
     * @param monsterId 怪物ID
     * @return 掉落配置列表
     */
    public List<DropConfigEntity> getMonsterDrops(int monsterId) {
        int key = 2 * 100000 + monsterId; // sourceType=2 怪物
        return dropCache.getOrDefault(key, Collections.emptyList());
    }

    /**
     * 获取BOSS掉落配置
     * 
     * @param bossId BOSS ID
     * @return 掉落配置列表
     */
    public List<DropConfigEntity> getBossDrops(int bossId) {
        int key = 3 * 100000 + bossId; // sourceType=3 BOSS
        return dropCache.getOrDefault(key, Collections.emptyList());
    }

    /**
     * 计算掉落物品
     * 
     * @param drops 掉落配置列表
     * @return 掉落结果 Map<itemId, count>
     */
    public Map<Integer, Integer> calculateDrops(List<DropConfigEntity> drops) {
        Map<Integer, Integer> result = new HashMap<>();

        for (DropConfigEntity drop : drops) {
            // 判断是否掉落 (万分比)
            if (ThreadLocalRandom.current().nextInt(10000) < drop.getDropRate()) {
                // 计算掉落数量
                int count = drop.getMinCount();
                if (drop.getMaxCount() > drop.getMinCount()) {
                    count += ThreadLocalRandom.current().nextInt(drop.getMaxCount() - drop.getMinCount() + 1);
                }
                result.merge(drop.getItemId(), count, (a, b) -> a + b);
            }
        }
        return result;
    }
}
