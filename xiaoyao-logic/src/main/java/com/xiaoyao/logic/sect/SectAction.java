package com.xiaoyao.logic.sect;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.CmdModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 宗门模块 Action
 * 处理宗门相关请求
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(CmdModule.SECT)
public class SectAction {

    @Resource
    private SectService sectService;

    /**
     * 获取宗门信息
     */
    @ActionMethod(1)
    public SectMemberEntity getInfo(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[宗门] 获取信息 playerId={}", playerId);
        return sectService.getPlayerSect(playerId);
    }

    /**
     * 获取宗门成员列表
     */
    @ActionMethod(2)
    public List<SectMemberEntity> getMemberList(String sectId, FlowContext flowContext) {
        log.debug("[宗门] 获取成员列表 sectId={}", sectId);
        return sectService.getSectMembers(sectId);
    }

    /**
     * 加入宗门
     */
    @ActionMethod(3)
    public boolean join(String sectId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[宗门] 加入宗门 playerId={}, sectId={}", playerId, sectId);

        try {
            sectService.joinSect(playerId, sectId);
            return true;
        } catch (Exception e) {
            log.error("[宗门] 加入失败", e);
            return false;
        }
    }

    /**
     * 领取俸禄
     */
    @ActionMethod(4)
    public boolean claimSalary(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[宗门] 领取俸禄 playerId={}", playerId);

        try {
            sectService.claimSalary(playerId);
            return true;
        } catch (Exception e) {
            log.error("[宗门] 领取失败", e);
            return false;
        }
    }

    /**
     * 退出宗门
     */
    @ActionMethod(5)
    public boolean quit(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[宗门] 退出宗门 playerId={}", playerId);

        try {
            sectService.quitSect(playerId);
            return true;
        } catch (Exception e) {
            log.error("[宗门] 退出失败", e);
            return false;
        }
    }
}
