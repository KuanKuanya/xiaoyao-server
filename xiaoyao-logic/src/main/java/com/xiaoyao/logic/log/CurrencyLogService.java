package com.xiaoyao.logic.log;

import com.xiaoyao.logic.entity.CurrencyLogEntity;
import com.xiaoyao.logic.enums.CurrencyChangeType;
import com.xiaoyao.logic.enums.CurrencySourceType;
import com.xiaoyao.logic.enums.CurrencyType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 货币变动日志服务
 * 用于记录所有货币变动，支持对账和审计
 *
 * @author xiaoyao
 */
@Slf4j
@Service
public class CurrencyLogService {

    @Resource
    private com.xiaoyao.logic.repository.CurrencyLogRepository currencyLogRepository;

    /**
     * 记录货币增加日志
     *
     * @param playerId     玩家ID
     * @param currencyType 货币类型
     * @param amount       增加数量
     * @param beforeAmount 变动前数量
     * @param sourceType   来源类型
     * @param sourceId     来源ID
     */
    public void logAdd(Long playerId,
            CurrencyType currencyType,
            Long amount,
            Long beforeAmount,
            CurrencySourceType sourceType,
            String sourceId) {
        logChange(playerId, currencyType, CurrencyChangeType.ADD, amount, beforeAmount, sourceType, sourceId, null);
    }

    /**
     * 记录货币减少日志
     *
     * @param playerId     玩家ID
     * @param currencyType 货币类型
     * @param amount       减少数量
     * @param beforeAmount 变动前数量
     * @param sourceType   来源类型
     * @param sourceId     来源ID
     */
    public void logReduce(Long playerId,
            CurrencyType currencyType,
            Long amount,
            Long beforeAmount,
            CurrencySourceType sourceType,
            String sourceId) {
        logChange(playerId, currencyType, CurrencyChangeType.REDUCE, amount, beforeAmount, sourceType, sourceId, null);
    }

    /**
     * 记录货币变动日志（带备注）
     *
     * @param playerId     玩家ID
     * @param currencyType 货币类型
     * @param changeType   变动类型 (增加/减少)
     * @param amount       变动数量
     * @param beforeAmount 变动前数量
     * @param sourceType   来源类型
     * @param sourceId     来源ID
     * @param remark       备注说明
     */
    public void logChange(Long playerId,
            CurrencyType currencyType,
            CurrencyChangeType changeType,
            Long amount,
            Long beforeAmount,
            CurrencySourceType sourceType,
            String sourceId,
            String remark) {
        try {
            // 计算变动后的数量
            Long afterAmount = changeType == CurrencyChangeType.ADD
                    ? beforeAmount + amount
                    : beforeAmount - amount;

            // 创建日志实体
            CurrencyLogEntity currencyLogEntity = new CurrencyLogEntity();
            currencyLogEntity.setPlayerId(playerId);
            currencyLogEntity.setCurrencyType(currencyType.getCode());
            currencyLogEntity.setChangeType(changeType.getCode());
            currencyLogEntity.setChangeAmount(amount);
            currencyLogEntity.setBeforeAmount(beforeAmount);
            currencyLogEntity.setAfterAmount(afterAmount);
            currencyLogEntity.setSourceType(sourceType.getCode());
            currencyLogEntity.setSourceId(sourceId != null ? sourceId : "");
            currencyLogEntity.setRemark(remark != null ? remark : "");
            // 操作人默认为玩家自己
            currencyLogEntity.setCreatedBy(playerId);
            currencyLogEntity.setCreatedAt(LocalDateTime.now());

            // 插入数据库
            currencyLogRepository.save(currencyLogEntity);

            log.info("[货币日志] playerId={} 货币={} 操作={} 数量={} 前={} 后={} 来源={}:{}",
                    playerId,
                    currencyType.getDesc(),
                    changeType.getDesc(),
                    amount,
                    beforeAmount,
                    afterAmount,
                    sourceType.getDesc(),
                    sourceId);

        } catch (Exception e) {
            log.error("[货币日志] 记录失败 playerId={} currencyType={} amount={}", playerId, currencyType, amount, e);
            // 日志记录失败不应影响主流程，只记录错误日志
        }
    }

    /**
     * 记录GM操作的货币变动
     *
     * @param playerId     玩家ID
     * @param gmId         GM操作者ID
     * @param currencyType 货币类型
     * @param changeType   变动类型
     * @param amount       变动数量
     * @param beforeAmount 变动前数量
     * @param remark       操作原因
     */
    public void logGmChange(Long playerId,
            Long gmId,
            CurrencyType currencyType,
            CurrencyChangeType changeType,
            Long amount,
            Long beforeAmount,
            String remark) {
        try {
            Long afterAmount = changeType == CurrencyChangeType.ADD
                    ? beforeAmount + amount
                    : beforeAmount - amount;

            CurrencyLogEntity currencyLogEntity = new CurrencyLogEntity();
            currencyLogEntity.setPlayerId(playerId);
            currencyLogEntity.setCurrencyType(currencyType.getCode());
            currencyLogEntity.setChangeType(changeType.getCode());
            currencyLogEntity.setChangeAmount(amount);
            currencyLogEntity.setBeforeAmount(beforeAmount);
            currencyLogEntity.setAfterAmount(afterAmount);
            currencyLogEntity.setSourceType(CurrencySourceType.GM.getCode());
            currencyLogEntity.setSourceId("GM:" + gmId);
            currencyLogEntity.setRemark(remark != null ? remark : "");
            // 操作人为GM
            currencyLogEntity.setCreatedBy(gmId);
            currencyLogEntity.setCreatedAt(LocalDateTime.now());

            currencyLogRepository.save(currencyLogEntity);

            log.warn("[GM货币操作] gmId={} playerId={} 货币={} 操作={} 数量={} 前={} 后={} 原因={}",
                    gmId, playerId, currencyType.getDesc(), changeType.getDesc(),
                    amount, beforeAmount, afterAmount, remark);

        } catch (Exception e) {
            log.error("[GM货币操作] 记录失败 gmId={} playerId={}", gmId, playerId, e);
        }
    }
}
