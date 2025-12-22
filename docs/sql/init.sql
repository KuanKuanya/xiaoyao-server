-- ================================================================
-- 逍遥游 数据库初始化脚本
-- ================================================================

CREATE DATABASE IF NOT EXISTS `xiaoyao_game` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `xiaoyao_game`;

-- ================================================================
-- 玩家相关表
-- ================================================================

-- 玩家基础表
CREATE TABLE IF NOT EXISTS `t_player` (
    `id` BIGINT NOT NULL COMMENT '玩家ID (雪花算法)',
    `device_id` VARCHAR(64) NOT NULL COMMENT '设备ID',
    `platform` TINYINT NOT NULL DEFAULT 0 COMMENT '平台类型 0=游客 1=微信 2=抖音',
    `nickname` VARCHAR(32) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像URL',
    `realm_id` INT NOT NULL DEFAULT 1 COMMENT '境界ID',
    `realm_exp` BIGINT NOT NULL DEFAULT 0 COMMENT '当前境界经验',
    `ascended` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已飞升',
    `spirit_stone` BIGINT NOT NULL DEFAULT 0 COMMENT '灵石',
    `jade` INT NOT NULL DEFAULT 0 COMMENT '仙玉',
    `bind_jade` INT NOT NULL DEFAULT 0 COMMENT '绑定仙玉',
    `vip_level` INT NOT NULL DEFAULT 0 COMMENT 'VIP等级',
    `combat_power` BIGINT NOT NULL DEFAULT 0 COMMENT '战斗力',
    `last_cultivate_time` BIGINT NOT NULL DEFAULT 0 COMMENT '上次领取修炼时间',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '上次登录时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device` (`device_id`),
    KEY `idx_realm` (`realm_id`),
    KEY `idx_combat_power` (`combat_power` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家基础表';

-- ================================================================
-- 背包装备相关表
-- ================================================================

-- 背包物品表
CREATE TABLE IF NOT EXISTS `t_bag_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `item_id` INT NOT NULL COMMENT '物品ID',
    `count` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `slot` INT NOT NULL DEFAULT 0 COMMENT '槽位',
    `locked` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否锁定',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_item` (`player_id`, `item_id`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='背包物品表';

-- 装备表
CREATE TABLE IF NOT EXISTS `t_equipment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `slot` VARCHAR(16) NOT NULL COMMENT '槽位 WEAPON/ARMOR/HEAD/FEET/BELT/ARTIFACT',
    `item_id` INT NOT NULL COMMENT '物品ID',
    `enhance_level` INT NOT NULL DEFAULT 0 COMMENT '强化等级',
    `star_level` INT NOT NULL DEFAULT 0 COMMENT '升星等级',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_slot` (`player_id`, `slot`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='装备表';

-- ================================================================
-- 技能相关表
-- ================================================================

-- 玩家技能表
CREATE TABLE IF NOT EXISTS `t_player_skill` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `skill_id` INT NOT NULL COMMENT '技能ID',
    `level` INT NOT NULL DEFAULT 1 COMMENT '技能等级',
    `exp` INT NOT NULL DEFAULT 0 COMMENT '当前经验',
    `equipped` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否装备',
    `equip_slot` TINYINT DEFAULT NULL COMMENT '装备槽位 (主动技能0-3, 心法99)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_skill` (`player_id`, `skill_id`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家技能表';

-- ================================================================
-- 任务邮件相关表
-- ================================================================

-- 任务进度表
CREATE TABLE IF NOT EXISTS `t_quest_progress` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `quest_id` INT NOT NULL COMMENT '任务ID',
    `progress` INT NOT NULL DEFAULT 0 COMMENT '当前进度',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 0=进行中 1=可领取 2=已完成',
    `claim_time` DATETIME DEFAULT NULL COMMENT '领取时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_quest` (`player_id`, `quest_id`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务进度表';

-- 邮件表
CREATE TABLE IF NOT EXISTS `t_mail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '邮件ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `title` VARCHAR(64) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `sender` VARCHAR(32) NOT NULL DEFAULT '系统' COMMENT '发送者',
    `attachments` JSON DEFAULT NULL COMMENT '附件 [{itemId, count}]',
    `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
    `is_claimed` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '附件是否已领取',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_expire` (`player_id`, `expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件表';

-- ================================================================
-- 战斗挂机相关表
-- ================================================================

-- 挂机记录表
CREATE TABLE IF NOT EXISTS `t_idle_battle` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `map_id` INT NOT NULL COMMENT '地图ID',
    `start_time` BIGINT NOT NULL COMMENT '开始时间戳',
    `is_active` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否进行中',
    `claim_time` BIGINT DEFAULT NULL COMMENT '领取时间戳',
    `total_exp` BIGINT NOT NULL DEFAULT 0 COMMENT '累计经验',
    `total_stones` BIGINT NOT NULL DEFAULT 0 COMMENT '累计灵石',
    `battle_count` INT NOT NULL DEFAULT 0 COMMENT '战斗次数',
    `victory_count` INT NOT NULL DEFAULT 0 COMMENT '胜利次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_active` (`player_id`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挂机记录表';

-- ================================================================
-- 商店相关表
-- ================================================================

-- 商店购买记录表
CREATE TABLE IF NOT EXISTS `t_shop_buy_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `shop_item_id` INT NOT NULL COMMENT '商品ID',
    `bought_count` INT NOT NULL DEFAULT 0 COMMENT '已购买数量',
    `reset_date` DATE NOT NULL COMMENT '重置日期',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_item_date` (`player_id`, `shop_item_id`, `reset_date`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商店购买记录表';

-- ================================================================
-- 排行榜相关表 (可选，也可用 Redis 实现)
-- ================================================================

-- 排行榜快照表
CREATE TABLE IF NOT EXISTS `t_rank_snapshot` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `rank_type` TINYINT NOT NULL COMMENT '排行类型 1=战力 2=境界 3=财富',
    `rank` INT NOT NULL COMMENT '排名',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `nickname` VARCHAR(32) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像',
    `realm_id` INT NOT NULL DEFAULT 1 COMMENT '境界ID',
    `value` BIGINT NOT NULL COMMENT '排行值',
    `snapshot_time` DATETIME NOT NULL COMMENT '快照时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_rank` (`rank_type`, `rank`, `snapshot_time`),
    KEY `idx_snapshot_time` (`snapshot_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排行榜快照表';
