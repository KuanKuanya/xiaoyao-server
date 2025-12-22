package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyao.logic.entity.CurrencyLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 货币变动日志 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface CurrencyLogMapper extends BaseMapper<CurrencyLogEntity> {

    /**
     * 查询玩家指定货币类型的变动记录
     *
     * @param playerId 玩家ID
     * @param currencyType 货币类型 (null=查询所有类型)
     * @param limit 限制条数
     * @return 日志列表 (按时间倒序)
     */
    @Select("<script>" +
            "SELECT * FROM t_currency_log " +
            "WHERE player_id = #{playerId} " +
            "<if test='currencyType != null'> AND currency_type = #{currencyType} </if>" +
            "ORDER BY created_at DESC " +
            "LIMIT #{limit}" +
            "</script>")
    List<CurrencyLogEntity> selectRecentLogs(@Param("playerId") Long playerId,
                                              @Param("currencyType") Integer currencyType,
                                              @Param("limit") Integer limit);

    /**
     * 统计玩家某个货币类型的总收入
     *
     * @param playerId 玩家ID
     * @param currencyType 货币类型
     * @return 总收入
     */
    @Select("SELECT COALESCE(SUM(change_amount), 0) FROM t_currency_log " +
            "WHERE player_id = #{playerId} AND currency_type = #{currencyType} AND change_type = 1")
    Long sumIncome(@Param("playerId") Long playerId, @Param("currencyType") Integer currencyType);

    /**
     * 统计玩家某个货币类型的总支出
     *
     * @param playerId 玩家ID
     * @param currencyType 货币类型
     * @return 总支出
     */
    @Select("SELECT COALESCE(SUM(change_amount), 0) FROM t_currency_log " +
            "WHERE player_id = #{playerId} AND currency_type = #{currencyType} AND change_type = 2")
    Long sumExpense(@Param("playerId") Long playerId, @Param("currencyType") Integer currencyType);
}
