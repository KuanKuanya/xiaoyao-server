package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 敌人信息
 */
@Data
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
    private int hp;
    /** 最大血量 */
    private int maxHp;
    /** 攻击力 */
    private int atk;
    /** 防御力 */
    private int def;
    /** 速度 */
    private int speed;
}
