package com.xiaoyao.logic.inventory;

import com.xiaoyao.common.proto.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 背包服务
 *
 * @author xiaoyao
 */
@Slf4j
public class InventoryService {
    
    /** 模拟玩家背包数据 (实际应从数据库获取) */
    private static final Map<Long, List<BagItem>> playerBags = new HashMap<>();
    
    /** 模拟玩家装备数据 */
    private static final Map<Long, List<EquippedItem>> playerEquips = new HashMap<>();
    
    /** 背包容量 */
    private static final int BAG_CAPACITY = 100;
    
    /**
     * 获取背包
     */
    public InventoryResp getInventory(long playerId) {
        InventoryResp resp = new InventoryResp();
        
        // 获取背包物品
        List<BagItem> items = playerBags.getOrDefault(playerId, new ArrayList<>());
        resp.setItems(items);
        
        // 获取装备
        List<EquippedItem> equipped = playerEquips.getOrDefault(playerId, new ArrayList<>());
        resp.setEquipped(equipped);
        
        resp.setCapacity(BAG_CAPACITY);
        resp.setUsedSlots(items.size());
        
        return resp;
    }
    
    /**
     * 使用物品
     */
    public UseItemResp useItem(long playerId, int itemId, int count) {
        UseItemResp resp = new UseItemResp();
        
        // 检查物品是否存在
        List<BagItem> items = playerBags.getOrDefault(playerId, new ArrayList<>());
        BagItem targetItem = null;
        for (BagItem item : items) {
            if (item.getItemId() == itemId) {
                targetItem = item;
                break;
            }
        }
        
        if (targetItem == null || targetItem.getCount() < count) {
            resp.setSuccess(false);
            resp.setMessage("物品不存在或数量不足");
            return resp;
        }
        
        // 扣除物品
        targetItem.setCount(targetItem.getCount() - count);
        if (targetItem.getCount() <= 0) {
            items.remove(targetItem);
        }
        
        // TODO: 根据物品类型执行效果
        resp.setSuccess(true);
        resp.setItemId(itemId);
        resp.setRemainCount(targetItem != null ? targetItem.getCount() : 0);
        resp.setExpGained(100 * count);  // 示例：每个物品给100经验
        resp.setMessage("使用成功");
        
        return resp;
    }
    
    /**
     * 穿戴装备
     */
    public EquipResp equip(long playerId, int itemId) {
        EquipResp resp = new EquipResp();
        
        // TODO: 实现装备逻辑
        // 1. 检查背包是否有该物品
        // 2. 检查物品是否为装备类型
        // 3. 检查槽位是否有旧装备
        // 4. 卸下旧装备到背包
        // 5. 穿戴新装备
        
        resp.setSuccess(true);
        resp.setUnequippedItemId(0);
        resp.setMessage("装备成功");
        
        return resp;
    }
    
    /**
     * 卸下装备
     */
    public EquipResp unequip(long playerId, int itemId) {
        EquipResp resp = new EquipResp();
        
        // TODO: 实现卸下逻辑
        
        resp.setSuccess(true);
        resp.setMessage("卸下成功");
        
        return resp;
    }
    
    /**
     * 添加物品到背包
     */
    public boolean addItem(long playerId, int itemId, int count) {
        List<BagItem> items = playerBags.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        // 检查容量
        if (items.size() >= BAG_CAPACITY) {
            return false;
        }
        
        // 查找是否已有该物品
        for (BagItem item : items) {
            if (item.getItemId() == itemId) {
                item.setCount(item.getCount() + count);
                return true;
            }
        }
        
        // 添加新物品
        BagItem newItem = new BagItem();
        newItem.setItemId(itemId);
        newItem.setCount(count);
        newItem.setSlot(items.size());
        items.add(newItem);
        
        return true;
    }
}
