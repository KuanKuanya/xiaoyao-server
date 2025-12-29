package com.xiaoyao.logic.skill;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.SkillCmd;
import com.xiaoyao.common.proto.SkillListResp;
import com.xiaoyao.common.proto.SkillReq;
import com.xiaoyao.common.proto.SkillResp;
import lombok.extern.slf4j.Slf4j;

/**
 * 技能模块 Action
 * <p>
 * 处理技能学习、升级、装备等请求
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@ActionController(SkillCmd.cmd)
public class SkillAction {

    private final SkillService skillService = new SkillService();

    /**
     * 获取技能列表
     */
    @ActionMethod(SkillCmd.getList)
    public SkillListResp getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[技能] 获取列表 playerId={}", playerId);
        return skillService.getSkillList(playerId);
    }

    /**
     * 学习技能
     */
    @ActionMethod(SkillCmd.learn)
    public SkillResp learn(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 学习技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.learnSkill(playerId, req.getSkillId());
    }

    /**
     * 升级技能
     */
    @ActionMethod(SkillCmd.upgrade)
    public SkillResp upgrade(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 升级技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.upgradeSkill(playerId, req.getSkillId());
    }

    /**
     * 装备技能
     */
    @ActionMethod(SkillCmd.equip)
    public SkillResp equip(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 装备技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.equipSkill(playerId, req.getSkillId());
    }

    /**
     * 卸下技能
     */
    @ActionMethod(SkillCmd.unequip)
    public SkillResp unequip(SkillReq req, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[技能] 卸下技能 playerId={}, skillId={}", playerId, req.getSkillId());
        return skillService.unequipSkill(playerId, req.getSkillId());
    }
}
