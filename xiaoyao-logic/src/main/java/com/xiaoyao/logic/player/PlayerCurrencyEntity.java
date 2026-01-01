package com.xiaoyao.logic.player;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 玩家货币实体
 * 对应表: t_player_currency
 * 存储灵石、仙玉等货币
 *
 * @author xiaoyao
 */
@Data
@Table("t_player_currency")
public class PlayerCurrencyEntity {

    /** 玩家ID (与 t_player.id 一对一) */
    @Id(keyType = KeyType.None)
    private Long playerId;

    /** 灵石 */
    private Long spiritStone;

    /** 仙玉 */
    private Integer jade;

    /** 绑定仙玉 */
    private Integer bindJade;

    /** 乐观锁版本号 */
    @Column(version = true)
    private Integer version;

    /** 更新时间 */
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updatedAt;
}
