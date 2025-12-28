package com.xiaoyao.logic.inventory;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.InventoryCmd;
import com.xiaoyao.common.proto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 背包模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(InventoryCmd.cmd)
public class InventoryAction {

    private final InventoryService inventoryService;

    @ActionMethod(InventoryCmd.getList)
    public InventoryResp getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[背包] 获取列表 playerId={}", playerId);
        return inventoryService.getInventory(playerId);
    }

    @ActionMethod(InventoryCmd.useItem)
    public UseItemResp useItem(UseItemReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[背包] 使用物品 playerId={}, itemId={}, count={}",
                playerId, req.getItemId(), req.getCount());
        return inventoryService.useItem(playerId, req.getItemId(), req.getCount());
    }

    @ActionMethod(InventoryCmd.equip)
    public EquipResp equip(EquipReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[背包] 穿戴装备 playerId={}, itemId={}", playerId, req.getItemId());
        return inventoryService.equip(playerId, req.getItemId());
    }

    @ActionMethod(InventoryCmd.unequip)
    public EquipResp unequip(EquipReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[背包] 卸下装备 playerId={}, itemId={}", playerId, req.getItemId());
        return inventoryService.unequip(playerId, req.getItemId());
    }
}
