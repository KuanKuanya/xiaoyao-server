package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * 突破结果响应
 *
 * @author xiaoyao
 */
@Data
@ProtobufClass
public class BreakthroughResp {

    /** 是否成功 */
    private boolean success;

    /** 新境界ID */
    private int newRealmId;

    /** 新境界名称 */
    private String newRealmName;

    /** 攻击力提升 */
    private long atkBonus;

    /** 防御力提升 */
    private long defBonus;

    /** 生命值提升 */
    private long hpBonus;

    /** 更新后的战斗力 */
    private long combatPower;
}
