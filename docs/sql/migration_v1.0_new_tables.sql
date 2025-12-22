-- ================================================================
-- 逍遥游 数据库迁移脚本 v1.0 - 新建表
-- 说明: 补充规划文档中定义但init.sql中缺失的9张表
-- 作者: xiaoyao
-- 日期: 2025-12-22
-- ================================================================

USE `xiaoyao_game`;

-- ================================================================
-- 1. 玩家属性表 (t_player_stats) - P0 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_player_stats` (
    `id` BIGINT NOT NULL COMMENT '玩家ID (与t_player.id一对一)',

    -- 基础属性
    `base_atk` INT UNSIGNED NOT NULL DEFAULT 10 COMMENT '基础攻击',
    `base_def` INT UNSIGNED NOT NULL DEFAULT 5 COMMENT '基础防御',
    `base_hp` INT UNSIGNED NOT NULL DEFAULT 100 COMMENT '基础生命',
    `base_mp` INT UNSIGNED NOT NULL DEFAULT 50 COMMENT '基础法力',

    -- 战斗属性
    `crit_rate` DECIMAL(5,4) NOT NULL DEFAULT 0.0500 COMMENT '暴击率 (0.05=5%)',
    `crit_damage` DECIMAL(5,4) NOT NULL DEFAULT 1.5000 COMMENT '暴击伤害倍率',
    `dodge_rate` DECIMAL(5,4) NOT NULL DEFAULT 0.0000 COMMENT '闪避率',
    `hit_rate` DECIMAL(5,4) NOT NULL DEFAULT 1.0000 COMMENT '命中率',
    `block_rate` DECIMAL(5,4) NOT NULL DEFAULT 0.0000 COMMENT '格挡率',
    `lifesteal_rate` DECIMAL(5,4) NOT NULL DEFAULT 0.0000 COMMENT '吸血率',

    -- 计算属性
    `combat_power` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '战斗力',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除',
    `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    INDEX `idx_combat_power` (`combat_power` DESC),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家属性表';

