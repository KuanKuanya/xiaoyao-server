package com.xiaoyao.logic.rank;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.RankCmd;
import com.xiaoyao.common.proto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 排行榜模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(RankCmd.cmd)
public class RankAction {

    private final RankService rankService;

    @ActionMethod(RankCmd.getCombatPowerRank)
    public RankListResp getCombatPowerRank(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[排行榜] 获取战力排行 playerId={}", playerId);
        return rankService.getRankList(playerId, 1);
    }

    @ActionMethod(RankCmd.getRealmRank)
    public RankListResp getRealmRank(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[排行榜] 获取境界排行 playerId={}", playerId);
        return rankService.getRankList(playerId, 2);
    }

    @ActionMethod(RankCmd.getWealthRank)
    public RankListResp getWealthRank(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[排行榜] 获取财富排行 playerId={}", playerId);
        return rankService.getRankList(playerId, 3);
    }

    @ActionMethod(RankCmd.getMyRank)
    public RankEntry getMyRank(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[排行榜] 获取我的排名 playerId={}", playerId);
        return rankService.getMyRank(playerId);
    }
}
