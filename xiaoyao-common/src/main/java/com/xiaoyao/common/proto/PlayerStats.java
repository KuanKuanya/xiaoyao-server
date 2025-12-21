package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 玩家属性数据
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
public class PlayerStats {
    
    /** 基础攻击 */
    private int baseAtk;
    
    /** 基础防御 */
    private int baseDef;
    
    /** 基础生命 */
    private int baseHp;
    
    /** 基础法力 */
    private int baseMp;
    
    /** 暴击率 (0.05 = 5%) */
    private double critRate;
    
    /** 暴击伤害倍率 */
    private double critDamage;
    
    /** 闪避率 */
    private double dodgeRate;
    
    /** 命中率 */
    private double hitRate;
    
    /** 吸血率 */
    private double lifestealRate;
    
    /** 战斗力 */
    private long combatPower;
}
