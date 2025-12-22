package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 排行榜条目
 */
@Data
public class RankEntry {
    /** 排名 */
    private int rank;
    /** 玩家ID */
    private long playerId;
    /** 玩家昵称 */
    private String nickname;
    /** 头像 */
    private String avatar;
    /** 境界ID */
    private int realmId;
    /** 境界名称 */
    private String realmName;
    /** 战斗力 */
    private long combatPower;
    /** 排行值 */
    private long value;
}
