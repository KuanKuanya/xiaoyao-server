package com.xiaoyao.logic.mail;

import com.iohao.net.framework.annotations.ActionController;
import com.iohao.net.framework.annotations.ActionMethod;
import com.iohao.net.framework.core.flow.FlowContext;
import com.xiaoyao.common.cmd.MailCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮件模块 Action
 * 处理邮件相关请求
 *
 * @author xiaoyao
 */
@Slf4j
@Component
@ActionController(MailCmd.cmd)
public class MailAction {

    /**
     * 获取邮件列表
     */
    @ActionMethod(MailCmd.getList)
    public List<MailEntity> getList(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[邮件] 获取列表 playerId={}", playerId);

        // TODO: 接入 MailService 查询玩家邮件
        return new ArrayList<>();
    }

    /**
     * 读取邮件
     */
    @ActionMethod(MailCmd.read)
    public boolean read(Long mailId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.debug("[邮件] 读取邮件 playerId={}, mailId={}", playerId, mailId);

        // TODO: 标记邮件为已读
        return true;
    }

    /**
     * 领取邮件附件
     */
    @ActionMethod(MailCmd.claim)
    public boolean claim(Long mailId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[邮件] 领取附件 playerId={}, mailId={}", playerId, mailId);

        // TODO: 发放附件物品
        return true;
    }

    /**
     * 删除邮件
     */
    @ActionMethod(MailCmd.delete)
    public boolean delete(Long mailId, FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[邮件] 删除邮件 playerId={}, mailId={}", playerId, mailId);

        // TODO: 删除邮件
        return true;
    }

    /**
     * 一键领取所有附件
     */
    @ActionMethod(MailCmd.claimAll)
    public int claimAll(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[邮件] 一键领取 playerId={}", playerId);

        // TODO: 遍历所有邮件领取附件
        return 0;
    }

    /**
     * 一键删除已读邮件
     */
    @ActionMethod(MailCmd.deleteRead)
    public int deleteRead(FlowContext flowContext) {
        long playerId = flowContext.getUserId();
        log.info("[邮件] 删除已读 playerId={}", playerId);

        // TODO: 删除所有已读邮件
        return 0;
    }
}
