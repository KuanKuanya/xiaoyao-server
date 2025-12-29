package com.xiaoyao.logic.rank;

import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.common.proto.RankEntry;
import com.xiaoyao.common.proto.RankListResp;
import com.xiaoyao.logic.player.PlayerEntity;
import com.xiaoyao.logic.player.PlayerMapper;
import com.xiaoyao.logic.player.PlayerStatsEntity;
import com.xiaoyao.logic.player.PlayerStatsMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜服务
 * 提供战力榜、境界榜、财富榜等排名功能
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class RankService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private PlayerStatsMapper playerStatsMapper;

    /**
     * 排行榜类型常量
     */
    private static final int RANK_TYPE_COMBAT_POWER = 1;  // 战力榜
    private static final int RANK_TYPE_REALM = 2;         // 境界榜
    private static final int RANK_TYPE_WEALTH = 3;        // 财富榜

    /**
     * 排行榜最大显示条数
     */
    private static final int MAX_RANK_SIZE = 100;

    /**
     * 获取排行榜列表
     *
     * @param playerId 玩家ID
     * @param rankType 排行榜类型 1=战力 2=境界 3=财富
     * @return 排行榜响应
     */
    public RankListResp getRankList(long playerId, int rankType) {
        log.debug("[排行榜] 获取排行榜 playerId={} rankType={}", playerId, rankType);

        RankListResp resp = new RankListResp();
        resp.setRankType(rankType);
        resp.setUpdateTime(System.currentTimeMillis());

        // 获取排行榜列表
        List<RankEntry> rankList = buildRankList(rankType);
        resp.setList(rankList);

        // 获取我的排名
        RankEntry myRank = findMyRankInList(rankList, playerId);
        if (myRank == null) {
            // 如果不在榜单中,单独查询
            myRank = buildMyRank(playerId, rankType);
        }
        resp.setMyRank(myRank);

        return resp;
    }

    /**
     * 获取我的排名
     *
     * @param playerId 玩家ID
     * @return 我的排名信息
     */
    public RankEntry getMyRank(long playerId) {
        log.debug("[排行榜] 获取我的排名 playerId={}", playerId);

        // 默认返回战力排名
        return buildMyRank(playerId, RANK_TYPE_COMBAT_POWER);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 构建排行榜列表
     */
    private List<RankEntry> buildRankList(int rankType) {
        List<RankEntry> rankList = new ArrayList<>();

        // 根据排行榜类型查询
        QueryWrapper query = QueryWrapper.create()
                .from("t_player p")
                .leftJoin("t_player_attribute pa").on("p.id = pa.player_id")
                .where("p.is_deleted = 0")
                .limit(MAX_RANK_SIZE);

        // 根据类型排序
        switch (rankType) {
            case RANK_TYPE_COMBAT_POWER:
                query.orderBy("pa.combat_power DESC, p.id ASC");
                break;
            case RANK_TYPE_REALM:
                // TODO: 境界排序需要根据实际的境界字段调整
                query.orderBy("pa.combat_power DESC, p.id ASC");
                break;
            case RANK_TYPE_WEALTH:
                // TODO: 财富排序需要根据实际的货币字段调整
                query.orderBy("pa.combat_power DESC, p.id ASC");
                break;
            default:
                log.warn("[排行榜] 未知的排行榜类型: {}", rankType);
                query.orderBy("pa.combat_power DESC, p.id ASC");
        }

        // TODO: 这里简化处理,实际应该使用联表查询或缓存优化
        // 暂时使用分步查询
        List<PlayerStatsEntity> statsList = playerStatsMapper.selectListByQuery(
                QueryWrapper.create()
                        .orderBy("combat_power DESC")
                        .limit(MAX_RANK_SIZE)
        );

        int rank = 1;
        for (PlayerStatsEntity stats : statsList) {
            PlayerEntity player = playerMapper.selectOneById(stats.getPlayerId());
            if (player != null && player.getIsDeleted() == 0) {
                RankEntry entry = new RankEntry();
                entry.setRank(rank++);
                entry.setPlayerId(player.getId());
                entry.setNickname(player.getNickname() != null ? player.getNickname() : "玩家" + player.getId());
                entry.setAvatar(player.getAvatar() != null ? player.getAvatar() : "");
                entry.setRealmId(0);  // TODO: 从实际字段获取
                entry.setRealmName(""); // TODO: 从配置获取境界名称
                entry.setCombatPower(stats.getCombatPower() != null ? stats.getCombatPower() : 0L);
                entry.setValue(stats.getCombatPower() != null ? stats.getCombatPower() : 0L);
                rankList.add(entry);
            }
        }

        log.debug("[排行榜] 构建排行榜完成 rankType={} size={}", rankType, rankList.size());
        return rankList;
    }

    /**
     * 在排行榜列表中查找我的排名
     */
    private RankEntry findMyRankInList(List<RankEntry> rankList, long playerId) {
        for (RankEntry entry : rankList) {
            if (entry.getPlayerId() == playerId) {
                return entry;
            }
        }
        return null;
    }

    /**
     * 构建我的排名信息
     */
    private RankEntry buildMyRank(long playerId, int rankType) {
        PlayerEntity player = playerMapper.selectOneById(playerId);
        if (player == null) {
            log.warn("[排行榜] 玩家不存在 playerId={}", playerId);
            return createEmptyRank();
        }

        PlayerStatsEntity stats = playerStatsMapper.selectOneById(playerId);
        if (stats == null) {
            log.warn("[排行榜] 玩家属性不存在 playerId={}", playerId);
            return createEmptyRank();
        }

        RankEntry entry = new RankEntry();
        entry.setRank(calculateMyRank(playerId, rankType));
        entry.setPlayerId(player.getId());
        entry.setNickname(player.getNickname() != null ? player.getNickname() : "玩家" + player.getId());
        entry.setAvatar(player.getAvatar() != null ? player.getAvatar() : "");
        entry.setRealmId(0);  // TODO: 从实际字段获取
        entry.setRealmName(""); // TODO: 从配置获取境界名称
        entry.setCombatPower(stats.getCombatPower() != null ? stats.getCombatPower() : 0L);
        entry.setValue(stats.getCombatPower() != null ? stats.getCombatPower() : 0L);

        return entry;
    }

    /**
     * 计算我的排名
     */
    private int calculateMyRank(long playerId, int rankType) {
        PlayerStatsEntity myStats = playerStatsMapper.selectOneById(playerId);
        if (myStats == null || myStats.getCombatPower() == null) {
            return 999999; // 未上榜
        }

        // 查询有多少人战力比我高
        long count = playerStatsMapper.selectCountByQuery(
                QueryWrapper.create()
                        .where("combat_power > ?", myStats.getCombatPower())
                        .or("combat_power = ? AND player_id < ?", myStats.getCombatPower(), playerId)
        );

        return (int) (count + 1);
    }

    /**
     * 创建空排名
     */
    private RankEntry createEmptyRank() {
        RankEntry entry = new RankEntry();
        entry.setRank(999999);
        entry.setPlayerId(0L);
        entry.setNickname("");
        entry.setAvatar("");
        entry.setRealmId(0);
        entry.setRealmName("");
        entry.setCombatPower(0L);
        entry.setValue(0L);
        return entry;
    }
}
