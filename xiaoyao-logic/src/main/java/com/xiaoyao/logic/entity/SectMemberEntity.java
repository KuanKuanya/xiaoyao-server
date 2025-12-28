package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宗门成员实体
 * 对应数据库表: t_sect_member
 *
 * @author xiaoyao
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("t_sect_member")
public class SectMemberEntity extends BaseEntity {

    /**
     * 自增主键
     */
    @Id
    private Long id;

    /**
     * 玩家ID
     */
    private Long playerId;

    /**
     * 宗门配置ID
     */
    private String sectId;

    /**
     * 职位: 0-弟子 1-内门 2-长老 3-副掌门 4-掌门
     */
    private Integer position;

    /**
     * 贡献度
     */
    private Long contribution;

    /**
     * 本周贡献度
     */
    private Long weeklyContribution;

    /**
     * 上次领取俸禄时间戳(毫秒)
     */
    private Long lastSalaryTime;

    /**
     * 累计领取俸禄次数
     */
    private Integer totalSalaryCount;

    // 公共字段 (version, is_deleted, created_at, updated_at等) 已由 BaseEntity 提供
}
