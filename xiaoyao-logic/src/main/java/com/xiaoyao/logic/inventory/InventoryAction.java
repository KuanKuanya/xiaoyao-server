package com.xiaoyao.logic.inventory;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.InventoryCmd;
import com.xiaoyao.common.proto.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 背包模块 Action
 * <p>
 * 处理背包查看、物品使用、装备穿戴等请求
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@ActionController(InventoryCmd.cmd)
public class InventoryAction {
    
    private final InventoryService inventoryService = new InventoryService();
    
    /**
     * 获取背包列表
     *
     * @param flowContext 请求上下文
     * @return 背包数据
     */
    @ActionMethod(InventoryCmd.getList)
    public InventoryResp getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[背包] 获取列表 playerId={}", playerId);
        
        return inventoryService.getInventory(playerId);
    }
    
    /**
     * 使用物品
     *
     * @param req 使用请求
     * @param flowContext 请求上下文
     * @return 使用结果
     */
    @ActionMethod(InventoryCmd.useItem)
    public UseItemResp useItem(UseItemReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[背包] 使用物品 playerId={}, itemId={}, count={}", 
                playerId, req.getItemId(), req.getCount());
        
        return inventoryService.useItem(playerId, req.getItemId(), req.getCount());
    }
    
    /**
     * 穿戴装备
     *
     * @param req 装备请求
     * @param flowContext 请求上下文
     * @return 装备结果
     */
    @ActionMethod(InventoryCmd.equip)
    public EquipResp equip(EquipReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[背包] 穿戴装备 playerId={}, itemId={}", playerId, req.getItemId());
        
        return inventoryService.equip(playerId, req.getItemId());
    }
    
    /**
     * 卸下装备
     *
     * @param req 装备请求 (用slot表示槽位)
     * @param flowContext 请求上下文
     * @return 卸下结果
     */
    @ActionMethod(InventoryCmd.unequip)
    public EquipResp unequip(EquipReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[背包] 卸下装备 playerId={}, itemId={}", playerId, req.getItemId());
        
        return inventoryService.unequip(playerId, req.getItemId());
    }
}
