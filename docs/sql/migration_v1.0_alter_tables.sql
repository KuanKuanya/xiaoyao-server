-- ================================================================
-- 逍遥游 数据库迁移脚本 v1.0 - 修改现有表
-- 说明: 为init.sql中已存在的表补充规范的公共字段
-- 作者: xiaoyao
-- 日期: 2025-12-22
-- ================================================================

USE `xiaoyao_game`;

-- ================================================================
-- 1. t_player (玩家基础表)
-- ================================================================
ALTER TABLE `t_player`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 2. t_bag_item (背包物品表)
-- ================================================================
ALTER TABLE `t_bag_item`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 3. t_equipment (装备表)
-- ================================================================
ALTER TABLE `t_equipment`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 4. t_player_skill (玩家技能表)
-- ================================================================
ALTER TABLE `t_player_skill`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 5. t_quest_progress (任务进度表)
-- ================================================================
ALTER TABLE `t_quest_progress`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 6. t_mail (邮件表)
-- ================================================================
-- 邮件表补充缺失字段
ALTER TABLE `t_mail`
    ADD COLUMN `mail_type` TINYINT NOT NULL DEFAULT 1 COMMENT '邮件类型: 1-系统 2-战斗 3-活动 4-GM' AFTER `player_id`,
    ADD COLUMN `read_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '阅读时间戳(毫秒)' AFTER `is_read`,
    ADD COLUMN `claim_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '领取时间戳(毫秒)' AFTER `is_claimed`,
    CHANGE COLUMN `expire_time` `expire_time` BIGINT UNSIGNED NOT NULL COMMENT '过期时间戳(毫秒)';

-- 邮件表补充公共字段
ALTER TABLE `t_mail`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `create_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    ADD COLUMN `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `updated_by`,
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 7. t_idle_battle (挂机记录表)
-- ================================================================
ALTER TABLE `t_idle_battle`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 8. t_shop_buy_record (商店购买记录表)
-- ================================================================
ALTER TABLE `t_shop_buy_record`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `update_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    CHANGE COLUMN `create_time` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    CHANGE COLUMN `update_time` `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 9. t_rank_snapshot (排行榜快照表)
-- ================================================================
-- 注意: 排行榜快照表通常不需要逻辑删除,因为是历史数据快照
-- 这里仅补充部分公共字段作为示例
ALTER TABLE `t_rank_snapshot`
    ADD COLUMN `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号' AFTER `snapshot_time`,
    ADD COLUMN `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除' AFTER `version`,
    ADD COLUMN `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID' AFTER `is_deleted`,
    ADD COLUMN `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `created_by`,
    ADD COLUMN `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID' AFTER `created_at`,
    ADD COLUMN `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `updated_by`,
    ADD COLUMN `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID' AFTER `updated_at`,
    ADD COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间' AFTER `deleted_by`,
    ADD INDEX `idx_deleted` (`is_deleted`);

-- ================================================================
-- 执行完成提示
-- ================================================================
SELECT 'v1.0 现有表迁移完成! 已为9张表补充公共字段' AS 'Migration Status';
