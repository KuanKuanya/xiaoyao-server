package com.xiaoyao.logic.rank;

import com.xiaoyao.common.proto.PlayerData;
import com.xiaoyao.common.proto.RankEntry;
import com.xiaoyao.common.proto.RankListResp;
import com.xiaoyao.logic.player.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜服务
 * <p>
 * TODO: 后续改为使用 Redis 的 Sorted Set 实现
 * </p>
 *
 * @author xiaoyao
 */
@Slf4j
@RequiredArgsConstructor
public class RankService {

    private final PlayerService playerService;

    /** 排行榜类型：战力 */
    private static final int RANK_TYPE_COMBAT_POWER = 1;
    /** 排行榜类型：境界 */
    private static final int RANK_TYPE_REALM = 2;
    /** 排行榜类型：财富 */
    private static final int RANK_TYPE_WEALTH = 3;

    /** 排行榜最大显示数量 */
    private static final int MAX_RANK_SIZE = 100;

    /**
     * 获取排行榜
     *
     * @param playerId 玩家ID
     * @param rankType 排行榜类型 (1:战力 2:境界 3:财富)
     * @return 排行榜响应
     */
    public RankListResp getRankList(long playerId, int rankType) {
        RankListResp resp = new RankListResp();
        resp.setRankType(rankType);
        resp.setUpdateTime(System.currentTimeMillis());

        // TODO: 从数据库或Redis获取所有玩家数据
        // 目前使用内存数据临时实现
        List<RankEntry> rankList = buildRankList(rankType);
        resp.setList(rankList);

        // 查找我的排名
        RankEntry myRank = findMyRank(playerId, rankList);
        if (myRank == null) {
            // 如果不在榜上，构造我的排名信息
            PlayerData playerData = playerService.getPlayerData(playerId);
            if (playerData != null) {
                myRank = buildRankEntry(playerData, 0, rankType);
            }
        }
        resp.setMyRank(myRank);

        return resp;
    }

    /**
     * 获取我的排名
     *
     * @param playerId 玩家ID
     * @return 排名条目
     */
    public RankEntry getMyRank(long playerId) {
        PlayerData playerData = playerService.getPlayerData(playerId);
        if (playerData == null) {
            return null;
        }

        // TODO: 从Redis获取实时排名
        // 目前返回默认排名
        return buildRankEntry(playerData, 0, RANK_TYPE_COMBAT_POWER);
    }

    /**
     * 构建排行榜列表
     *
     * @param rankType 排行榜类型
     * @return 排行榜列表
     */
    private List<RankEntry> buildRankList(int rankType) {
        // TODO: 从数据库或Redis获取玩家数据
        // 目前返回空列表作为临时实现
        List<RankEntry> list = new ArrayList<>();

        log.debug("[排行榜] 构建排行榜 type={} size={}", rankType, list.size());

        return list;
    }

    /**
     * 查找我的排名
     *
     * @param playerId 玩家ID
     * @param rankList 排行榜列表
     * @return 我的排名条目
     */
    private RankEntry findMyRank(long playerId, List<RankEntry> rankList) {
        for (RankEntry entry : rankList) {
            if (entry.getPlayerId() == playerId) {
                return entry;
            }
        }
        return null;
    }

    /**
     * 构建排名条目
     *
     * @param playerData 玩家数据
     * @param rank       排名
     * @param rankType   排行榜类型
     * @return 排名条目
     */
    private RankEntry buildRankEntry(PlayerData playerData, int rank, int rankType) {
        RankEntry entry = new RankEntry();
        entry.setRank(rank);
        entry.setPlayerId(playerData.getPlayerId());
        entry.setNickname(playerData.getNickname());
        entry.setAvatar(playerData.getAvatar());
        entry.setRealmId(playerData.getRealmId());
        entry.setRealmName(getRealmName(playerData.getRealmId()));
        entry.setCombatPower(playerData.getCombatPower());

        // 根据不同类型设置排行值
        switch (rankType) {
            case RANK_TYPE_COMBAT_POWER:
                entry.setValue(playerData.getCombatPower());
                break;
            case RANK_TYPE_REALM:
                entry.setValue(playerData.getRealmId() * 1000000L + playerData.getRealmExp());
                break;
            case RANK_TYPE_WEALTH:
                entry.setValue(playerData.getSpiritStone());
                break;
            default:
                entry.setValue(0);
        }

        return entry;
    }

    /**
     * 获取境界名称
     * TODO: 从配置表获取
     *
     * @param realmId 境界ID
     * @return 境界名称
     */
    private String getRealmName(int realmId) {
        // 临时实现
        if (realmId <= 9) {
            return "练气期" + realmId + "层";
        } else if (realmId <= 18) {
            return "筑基期" + (realmId - 9) + "层";
        } else if (realmId <= 27) {
            return "金丹期" + (realmId - 18) + "层";
        } else if (realmId <= 36) {
            return "元婴期" + (realmId - 27) + "层";
        } else {
            return "未知境界";
        }
    }

    /**
     * 更新排行榜
     * 当玩家数据变化时调用
     *
     * @param playerId 玩家ID
     */
    public void updateRank(long playerId) {
        // TODO: 更新Redis中的排行榜数据
        log.debug("[排行榜] 更新排名 playerId={}", playerId);
    }
}
