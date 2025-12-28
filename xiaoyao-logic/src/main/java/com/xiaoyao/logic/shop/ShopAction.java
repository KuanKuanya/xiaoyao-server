package com.xiaoyao.logic.shop;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.ShopCmd;
import com.xiaoyao.common.proto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 商店模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(ShopCmd.cmd)
public class ShopAction {

    private final ShopService shopService;

    @ActionMethod(ShopCmd.getList)
    public ShopListResp getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[商店] 获取列表 playerId={}", playerId);
        return shopService.getShopList(playerId);
    }

    @ActionMethod(ShopCmd.buy)
    public BuyResp buy(BuyReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[商店] 购买 playerId={}, itemId={}, count={}",
                playerId, req.getShopItemId(), req.getCount());
        return shopService.buy(playerId, req.getShopItemId(), req.getCount());
    }

    @ActionMethod(ShopCmd.refresh)
    public ShopListResp refresh(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[商店] 刷新 playerId={}", playerId);
        return shopService.refresh(playerId);
    }
}
