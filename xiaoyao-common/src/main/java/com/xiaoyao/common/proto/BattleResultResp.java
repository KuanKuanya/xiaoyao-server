package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 战斗结果响应
 */
@Data
@ProtobufClass
public class BattleResultResp {
    /** 是否胜利 */
    private boolean victory;
    /** 敌人信息 */
    private EnemyInfo enemy;
    /** 回合数 */
    private int rounds;
    /** 获得经验 */
    private long expGained;
    /** 获得灵石 */
    private long stonesGained;
    /** 掉落物品 */
    private List<BagItem> drops;
    /** 玩家剩余血量比例 */
    private float playerHpPercent;
}
