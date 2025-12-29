package com.xiaoyao.logic.shop;

import com.xiaoyao.common.proto.BuyResp;
import com.xiaoyao.common.proto.ShopItem;
import com.xiaoyao.common.proto.ShopListResp;
import com.xiaoyao.logic.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商店服务
 * <p>
 * TODO: 接入数据库后替换内存存储
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class ShopService {

    /** 商店商品配置 */
    private static final List<ShopItem> SHOP_CONFIG = new ArrayList<>();

    /** 玩家购买记录 */
    private static final Map<Long, Map<Integer, Integer>> PLAYER_BUY_RECORDS = new ConcurrentHashMap<>();

    private final InventoryService inventoryService = new InventoryService();

    static {
        // 初始化商店配置
        initShopConfig();
    }

    private static void initShopConfig() {
        // 普通商品
        addShopItem(1, 101, 1, 100, 1, 0);        // 小还丹 100灵石
        addShopItem(2, 102, 1, 500, 1, 0);        // 中还丹 500灵石
        addShopItem(3, 103, 1, 2000, 1, 0);       // 大还丹 2000灵石

        // 限购商品
        addShopItem(10, 201, 1, 50, 2, 5);        // 初级武器 50仙玉 限购5
        addShopItem(11, 202, 1, 100, 2, 3);       // 中级武器 100仙玉 限购3

        // 功法书
        addShopItem(20, 1001, 1, 1000, 1, 1);     // 基础剑法 限购1
        addShopItem(21, 1002, 1, 2000, 1, 1);     // 火球术 限购1
    }

    private static void addShopItem(int id, int itemId, int count, int price, int currencyType, int limit) {
        ShopItem item = new ShopItem();
        item.setId(id);
        item.setItemId(itemId);
        item.setCount(count);
        item.setPrice(price);
        item.setCurrencyType(currencyType);
        item.setLimitCount(limit);
        item.setDiscount(100);
        SHOP_CONFIG.add(item);
    }

    /**
     * 获取商店列表
     */
    public ShopListResp getShopList(long playerId) {
        ShopListResp resp = new ShopListResp();

        Map<Integer, Integer> buyRecords = PLAYER_BUY_RECORDS.getOrDefault(playerId, new HashMap<>());

        List<ShopItem> items = new ArrayList<>();
        for (ShopItem config : SHOP_CONFIG) {
            ShopItem item = new ShopItem();
            item.setId(config.getId());
            item.setItemId(config.getItemId());
            item.setCount(config.getCount());
            item.setPrice(config.getPrice());
            item.setCurrencyType(config.getCurrencyType());
            item.setLimitCount(config.getLimitCount());
            item.setBoughtCount(buyRecords.getOrDefault(config.getId(), 0));
            item.setDiscount(config.getDiscount());
            items.add(item);
        }

        resp.setItems(items);
        resp.setNextRefreshTime(getNextDayStartTime());
        resp.setRefreshRemain(3);

        return resp;
    }

    /**
     * 购买商品
     */
    public BuyResp buy(long playerId, int shopItemId, int count) {
        BuyResp resp = new BuyResp();

        // 查找商品
        ShopItem target = null;
        for (ShopItem item : SHOP_CONFIG) {
            if (item.getId() == shopItemId) {
                target = item;
                break;
            }
        }

        if (target == null) {
            resp.setSuccess(false);
            resp.setMessage("商品不存在");
            return resp;
        }

        // 检查限购
        Map<Integer, Integer> buyRecords = PLAYER_BUY_RECORDS.computeIfAbsent(playerId, k -> new HashMap<>());
        int bought = buyRecords.getOrDefault(shopItemId, 0);
        if (target.getLimitCount() > 0 && bought + count > target.getLimitCount()) {
            resp.setSuccess(false);
            resp.setMessage("超出限购数量");
            return resp;
        }

        // TODO: 检查并扣除货币
        int cost = target.getPrice() * count;

        // 发放物品
        inventoryService.addItem(playerId, target.getItemId(), target.getCount() * count);

        // 记录购买
        buyRecords.put(shopItemId, bought + count);

        resp.setSuccess(true);
        resp.setItemId(target.getItemId());
        resp.setCount(target.getCount() * count);
        resp.setCost(cost);
        resp.setMessage("购买成功");

        return resp;
    }

    /**
     * 刷新商店
     */
    public ShopListResp refresh(long playerId) {
        // TODO: 检查并扣除刷新费用
        // 清除购买记录
        PLAYER_BUY_RECORDS.remove(playerId);
        return getShopList(playerId);
    }

    private long getNextDayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