-- ================================================================
-- 2. 货币变动日志表 (t_currency_log) - P0 优先级 ⭐⭐⭐⭐⭐
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_currency_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',

    -- 货币信息
    `currency_type` TINYINT NOT NULL COMMENT '货币类型: 1-灵石 2-仙玉 3-绑定仙玉',
    `change_type` TINYINT NOT NULL COMMENT '变动类型: 1-增加 2-减少',
    `change_amount` BIGINT NOT NULL COMMENT '变动数量',
    `before_amount` BIGINT UNSIGNED NOT NULL COMMENT '变动前数量',
    `after_amount` BIGINT UNSIGNED NOT NULL COMMENT '变动后数量',

    -- 来源信息
    `source_type` TINYINT NOT NULL COMMENT '来源类型: 1-战斗 2-任务 3-商店 4-充值 5-GM 6-邮件 7-成就',
    `source_id` VARCHAR(64) DEFAULT '' COMMENT '来源ID (如任务ID、商品ID等)',
    `remark` VARCHAR(256) DEFAULT '' COMMENT '备注说明',

    -- 公共字段 (日志表只记录创建时间)
    `created_by` BIGINT DEFAULT 0 COMMENT '操作人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    INDEX `idx_player` (`player_id`),
    INDEX `idx_player_type` (`player_id`, `currency_type`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='货币变动日志表';

-- ================================================================
-- 3. 登录日志表 (t_login_log) - P0 优先级 ⭐⭐⭐⭐⭐
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',

    -- 登录信息
    `login_type` TINYINT NOT NULL DEFAULT 0 COMMENT '登录类型: 0-正常登录 1-断线重连 2-切换账号',
    `platform_type` TINYINT NOT NULL DEFAULT 0 COMMENT '平台类型: 0-游客 1-微信 2-抖音 3-iOS 4-Android',
    `device_id` VARCHAR(128) DEFAULT '' COMMENT '设备ID',
    `device_model` VARCHAR(64) DEFAULT '' COMMENT '设备型号',
    `os_version` VARCHAR(32) DEFAULT '' COMMENT '系统版本',
    `app_version` VARCHAR(32) DEFAULT '' COMMENT 'App版本',
    `client_ip` VARCHAR(64) DEFAULT '' COMMENT '客户端IP',

    -- 时间信息
    `login_time` BIGINT UNSIGNED NOT NULL COMMENT '登录时间戳(毫秒)',
    `logout_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '登出时间戳(毫秒)',
    `online_duration` INT UNSIGNED DEFAULT 0 COMMENT '在线时长(秒)',

    -- 公共字段 (日志表只记录创建时间)
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (`id`),
    INDEX `idx_player` (`player_id`),
    INDEX `idx_login_time` (`login_time`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家登录日志表';

-- ================================================================
-- 4. 充值订单表 (t_recharge_order) - P0 优先级 ⭐⭐⭐⭐⭐
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_recharge_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL COMMENT '订单号 (唯一)',

    -- 玩家信息
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',

    -- 商品信息
    `product_id` VARCHAR(64) NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(64) NOT NULL COMMENT '商品名称',
    `product_price` INT NOT NULL COMMENT '商品价格(分)',
    `jade_amount` INT NOT NULL COMMENT '仙玉数量',
    `bonus_jade` INT NOT NULL DEFAULT 0 COMMENT '赠送仙玉',

    -- 支付信息
    `platform_type` TINYINT NOT NULL COMMENT '平台类型: 1-微信 2-抖音 3-苹果 4-安卓',
    `platform_order_no` VARCHAR(128) DEFAULT '' COMMENT '平台订单号',
    `pay_amount` INT NOT NULL COMMENT '实际支付金额(分)',
    `pay_channel` VARCHAR(32) DEFAULT '' COMMENT '支付渠道',

    -- 订单状态
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-创建 1-支付中 2-支付成功 3-已发货 4-支付失败 5-已退款',
    `pay_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '支付时间戳(毫秒)',
    `deliver_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '发货时间戳(毫秒)',

    -- 客户端信息
    `client_ip` VARCHAR(64) DEFAULT '' COMMENT '客户端IP',
    `device_id` VARCHAR(128) DEFAULT '' COMMENT '设备ID',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    INDEX `idx_player` (`player_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_platform_order` (`platform_type`, `platform_order_no`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值订单表';

-- ================================================================
-- 5. 战宠表 (t_pet) - P1 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',

    -- 宠物信息
    `pet_cfg_id` VARCHAR(64) NOT NULL COMMENT '战宠配置ID',
    `pet_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '战宠名称(玩家自定义)',
    `pet_level` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '等级',
    `pet_exp` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '经验',
    `pet_quality` TINYINT NOT NULL DEFAULT 1 COMMENT '品质: 1白 2绿 3蓝 4紫 5橙 6红 7金',

    -- 状态
    `is_active` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否出战: 0-否 1-是',
    `hunger` INT UNSIGNED NOT NULL DEFAULT 100 COMMENT '饱食度 (0-100)',
    `mood` INT UNSIGNED NOT NULL DEFAULT 100 COMMENT '心情值 (0-100)',

    -- 属性加成
    `bonus_atk` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '攻击加成',
    `bonus_def` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '防御加成',
    `bonus_hp` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '生命加成',

    -- 获取信息
    `obtain_way` TINYINT NOT NULL DEFAULT 0 COMMENT '获取途径: 0-未知 1-战斗捕获 2-商店 3-孵化',
    `obtain_time` BIGINT UNSIGNED DEFAULT 0 COMMENT '获取时间戳(毫秒)',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    INDEX `idx_player` (`player_id`),
    INDEX `idx_player_active` (`player_id`, `is_active`),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='战宠表';

-- ================================================================
-- 6. 宗门成员表 (t_sect_member) - P1 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_sect_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `sect_id` VARCHAR(64) NOT NULL COMMENT '宗门配置ID',

    -- 成员信息
    `position` TINYINT NOT NULL DEFAULT 0 COMMENT '职位: 0-弟子 1-内门 2-长老 3-副掌门 4-掌门',
    `contribution` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '贡献度',
    `weekly_contribution` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '本周贡献度',

    -- 俸禄
    `last_salary_time` BIGINT UNSIGNED DEFAULT 0 COMMENT '上次领取俸禄时间戳(毫秒)',
    `total_salary_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '累计领取俸禄次数',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-正常 1-已删除(退出宗门)',
    `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID(踢出=操作者ID, 主动退出=自己ID)',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '退出时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player` (`player_id`, `is_deleted`),
    INDEX `idx_sect` (`sect_id`),
    INDEX `idx_contribution` (`contribution` DESC),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宗门成员表';

-- ================================================================
-- 7. 每日任务表 (t_daily_quest) - P1 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_daily_quest` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `quest_date` DATE NOT NULL COMMENT '任务日期',
    `quest_cfg_id` VARCHAR(64) NOT NULL COMMENT '任务配置ID',

    -- 任务状态
    `progress` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前进度',
    `target` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '目标进度',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-进行中 1-已完成 2-已领取',

    -- 领取信息
    `claim_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '领取奖励时间戳(毫秒)',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_date_quest` (`player_id`, `quest_date`, `quest_cfg_id`),
    INDEX `idx_player_date` (`player_id`, `quest_date`),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日任务表';

-- ================================================================
-- 8. 成就记录表 (t_achievement) - P1 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_achievement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `player_id` BIGINT NOT NULL COMMENT '玩家ID',
    `achievement_cfg_id` VARCHAR(64) NOT NULL COMMENT '成就配置ID',

    -- 成就状态
    `progress` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前进度',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-进行中 1-已完成 2-已领取',
    `complete_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '完成时间戳(毫秒)',
    `claim_time` BIGINT UNSIGNED DEFAULT NULL COMMENT '领取奖励时间戳(毫秒)',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_by` BIGINT DEFAULT 0 COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_player_achievement` (`player_id`, `achievement_cfg_id`),
    INDEX `idx_player` (`player_id`),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成就记录表';

-- ================================================================
-- 9. 全服邮件模板表 (t_mail_template) - P1 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_mail_template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',

    -- 邮件内容
    `title` VARCHAR(64) NOT NULL COMMENT '邮件标题',
    `content` VARCHAR(512) NOT NULL COMMENT '邮件内容',
    `sender_name` VARCHAR(32) NOT NULL DEFAULT '系统' COMMENT '发件人名称',
    `attachments` JSON DEFAULT NULL COMMENT '附件物品 [{itemId, count}, ...]',

    -- 发送范围
    `target_type` TINYINT NOT NULL DEFAULT 1 COMMENT '目标类型: 1-全服 2-指定玩家 3-条件筛选',
    `target_condition` JSON DEFAULT NULL COMMENT '筛选条件 (JSON格式)',

    -- 有效期
    `valid_days` INT NOT NULL DEFAULT 7 COMMENT '有效天数',
    `start_time` DATETIME NOT NULL COMMENT '生效时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',

    -- 状态
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待发送 1-已发送 2-已撤回',
    `send_time` DATETIME DEFAULT NULL COMMENT '实际发送时间',
    `send_count` INT UNSIGNED DEFAULT 0 COMMENT '发送数量',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_by` BIGINT NOT NULL COMMENT '创建人(GM)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='全服邮件模板表';

-- ================================================================
-- 10. 公告表 (t_notice) - P1 优先级
-- ================================================================
CREATE TABLE IF NOT EXISTS `t_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',

    -- 公告内容
    `notice_type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型: 1-系统公告 2-活动公告 3-更新公告 4-紧急公告',
    `title` VARCHAR(64) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容 (支持富文本)',
    `summary` VARCHAR(128) DEFAULT '' COMMENT '摘要',
    `banner_url` VARCHAR(256) DEFAULT '' COMMENT '横幅图片URL',

    -- 显示控制
    `priority` INT NOT NULL DEFAULT 0 COMMENT '优先级 (越大越靠前)',
    `is_popup` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否弹窗: 0-否 1-是',
    `is_force_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否强制阅读: 0-否 1-是',

    -- 时间控制
    `start_time` DATETIME NOT NULL COMMENT '生效时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',

    -- 平台控制
    `platforms` VARCHAR(32) DEFAULT 'all' COMMENT '目标平台: all/wechat/douyin/ios/android',
    `min_version` VARCHAR(16) DEFAULT '' COMMENT '最小版本号',
    `max_version` VARCHAR(16) DEFAULT '' COMMENT '最大版本号',

    -- 公共字段
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `created_by` BIGINT NOT NULL COMMENT '创建人(GM)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT 0 COMMENT '更新人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_by` BIGINT DEFAULT NULL COMMENT '删除人ID',
    `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间',

    PRIMARY KEY (`id`),
    INDEX `idx_type` (`notice_type`),
    INDEX `idx_time` (`start_time`, `end_time`),
    INDEX `idx_priority` (`priority` DESC),
    INDEX `idx_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ================================================================
-- 执行完成提示
-- ================================================================
SELECT 'v1.0 新建表迁移完成! 已创建10张表' AS 'Migration Status';
