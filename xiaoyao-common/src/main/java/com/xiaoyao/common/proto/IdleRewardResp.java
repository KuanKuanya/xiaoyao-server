package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * 挂机奖励响应
 */
@Data
@ProtobufClass
public class IdleRewardResp {
    /** 挂机时长 (秒) */
    private int idleSeconds;
    /** 战斗次数 */
    private int battleCount;
    /** 胜利次数 */
    private int victoryCount;
    /** 获得总经验 */
    private long totalExp;
    /** 获得总灵石 */
    private long totalStones;
    /** 掉落物品 */
    private List<BagItem> drops;
}
