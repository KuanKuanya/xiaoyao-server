package com.xiaoyao.logic;

import com.iohao.net.framework.core.BarSkeletonBuilder;
import com.iohao.net.framework.core.CmdInfo;
import com.iohao.net.framework.core.doc.BroadcastDocument;
import com.iohao.net.framework.core.flow.internal.DebugInOut;
import com.iohao.net.framework.protocol.ServerBuilder;
import com.iohao.net.server.LogicServer;
import com.xiaoyao.common.proto.BattleResultResp;
import com.xiaoyao.common.proto.MailInfo;
import com.xiaoyao.logic.player.PlayerAction;
import com.xiaoyao.logic.rank.RankAction;
import com.xiaoyao.logic.realm.RealmAction;
import com.xiaoyao.logic.combat.CombatAction;
import com.xiaoyao.logic.inventory.InventoryAction;
import com.xiaoyao.logic.skill.SkillAction;
import com.xiaoyao.logic.config.ConfigAction;
import com.xiaoyao.logic.shop.ShopAction;
import com.xiaoyao.logic.quest.QuestAction;
import com.xiaoyao.logic.pet.PetAction;
import com.xiaoyao.logic.mail.MailAction;
import com.xiaoyao.logic.sect.SectAction;
import lombok.extern.slf4j.Slf4j;

/**
 * 逻辑服
 * <p>
 * 配置逻辑服的业务框架和 Action 扫描
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
public class HallLogicServer implements LogicServer {

    @Override
    public void settingBarSkeletonBuilder(BarSkeletonBuilder builder) {
        // 扫描 Action 类所在包
        builder.scanActionPackage(PlayerAction.class);
        builder.scanActionPackage(RealmAction.class);
        builder.scanActionPackage(RankAction.class);
        builder.scanActionPackage(CombatAction.class);
        builder.scanActionPackage(InventoryAction.class);
        builder.scanActionPackage(SkillAction.class);
        builder.scanActionPackage(ConfigAction.class);
        builder.scanActionPackage(ShopAction.class);
        builder.scanActionPackage(QuestAction.class);
        builder.scanActionPackage(PetAction.class);
        builder.scanActionPackage(MailAction.class);
        builder.scanActionPackage(SectAction.class);

        // 配置广播文档 (用于生成前端 SDK 的广播监听代码)
        configureBroadcastDocuments(builder);

        // 添加调试插件 (生产环境可关闭)
        builder.addInOut(new DebugInOut());

        log.info("[逻辑服] 业务框架配置完成");
    }

    @Override
    public void settingServerBuilder(ServerBuilder builder) {
        // 设置逻辑服名称
        builder.setName("HallLogicServer");
    }

    /**
     * 配置广播文档
     * <p>
     * 用于 SDK 生成器生成前端的广播监听代码
     */
    private void configureBroadcastDocuments(BarSkeletonBuilder builder) {
        // 战斗结果广播 (例如：挂机战斗中的结果推送)
        builder.addBroadcastDocument(
                BroadcastDocument.builder(CmdInfo.of(CombatCmd.cmd, CombatCmd.BROADCAST_BATTLE_RESULT))
                        .setDataClass(BattleResultResp.class)
                        .setMethodDescription("战斗结果广播")
                        .setMethodName("onBattleResult"));

        // 邮件通知广播
        builder.addBroadcastDocument(BroadcastDocument.builder(CmdInfo.of(MailCmd.cmd, MailCmd.BROADCAST_NEW_MAIL))
                .setDataClass(MailInfo.class)
                .setMethodDescription("新邮件通知")
                .setMethodName("onNewMail"));

        log.info("[逻辑服] 广播文档配置完成");
    }

    // ========== 广播路由定义 ==========

    /** 战斗模块路由 */
    public interface CombatCmd {
        int cmd = 12;
        int BROADCAST_BATTLE_RESULT = 50; // 战斗结果广播
    }

    /** 邮件模块路由 */
    public interface MailCmd {
        int cmd = 16;
        int BROADCAST_NEW_MAIL = 50; // 新邮件通知
    }
}
