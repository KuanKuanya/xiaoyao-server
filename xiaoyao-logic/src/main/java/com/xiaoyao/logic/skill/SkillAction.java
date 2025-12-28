package com.xiaoyao.logic.skill;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.SkillCmd;
import com.xiaoyao.common.proto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 技能模块 Action
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ActionController(SkillCmd.cmd)
public class SkillAction {

    private final SkillService skillService;

    @ActionMethod(SkillCmd.getList)
    public SkillListResp getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[技能] 获取列表 playerId={}", playerId);
        return skillService.getSkillList(playerId);
    }

    @ActionMethod(SkillCmd.learn)
    public SkillResp learn(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 学习技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.learnSkill(playerId, req.getSkillId());
    }

    @ActionMethod(SkillCmd.upgrade)
    public SkillResp upgrade(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 升级技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.upgradeSkill(playerId, req.getSkillId());
    }

    @ActionMethod(SkillCmd.equip)
    public SkillResp equip(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 装备技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.equipSkill(playerId, req.getSkillId());
    }

    @ActionMethod(SkillCmd.unequip)
    public SkillResp unequip(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 卸下技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.unequipSkill(playerId, req.getSkillId());
    }
}
