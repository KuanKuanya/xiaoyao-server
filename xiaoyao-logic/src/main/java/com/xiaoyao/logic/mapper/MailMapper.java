package com.xiaoyao.logic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
        return selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MailEntity>()
                .eq(MailEntity::getPlayerId, playerId)
                .gt(MailEntity::getExpireTime, LocalDateTime.now())
                .orderByDesc(MailEntity::getCreatedAt));
    }
}
