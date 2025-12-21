package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 玩家基础数据
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
public class PlayerData {
    
    /** 玩家ID */
    private long playerId;
    
    /** 昵称 */
    private String nickname;
    
    /** 头像URL */
    private String avatar;
    
    /** 当前境界ID */
    private int realmId;
    
    /** 当前境界经验 */
    private long realmExp;
    
    /** 是否已飞升 */
    private boolean isAscended;
    
    /** 灵石 */
    private long spiritStone;
    
    /** 仙玉 */
    private long jade;
    
    /** 绑定仙玉 */
    private long bindJade;
    
    /** VIP等级 */
    private int vipLevel;
    
    /** 战斗力 */
    private long combatPower;
    
    /** 上次修炼时间戳 (毫秒) */
    private long lastCultivateTime;
}
