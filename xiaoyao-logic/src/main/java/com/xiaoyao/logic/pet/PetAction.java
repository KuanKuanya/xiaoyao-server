package com.xiaoyao.logic.pet;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.CmdModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 战宠模块 Action
 * 处理宠物相关请求
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(CmdModule.PET)
public class PetAction {

    /**
     * 获取宠物列表
     */
    @ActionMethod(1)
    public List<PetEntity> getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[战宠] 获取列表 playerId={}", playerId);

        // TODO: 接入 PetService
        return new ArrayList<>();
    }

    /**
     * 宠物出战
     */
    @ActionMethod(2)
    public boolean deploy(Long petId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战宠] 宠物出战 playerId={}, petId={}", playerId, petId);

        // TODO: 设置出战宠物
        return true;
    }

    /**
     * 收回宠物
     */
    @ActionMethod(3)
    public boolean recall(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战宠] 收回宠物 playerId={}", playerId);

        // TODO: 取消出战
        return true;
    }

    /**
     * 宠物强化
     */
    @ActionMethod(4)
    public boolean enhance(Long petId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[战宠] 强化宠物 playerId={}, petId={}", playerId, petId);

        // TODO: 消耗材料强化
        return true;
    }
}
