-- ================================================================
-- 逍遥游 数据库初始化脚本 (v2.0 重构版)
-- 版本: v2.0.0
-- 日期: 2025-12-28
-- 说明: 拆分职责、消除冗余、补充缺失字段
-- 表总数: 27张
-- ================================================================

CREATE DATABASE IF NOT EXISTS `xiaoyao_game` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `xiaoyao_game`;

-- ================================================================
-- 一、玩家核心模块 (3张表)
-- ================================================================

-- 1. 玩家账户表 (身份认证，低频更新)
DROP TABLE IF EXISTS `t_player`;
CREATE TABLE `t_player` (
    `id` BIGINT NOT NULL COMMENT '玩家ID (雪花算法)',
    `device_id` VARCHAR(64) NOT NULL COMMENT '设备ID',
    `platform` TINYINT NOT NULL DEFAULT 0 COMMENT '平台: 0=游客 1=微信 2=抖音 3=苹果 4=安卓',
    `nickname` VARCHAR(32) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像URL',
    `vip_level` INT NOT NULL DEFAULT 0 COMMENT 'VIP等级',
    `current_title_id` INT DEFAULT NULL COMMENT '当前佩戴称号ID',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '上次登录时间',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device` (`device_id`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家账户表';

-- 2. 玩家货币表 (高频更新，独立乐观锁)
DROP TABLE IF EXISTS `t_player_currency`;
CREATE TABLE `t_player_currency` (
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `spirit_stone` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '灵石',
    `jade` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '仙玉',
    `bind_jade` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '绑定仙玉',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家货币表';

-- 3. 玩家进度表 (境界/地图/洞府，低频更新)
DROP TABLE IF EXISTS `t_player_progress`;
CREATE TABLE `t_player_progress` (
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    -- 境界进度
    `realm_id` INT NOT NULL DEFAULT 1 COMMENT '境界ID',
    `realm_exp` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前境界经验',
    `total_exp` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计总修为',
    `ascended` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已飞升',
    -- 地图进度
    `tower_floor` INT NOT NULL DEFAULT 0 COMMENT '镇妖塔当前层数',
    `unlocked_map_id` INT NOT NULL DEFAULT 1 COMMENT '已解锁最高地图ID',
    -- 洞府进度
    `spirit_array_level` INT NOT NULL DEFAULT 1 COMMENT '聚灵阵等级',
    `last_cultivate_time` BIGINT NOT NULL DEFAULT 0 COMMENT '上次领取修炼时间',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`player_id`),
    KEY `idx_realm` (`realm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家进度表';

-- ================================================================
-- 二、属性与统计模块 (2张表)
-- ================================================================

-- 4. 玩家属性表 (战斗属性，高频更新)
DROP TABLE IF EXISTS `t_player_attribute`;
CREATE TABLE `t_player_attribute` (
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    -- 战力 (穿脱装备/升级技能时频繁更新)
    `combat_power` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '战斗力',
    -- 基础属性
    `base_atk` INT UNSIGNED NOT NULL DEFAULT 10 COMMENT '基础攻击',
    `base_def` INT UNSIGNED NOT NULL DEFAULT 5 COMMENT '基础防御',
    `base_hp` INT UNSIGNED NOT NULL DEFAULT 100 COMMENT '基础生命',
    `base_mp` INT UNSIGNED NOT NULL DEFAULT 50 COMMENT '基础法力',
    -- 战斗属性 (万分比: 500=5%, 10000=100%, 15000=150%)
    `crit_rate` INT UNSIGNED NOT NULL DEFAULT 500 COMMENT '暴击率(万分比)',
    `crit_damage` INT UNSIGNED NOT NULL DEFAULT 15000 COMMENT '暴击伤害(万分比)',
    `dodge_rate` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '闪避率(万分比)',
    `hit_rate` INT UNSIGNED NOT NULL DEFAULT 10000 COMMENT '命中率(万分比)',
    `block_rate` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '格挡率(万分比)',
    `lifesteal_rate` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '吸血率(万分比)',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`player_id`),
    KEY `idx_combat_power` (`combat_power` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家属性表';

-- 5. 玩家统计表 (成就用)
DROP TABLE IF EXISTS `t_player_statistics`;
CREATE TABLE `t_player_statistics` (
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    -- 累计统计
    `total_kills` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计击杀',
    `total_deaths` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计死亡',
    `total_crafts` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计炼丹',
    `total_online_time` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计在线时长(秒)',
    `max_damage` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '最高伤害',
    `max_enhance_level` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '最高强化等级',
    -- 每日统计
    `daily_kills` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '今日击杀',
    `daily_exp` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '今日修为',
    `daily_enhance` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '今日强化',
    `daily_items_used` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '今日使用物品',
    `daily_battles` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '今日战斗',
    `daily_breakthroughs` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '今日突破',
    `daily_reset_date` DATE DEFAULT NULL COMMENT '重置日期',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家统计表';

-- ================================================================
-- 三、背包装备模块 (2张表)
-- ================================================================

-- 6. 背包物品表 (可堆叠物品)
DROP TABLE IF EXISTS `t_bag_item`;
CREATE TABLE `t_bag_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `item_id` INT NOT NULL COMMENT '物品配置ID',
    `count` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `locked` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否锁定',
    `expire_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '过期时间(NULL=永久)',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_item` (`player_id`, `item_id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='背包物品表';

-- 7. 装备表 (已装备/背包中的装备实例)
DROP TABLE IF EXISTS `t_equipment`;
CREATE TABLE `t_equipment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '装备唯一ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `item_id` INT NOT NULL COMMENT '物品配置ID',
    `equip_slot` VARCHAR(16) DEFAULT NULL COMMENT '装备槽位(NULL=在背包,WEAPON/ARMOR/HEAD/FEET/ARTIFACT)',
    -- 装备属性
    `quality` TINYINT NOT NULL DEFAULT 1 COMMENT '品质: 1白 2绿 3蓝 4紫 5橙 6红 7金',
    `enhance_level` INT NOT NULL DEFAULT 0 COMMENT '强化等级',
    `star_level` INT NOT NULL DEFAULT 0 COMMENT '升星等级',
    `bind_type` TINYINT NOT NULL DEFAULT 0 COMMENT '绑定: 0未绑 1装备绑 2拾取绑',
    `extra_attrs` JSON DEFAULT NULL COMMENT '随机词条 [{attr, value}]',
    `locked` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否锁定',
    `obtain_way` TINYINT NOT NULL DEFAULT 0 COMMENT '获取途径',
    `obtain_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '获取时间',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_slot` (`player_id`, `equip_slot`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='装备表';

-- ================================================================
-- 四、技能模块 (1张表)
-- ================================================================

-- 8. 玩家技能表
DROP TABLE IF EXISTS `t_player_skill`;
CREATE TABLE `t_player_skill` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `skill_id` INT NOT NULL COMMENT '技能配置ID',
    `level` INT NOT NULL DEFAULT 1 COMMENT '技能等级',
    `exp` INT NOT NULL DEFAULT 0 COMMENT '当前经验',
    `equipped` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否装备',
    `equip_slot` TINYINT DEFAULT NULL COMMENT '槽位(0-3主动, 99心法)',
    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_skill` (`player_id`, `skill_id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家技能表';

-- ================================================================
-- 五、任务模块 (3张表)
-- ================================================================

-- 9. 主线任务表
DROP TABLE IF EXISTS `t_main_quest`;
CREATE TABLE `t_main_quest` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `chapter_id` INT NOT NULL COMMENT '章节ID',
    `quest_id` INT NOT NULL COMMENT '任务ID',
    `progress` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '进度',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0进行中 1完成 2已领取',
    `complete_time` BIGINT UNSIGNED DEFAULT NULL,
    `claim_time` BIGINT UNSIGNED DEFAULT NULL,
    `version` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_quest` (`player_id`, `chapter_id`, `quest_id`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主线任务表';

-- 10. 每日任务表
DROP TABLE IF EXISTS `t_daily_quest`;
CREATE TABLE `t_daily_quest` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `quest_date` DATE NOT NULL COMMENT '任务日期',
    `quest_cfg_id` VARCHAR(64) NOT NULL COMMENT '配置ID',
    `progress` INT UNSIGNED NOT NULL DEFAULT 0,
    `target` INT UNSIGNED NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0,
    `claim_time` BIGINT UNSIGNED DEFAULT NULL,
    `version` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_date_quest` (`player_id`, `quest_date`, `quest_cfg_id`),
    KEY `idx_player_date` (`player_id`, `quest_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日任务表';

-- 11. 成就表
DROP TABLE IF EXISTS `t_achievement`;
CREATE TABLE `t_achievement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `achievement_cfg_id` VARCHAR(64) NOT NULL,
    `progress` INT UNSIGNED NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0,
    `complete_time` BIGINT UNSIGNED DEFAULT NULL,
    `claim_time` BIGINT UNSIGNED DEFAULT NULL,
    `version` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_achievement` (`player_id`, `achievement_cfg_id`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成就表';

-- ================================================================
-- 六、邮件模块 (2张表)
-- ================================================================

-- 12. 邮件表
DROP TABLE IF EXISTS `t_mail`;
CREATE TABLE `t_mail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `mail_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1系统 2战斗 3活动 4GM',
    `title` VARCHAR(64) NOT NULL,
    `content` TEXT NOT NULL,
    `sender` VARCHAR(32) NOT NULL DEFAULT '系统',
    `attachments` JSON DEFAULT NULL COMMENT '[{itemId, count}]',
    `is_read` TINYINT(1) NOT NULL DEFAULT 0,
    `read_time` BIGINT UNSIGNED DEFAULT NULL,
    `is_claimed` TINYINT(1) NOT NULL DEFAULT 0,
    `claim_time` BIGINT UNSIGNED DEFAULT NULL,
    `expire_time` BIGINT UNSIGNED NOT NULL,
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_expire` (`player_id`, `expire_time`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件表';

-- 13. 邮件模板表 (GM用)
DROP TABLE IF EXISTS `t_mail_template`;
CREATE TABLE `t_mail_template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(64) NOT NULL,
    `content` VARCHAR(512) NOT NULL,
    `sender_name` VARCHAR(32) NOT NULL DEFAULT '系统',
    `attachments` JSON DEFAULT NULL,
    `target_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1全服 2指定 3条件',
    `target_condition` JSON DEFAULT NULL,
    `valid_days` INT NOT NULL DEFAULT 7,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0待发 1已发 2撤回',
    `send_time` DATETIME DEFAULT NULL,
    `send_count` INT UNSIGNED DEFAULT 0,
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件模板表';

-- ================================================================
-- 七、战斗挂机模块 (2张表)
-- ================================================================

-- 14. 挂机记录表 (每次挂机会话)
DROP TABLE IF EXISTS `t_idle_battle`;
CREATE TABLE `t_idle_battle` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `map_id` INT NOT NULL,
    `start_time` BIGINT NOT NULL,
    `is_active` TINYINT(1) NOT NULL DEFAULT 1,
    `claim_time` BIGINT DEFAULT NULL,
    `total_exp` BIGINT NOT NULL DEFAULT 0,
    `total_stones` BIGINT NOT NULL DEFAULT 0,
    `battle_count` INT NOT NULL DEFAULT 0,
    `victory_count` INT NOT NULL DEFAULT 0,
    `event_count` INT NOT NULL DEFAULT 0 COMMENT '触发事件数',
    `version` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_active` (`player_id`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挂机记录表';

-- 15. 挂机事件表 (奇遇/随机奖励)
DROP TABLE IF EXISTS `t_idle_event`;
CREATE TABLE `t_idle_event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '事件ID',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `idle_battle_id` BIGINT NOT NULL COMMENT '关联的挂机会话ID',
    -- 事件信息
    `event_type` TINYINT NOT NULL COMMENT '事件类型: 1-奇遇 2-宝箱 3-Boss 4-秘境 5-偷袭 6-仙人指路',
    `event_cfg_id` VARCHAR(64) NOT NULL COMMENT '事件配置ID',
    `event_title` VARCHAR(64) NOT NULL COMMENT '事件标题(缓存用于展示)',
    `event_desc` VARCHAR(256) DEFAULT '' COMMENT '事件描述',
    `trigger_time` BIGINT UNSIGNED NOT NULL COMMENT '触发时间戳(毫秒)',
    -- 奖励信息
    `rewards` JSON NOT NULL COMMENT '奖励列表 [{type,id,count}]',
    `bonus_exp` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '额外经验',
    `bonus_stones` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '额外灵石',
    -- 领取状态
    `is_claimed` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已领取',
    `claim_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '领取时间',
    -- 展示控制
    `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读(前端展示用)',
    `priority` TINYINT NOT NULL DEFAULT 0 COMMENT '展示优先级(越大越靠前)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_idle_battle` (`idle_battle_id`),
    KEY `idx_player_unclaimed` (`player_id`, `is_claimed`),
    KEY `idx_trigger_time` (`trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挂机事件表';

-- ================================================================
-- 八、商店充值模块 (2张表)
-- ================================================================

-- 15. 商店购买记录
DROP TABLE IF EXISTS `t_shop_buy_record`;
CREATE TABLE `t_shop_buy_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `shop_item_id` INT NOT NULL,
    `bought_count` INT NOT NULL DEFAULT 0,
    `reset_date` DATE NOT NULL,
    `version` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_item_date` (`player_id`, `shop_item_id`, `reset_date`),
    KEY `idx_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商店购买记录';

-- 16. 充值订单表
DROP TABLE IF EXISTS `t_recharge_order`;
CREATE TABLE `t_recharge_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(64) NOT NULL,
    `player_id` BIGINT NOT NULL,
    `product_id` VARCHAR(64) NOT NULL,
    `product_name` VARCHAR(64) NOT NULL,
    `product_price` INT NOT NULL COMMENT '分',
    `jade_amount` INT NOT NULL,
    `bonus_jade` INT NOT NULL DEFAULT 0,
    `platform_type` TINYINT NOT NULL,
    `platform_order_no` VARCHAR(128) DEFAULT '',
    `pay_amount` INT NOT NULL,
    `pay_channel` VARCHAR(32) DEFAULT '',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0创建 1支付中 2成功 3发货 4失败 5退款',
    `pay_time` BIGINT UNSIGNED DEFAULT NULL,
    `deliver_time` BIGINT UNSIGNED DEFAULT NULL,
    `client_ip` VARCHAR(64) DEFAULT '',
    `device_id` VARCHAR(128) DEFAULT '',
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_player` (`player_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值订单表';

-- ================================================================
-- 九、宠物模块 (1张表)
-- ================================================================

-- 17. 战宠表
DROP TABLE IF EXISTS `t_pet`;
CREATE TABLE `t_pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `pet_cfg_id` VARCHAR(64) NOT NULL,
    `pet_name` VARCHAR(32) NOT NULL DEFAULT '',
    `pet_level` INT UNSIGNED NOT NULL DEFAULT 1,
    `pet_exp` BIGINT UNSIGNED NOT NULL DEFAULT 0,
    `pet_quality` TINYINT NOT NULL DEFAULT 1,
    `is_active` TINYINT(1) NOT NULL DEFAULT 0,
    `hunger` INT UNSIGNED NOT NULL DEFAULT 100,
    `mood` INT UNSIGNED NOT NULL DEFAULT 100,
    `bonus_atk` INT UNSIGNED NOT NULL DEFAULT 0,
    `bonus_def` INT UNSIGNED NOT NULL DEFAULT 0,
    `bonus_hp` INT UNSIGNED NOT NULL DEFAULT 0,
    `obtain_way` TINYINT NOT NULL DEFAULT 0,
    `obtain_time` BIGINT UNSIGNED DEFAULT 0,
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_active` (`player_id`, `is_active`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='战宠表';

-- ================================================================
-- 十、宗门模块 (1张表，贡献/俸禄统一在此)
-- ================================================================

-- 18. 宗门成员表
DROP TABLE IF EXISTS `t_sect_member`;
CREATE TABLE `t_sect_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `sect_id` VARCHAR(64) NOT NULL,
    `position` TINYINT NOT NULL DEFAULT 0 COMMENT '0弟子 1内门 2长老 3副掌门 4掌门',
    `contribution` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计贡献',
    `weekly_contribution` BIGINT UNSIGNED NOT NULL DEFAULT 0,
    `last_salary_time` BIGINT UNSIGNED DEFAULT 0 COMMENT '上次领俸禄时间',
    `total_salary_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player` (`player_id`, `is_deleted`),
    KEY `idx_sect` (`sect_id`),
    KEY `idx_contribution` (`contribution` DESC),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宗门成员表';

-- ================================================================
-- 十一、社交模块 (2张表)
-- ================================================================

-- 19. 好友表
DROP TABLE IF EXISTS `t_friend`;
CREATE TABLE `t_friend` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `friend_id` BIGINT NOT NULL,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0待确认 1已添加 2拒绝 3拉黑',
    `source` TINYINT NOT NULL DEFAULT 0 COMMENT '0搜索 1推荐 2战斗 3宗门',
    `intimacy` INT UNSIGNED NOT NULL DEFAULT 0,
    `remark` VARCHAR(32) DEFAULT '',
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_friend` (`player_id`, `friend_id`),
    KEY `idx_friend` (`friend_id`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';

-- 20. 聊天消息表
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `channel` TINYINT NOT NULL COMMENT '1世界 2宗门 3私聊 4系统',
    `sender_id` BIGINT NOT NULL,
    `sender_name` VARCHAR(32) NOT NULL,
    `sender_realm_id` INT DEFAULT 1,
    `receiver_id` BIGINT DEFAULT NULL COMMENT '私聊用',
    `sect_id` VARCHAR(64) DEFAULT NULL COMMENT '宗门频道用',
    `content` VARCHAR(500) NOT NULL,
    `msg_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1文本 2表情 3物品 4战斗',
    `extra_data` JSON DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_channel` (`channel`),
    KEY `idx_sender` (`sender_id`),
    KEY `idx_receiver` (`receiver_id`),
    KEY `idx_sect` (`sect_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- ================================================================
-- 十二、称号模块 (1张表)
-- ================================================================

-- 21. 称号解锁表
DROP TABLE IF EXISTS `t_player_title`;
CREATE TABLE `t_player_title` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `title_id` INT NOT NULL,
    `unlock_time` BIGINT UNSIGNED NOT NULL,
    `expire_time` BIGINT UNSIGNED DEFAULT NULL COMMENT 'NULL=永久',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_title` (`player_id`, `title_id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='称号解锁表';

-- ================================================================
-- 十三、抽卡模块 (1张表)
-- ================================================================

-- 22. 抽卡记录表
DROP TABLE IF EXISTS `t_gacha_log`;
CREATE TABLE `t_gacha_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `gacha_type` TINYINT NOT NULL COMMENT '1天机 2秘境 3限时',
    `pool_id` VARCHAR(32) DEFAULT '',
    `cost_type` TINYINT NOT NULL DEFAULT 1,
    `cost_amount` INT NOT NULL DEFAULT 0,
    `item_id` INT NOT NULL,
    `item_count` INT NOT NULL DEFAULT 1,
    `quality` TINYINT NOT NULL DEFAULT 1,
    `is_guaranteed` TINYINT(1) NOT NULL DEFAULT 0,
    `pity_count` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_type` (`player_id`, `gacha_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽卡记录表';

-- ================================================================
-- 十四、排行榜模块 (1张表)
-- ================================================================

-- 23. 排行榜快照表
DROP TABLE IF EXISTS `t_rank_snapshot`;
CREATE TABLE `t_rank_snapshot` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `rank_type` TINYINT NOT NULL COMMENT '1战力 2境界 3财富',
    `rank` INT NOT NULL,
    `player_id` BIGINT NOT NULL,
    `nickname` VARCHAR(32) NOT NULL,
    `avatar` VARCHAR(255) DEFAULT '',
    `realm_id` INT NOT NULL DEFAULT 1,
    `value` BIGINT NOT NULL,
    `snapshot_time` DATETIME NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_rank_time` (`rank_type`, `rank`, `snapshot_time`),
    KEY `idx_snapshot_time` (`snapshot_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排行榜快照表';

-- ================================================================
-- 十五、日志模块 (2张表，只写不改)
-- ================================================================

-- 24. 货币变动日志
DROP TABLE IF EXISTS `t_currency_log`;
CREATE TABLE `t_currency_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `currency_type` TINYINT NOT NULL COMMENT '1灵石 2仙玉 3绑玉',
    `change_type` TINYINT NOT NULL COMMENT '1增加 2减少',
    `change_amount` BIGINT NOT NULL,
    `before_amount` BIGINT UNSIGNED NOT NULL,
    `after_amount` BIGINT UNSIGNED NOT NULL,
    `source_type` TINYINT NOT NULL COMMENT '1战斗 2任务 3商店 4充值 5GM 6邮件 7成就',
    `source_id` VARCHAR(64) DEFAULT '',
    `remark` VARCHAR(256) DEFAULT '',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_player_type` (`player_id`, `currency_type`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='货币变动日志';

-- 25. 登录日志
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `player_id` BIGINT NOT NULL,
    `login_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0正常 1重连 2切换',
    `platform_type` TINYINT NOT NULL DEFAULT 0,
    `device_id` VARCHAR(128) DEFAULT '',
    `device_model` VARCHAR(64) DEFAULT '',
    `os_version` VARCHAR(32) DEFAULT '',
    `app_version` VARCHAR(32) DEFAULT '',
    `client_ip` VARCHAR(64) DEFAULT '',
    `login_time` BIGINT UNSIGNED NOT NULL,
    `logout_time` BIGINT UNSIGNED DEFAULT NULL,
    `online_duration` INT UNSIGNED DEFAULT 0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_player` (`player_id`),
    KEY `idx_login_time` (`login_time`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志';

-- ================================================================
-- 十六、公告模块 (1张表)
-- ================================================================

-- 26. 公告表
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `notice_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1系统 2活动 3更新 4紧急',
    `title` VARCHAR(64) NOT NULL,
    `content` TEXT NOT NULL,
    `summary` VARCHAR(128) DEFAULT '',
    `banner_url` VARCHAR(256) DEFAULT '',
    `priority` INT NOT NULL DEFAULT 0,
    `is_popup` TINYINT(1) NOT NULL DEFAULT 0,
    `is_force_read` TINYINT(1) NOT NULL DEFAULT 0,
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `platforms` VARCHAR(32) DEFAULT 'all',
    `min_version` VARCHAR(16) DEFAULT '',
    `max_version` VARCHAR(16) DEFAULT '',
    `version` INT NOT NULL DEFAULT 0,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,
    `created_by` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_type` (`notice_type`),
    KEY `idx_time` (`start_time`, `end_time`),
    KEY `idx_priority` (`priority` DESC),
    KEY `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ================================================================
-- 十七、抽卡保底计数表 (实时更新用)
-- ================================================================

-- 27. 抽卡保底计数表
DROP TABLE IF EXISTS `t_gacha_pity`;
CREATE TABLE `t_gacha_pity` (
    `player_id` BIGINT NOT NULL,
    `gacha_type` TINYINT NOT NULL COMMENT '抽卡类型',
    `pity_count` INT NOT NULL DEFAULT 0 COMMENT '距离保底次数',
    `last_guaranteed_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '上次保底时间',
    `version` INT NOT NULL DEFAULT 0,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`player_id`, `gacha_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽卡保底计数表';

-- ================================================================
-- 初始化完成
-- ================================================================
SELECT '逍遥游数据库初始化完成! v2.0 共创建27张表' AS 'Status';
