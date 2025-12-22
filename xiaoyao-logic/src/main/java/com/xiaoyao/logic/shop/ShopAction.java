package com.xiaoyao.logic.shop;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.ShopCmd;
import com.xiaoyao.common.proto.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 商店模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@ActionController(ShopCmd.cmd)
public class ShopAction {
    
    private final ShopService shopService = new ShopService();
    
    /**
     * 获取商店列表
     */
    @ActionMethod(ShopCmd.getList)
    public ShopListResp getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[商店] 获取列表 playerId={}", playerId);
        return shopService.getShopList(playerId);
    }
    
    /**
     * 购买商品
     */
    @ActionMethod(ShopCmd.buy)
    public BuyResp buy(BuyReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[商店] 购买 playerId={}, itemId={}, count={}", 
                playerId, req.getShopItemId(), req.getCount());
        return shopService.buy(playerId, req.getShopItemId(), req.getCount());
    }
    
    /**
     * 刷新商店
     */
    @ActionMethod(ShopCmd.refresh)
    public ShopListResp refresh(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[商店] 刷新 playerId={}", playerId);
        return shopService.refresh(playerId);
    }
}
