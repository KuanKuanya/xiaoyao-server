package com.xiaoyao.logic;

import com.iohao.net.framework.core.BarSkeletonBuilder;
import com.iohao.net.framework.core.flow.internal.DebugInOut;
import com.iohao.net.framework.protocol.ServerBuilder;
import com.iohao.net.server.LogicServer;
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

        // 添加调试插件 (生产环境可关闭)
        builder.addInOut(new DebugInOut());

        log.info("[逻辑服] 业务框架配置完成");
    }

    @Override
    public void settingServerBuilder(ServerBuilder builder) {
        // 设置逻辑服名称
        builder.setName("HallLogicServer");
    }
}
