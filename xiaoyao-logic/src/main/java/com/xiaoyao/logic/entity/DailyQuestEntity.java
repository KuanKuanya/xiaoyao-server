package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 每日任务实体
 * 对应数据库表: t_daily_quest
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_daily_quest")
public class DailyQuestEntity extends BaseEntity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 玩家ID
     */
    private Long playerId;

    /**
     * 任务日期
     */
    private LocalDate questDate;

    /**
     * 任务配置ID
     */
    private String questCfgId;

    /**
     * 当前进度
     */
    private Integer progress;

    /**
     * 目标进度
     */
    private Integer target;

    /**
     * 状态: 0-进行中 1-已完成 2-已领取
     */
    private Integer status;

    /**
     * 领取奖励时间戳(毫秒)
     */
    private Long claimTime;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
