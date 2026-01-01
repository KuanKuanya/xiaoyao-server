package com.xiaoyao.common.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.net.extension.protobuf.ProtoFileMerge;
import lombok.Data;

/**
 * 敌人信息
 */
@Data
@ProtobufClass
@ProtoFileMerge(fileName = XiaoyaoProtoFile.FILE_NAME, filePackage = XiaoyaoProtoFile.FILE_PACKAGE)
public class EnemyInfo {
    /** 敌人ID */
    private long enemyId;
    /** 名称 */
    private String name;
    /** 等级 */
    private int level;
    /** 图标 */
    private String icon;
    /** 当前血量 */
    private long hp;
    /** 最大血量 */
    private long maxHp;
    /** 攻击力 */
    private long atk;
    /** 防御力 */
    private long def;
    /** 速度 */
    private int speed;

    /** 经验奖励 */
    private long expReward;
    /** 灵石奖励 */
    private long stoneReward;
}
