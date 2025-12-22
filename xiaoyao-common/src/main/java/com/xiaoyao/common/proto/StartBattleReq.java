package com.xiaoyao.common.proto;

import lombok.Data;

/**
 * 开始战斗请求
 */
@Data
public class StartBattleReq {
    /** 地图ID */
    private int mapId;
}
