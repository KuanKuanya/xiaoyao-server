package com.xiaoyao.logic.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.xiaoyao.logic.entity.MailEntity;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 邮件 Mapper
 *
 * @author xiaoyao
 */
@Mapper
public interface MailMapper extends BaseMapper<MailEntity> {

    /**
     * 根据玩家ID查询未过期邮件
     */
    default List<MailEntity> selectByPlayerId(Long playerId) {
        return selectListByQuery(QueryWrapper.create()
                .where("player_id = ? AND expire_time > ?", playerId, LocalDateTime.now())
                .orderBy("created_at DESC"));
    }
}
