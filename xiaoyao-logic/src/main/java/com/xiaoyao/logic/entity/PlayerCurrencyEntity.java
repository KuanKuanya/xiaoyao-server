package com.xiaoyao.logic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 玩家货币实体
 * 对应表: t_player_currency
 * 独立表用于减少高频更新时的锁竞争
 *
 * @author xiaoyao
 */
@Data
@Table("t_player_currency")
public class PlayerCurrencyEntity {

    /** 玩家ID */
    @Id
    private Long playerId;

    /** 灵石 */
    private Long spiritStone;

    /** 仙玉 */
    private Integer jade;

    /** 绑定仙玉 */
    private Integer bindJade;

    /** 乐观锁版本号 */
    @Version
    private Integer version;

    /** 更新时间 */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
