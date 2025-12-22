# 数据库表设计对比报告

## 📋 表对比总览

| 表名 | 规划文档 | init.sql | 状态 | 说明 |
|------|:--------:|:--------:|:----:|------|
| t_player | ✅ | ✅ | 🟡 字段差异 | 缺少公共字段 |
| t_player_stats | ✅ | ❌ | 🔴 缺失 | 玩家属性表完全缺失 |
| t_inventory/t_bag_item | ✅ | ✅ | 🟡 命名+字段差异 | 表名不一致 |
| t_equipment | ✅ | ✅ | 🟡 字段差异 | 字段定义不同 |
| t_player_skill | ✅ | ✅ | 🟢 基本一致 | 字段命名略有不同 |
| t_pet | ✅ | ❌ | 🔴 缺失 | 战宠表完全缺失 |
| t_sect_member | ✅ | ❌ | 🔴 缺失 | 宗门成员表完全缺失 |
| t_daily_quest | ✅ | ❌ | 🔴 缺失 | 每日任务表完全缺失 |
| t_achievement | ✅ | ❌ | 🔴 缺失 | 成就记录表完全缺失 |
| t_login_log | ✅ | ❌ | 🔴 缺失 | 登录日志表完全缺失 |
| t_currency_log | ✅ | ❌ | 🔴 缺失 | 货币变动日志表完全缺失 |
| t_mail | ✅ | ✅ | 🟡 字段差异 | 缺少公共字段 |
| t_mail_template | ✅ | ❌ | 🔴 缺失 | 全服邮件模板表缺失 |
| t_recharge_order | ✅ | ❌ | 🔴 缺失 | 充值订单表完全缺失 |
| t_notice | ✅ | ❌ | 🔴 缺失 | 公告表完全缺失 |
| t_quest_progress | ❌ | ✅ | 🟠 额外 | init.sql额外添加的 |
| t_idle_battle | ❌ | ✅ | 🟠 额外 | init.sql额外添加的 |
| t_shop_buy_record | ❌ | ✅ | 🟠 额外 | init.sql额外添加的 |
| t_rank_snapshot | ❌ | ✅ | 🟠 额外 | init.sql额外添加的 |

**统计**:
- ✅ 规划文档定义: 15张表
- ✅ init.sql实现: 9张表
- 🔴 完全缺失: 9张表
- 🟠 额外添加: 4张表

---

## 🔴 完全缺失的表 (共9张)

### 1. **t_player_stats** - 玩家属性表 ⚠️ 重要
**优先级**: P0 (核心表)

```sql
-- 规划文档定义:
CREATE TABLE t_player_stats (
    id BIGINT NOT NULL COMMENT '玩家ID',

    -- 基础属性
    base_atk INT UNSIGNED NOT NULL DEFAULT 10,
    base_def INT UNSIGNED NOT NULL DEFAULT 5,
    base_hp INT UNSIGNED NOT NULL DEFAULT 100,
    base_mp INT UNSIGNED NOT NULL DEFAULT 50,

    -- 战斗属性
    crit_rate DECIMAL(5,4) NOT NULL DEFAULT 0.0500,
    crit_damage DECIMAL(5,4) NOT NULL DEFAULT 1.5000,
    dodge_rate DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    hit_rate DECIMAL(5,4) NOT NULL DEFAULT 1.0000,
    block_rate DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    lifesteal_rate DECIMAL(5,4) NOT NULL DEFAULT 0.0000,

    -- 计算属性
    combat_power BIGINT UNSIGNED NOT NULL DEFAULT 0,

    -- 公共字段 (version, is_deleted, created_at, updated_at, deleted_at)
    ...
);
```

**影响**: 当前 t_player 表中只有 combat_power,缺少详细的属性计算字段

---

### 2. **t_pet** - 战宠表
**优先级**: P1

```sql
CREATE TABLE t_pet (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    pet_cfg_id VARCHAR(64) NOT NULL,
    pet_name VARCHAR(32) NOT NULL DEFAULT '',
    pet_level INT UNSIGNED NOT NULL DEFAULT 1,
    pet_exp BIGINT UNSIGNED NOT NULL DEFAULT 0,
    pet_quality TINYINT NOT NULL DEFAULT 1,

    is_active TINYINT(1) NOT NULL DEFAULT 0,
    hunger INT UNSIGNED NOT NULL DEFAULT 100,
    mood INT UNSIGNED NOT NULL DEFAULT 100,

    bonus_atk INT UNSIGNED NOT NULL DEFAULT 0,
    bonus_def INT UNSIGNED NOT NULL DEFAULT 0,
    bonus_hp INT UNSIGNED NOT NULL DEFAULT 0,

    obtain_way TINYINT NOT NULL DEFAULT 0,
    obtain_time BIGINT UNSIGNED DEFAULT 0,

    -- 公共字段...
);
```

---

### 3. **t_sect_member** - 宗门成员表
**优先级**: P1

```sql
CREATE TABLE t_sect_member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    sect_id VARCHAR(64) NOT NULL,

    position TINYINT NOT NULL DEFAULT 0,
    contribution BIGINT UNSIGNED NOT NULL DEFAULT 0,
    weekly_contribution BIGINT UNSIGNED NOT NULL DEFAULT 0,

    last_salary_time BIGINT UNSIGNED DEFAULT 0,
    total_salary_count INT UNSIGNED NOT NULL DEFAULT 0,

    -- 公共字段...
);
```

---

### 4. **t_daily_quest** - 每日任务表
**优先级**: P1

```sql
CREATE TABLE t_daily_quest (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    quest_date DATE NOT NULL,
    quest_cfg_id VARCHAR(64) NOT NULL,

    progress INT UNSIGNED NOT NULL DEFAULT 0,
    target INT UNSIGNED NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 0,

    claim_time BIGINT UNSIGNED DEFAULT NULL,

    -- 公共字段...
);
```

**注**: init.sql 中有 `t_quest_progress`,但功能定义不同

---

### 5. **t_achievement** - 成就记录表
**优先级**: P1

```sql
CREATE TABLE t_achievement (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    achievement_cfg_id VARCHAR(64) NOT NULL,

    progress INT UNSIGNED NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 0,
    complete_time BIGINT UNSIGNED DEFAULT NULL,
    claim_time BIGINT UNSIGNED DEFAULT NULL,

    -- 公共字段...
);
```

---

### 6. **t_login_log** - 玩家登录日志表 ⚠️ 重要
**优先级**: P0 (数据分析必需)

```sql
CREATE TABLE t_login_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    login_type TINYINT NOT NULL DEFAULT 0,
    platform_type TINYINT NOT NULL DEFAULT 0,
    device_id VARCHAR(128) DEFAULT '',
    device_model VARCHAR(64) DEFAULT '',
    os_version VARCHAR(32) DEFAULT '',
    app_version VARCHAR(32) DEFAULT '',
    client_ip VARCHAR(64) DEFAULT '',

    login_time BIGINT UNSIGNED NOT NULL,
    logout_time BIGINT UNSIGNED DEFAULT NULL,
    online_duration INT UNSIGNED DEFAULT 0,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_player (player_id),
    INDEX idx_login_time (login_time)
) COMMENT='玩家登录日志表';
```

**用途**: 留存分析、活跃度统计、异常登录检测

---

### 7. **t_currency_log** - 货币变动日志表 ⚠️ 重要
**优先级**: P0 (对账审计必需)

```sql
CREATE TABLE t_currency_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    currency_type TINYINT NOT NULL,      -- 1=灵石 2=仙玉 3=绑定仙玉
    change_type TINYINT NOT NULL,        -- 1=增加 2=减少
    change_amount BIGINT NOT NULL,
    before_amount BIGINT UNSIGNED NOT NULL,
    after_amount BIGINT UNSIGNED NOT NULL,

    source_type TINYINT NOT NULL,        -- 1=战斗 2=任务 3=商店 4=充值 5=GM
    source_id VARCHAR(64) DEFAULT '',
    remark VARCHAR(256) DEFAULT '',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_player (player_id),
    INDEX idx_player_type (player_id, currency_type)
) COMMENT='货币变动日志表';
```

**用途**:
- 玩家对账纠纷
- 经济系统平衡分析
- 作弊检测
- GM操作审计

---

### 8. **t_mail_template** - 全服邮件模板表
**优先级**: P1

```sql
CREATE TABLE t_mail_template (
    id BIGINT NOT NULL AUTO_INCREMENT,

    title VARCHAR(64) NOT NULL,
    content VARCHAR(512) NOT NULL,
    sender_name VARCHAR(32) NOT NULL DEFAULT '系统',
    attachments JSON DEFAULT NULL,

    target_type TINYINT NOT NULL DEFAULT 1,   -- 1=全服 2=指定玩家 3=条件筛选
    target_condition JSON DEFAULT NULL,

    valid_days INT NOT NULL DEFAULT 7,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,

    status TINYINT NOT NULL DEFAULT 0,        -- 0=待发送 1=已发送 2=已撤回
    send_time DATETIME DEFAULT NULL,
    send_count INT UNSIGNED DEFAULT 0,

    -- 公共字段...
);
```

---

### 9. **t_recharge_order** - 充值订单表 ⚠️ 重要
**优先级**: P0 (商业化核心)

```sql
CREATE TABLE t_recharge_order (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL,

    player_id BIGINT NOT NULL,

    product_id VARCHAR(64) NOT NULL,
    product_name VARCHAR(64) NOT NULL,
    product_price INT NOT NULL,              -- 价格(分)
    jade_amount INT NOT NULL,
    bonus_jade INT NOT NULL DEFAULT 0,

    platform_type TINYINT NOT NULL,          -- 1=微信 2=抖音 3=苹果 4=安卓
    platform_order_no VARCHAR(128) DEFAULT '',
    pay_amount INT NOT NULL,
    pay_channel VARCHAR(32) DEFAULT '',

    status TINYINT NOT NULL DEFAULT 0,       -- 0=创建 1=支付中 2=成功 3=已发货 4=失败 5=退款
    pay_time BIGINT UNSIGNED DEFAULT NULL,
    deliver_time BIGINT UNSIGNED DEFAULT NULL,

    client_ip VARCHAR(64) DEFAULT '',
    device_id VARCHAR(128) DEFAULT '',

    -- 公共字段...

    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_player (player_id)
) COMMENT='充值订单表';
```

**用途**:
- 支付对账
- 收入统计
- 付费用户分析
- 小游戏平台结算

---

### 10. **t_notice** - 公告表
**优先级**: P1

```sql
CREATE TABLE t_notice (
    id BIGINT NOT NULL AUTO_INCREMENT,

    notice_type TINYINT NOT NULL DEFAULT 1,  -- 1=系统 2=活动 3=更新 4=紧急
    title VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,
    summary VARCHAR(128) DEFAULT '',
    banner_url VARCHAR(256) DEFAULT '',

    priority INT NOT NULL DEFAULT 0,
    is_popup TINYINT(1) NOT NULL DEFAULT 0,
    is_force_read TINYINT(1) NOT NULL DEFAULT 0,

    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,

    platforms VARCHAR(32) DEFAULT 'all',
    min_version VARCHAR(16) DEFAULT '',
    max_version VARCHAR(16) DEFAULT '',

    -- 公共字段...
);
```

---

## 🟡 字段差异的表

### 1. **t_player** - 缺少规范的公共字段

**规划文档要求**:
```sql
-- 公共字段规范
version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
created_by BIGINT DEFAULT 0 COMMENT '创建人ID',
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_by BIGINT DEFAULT 0 COMMENT '更新人ID',
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
deleted_by BIGINT DEFAULT NULL COMMENT '删除人ID',
deleted_at DATETIME DEFAULT NULL COMMENT '删除时间',
```

**init.sql 实际**:
```sql
-- 只有
create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
```

**缺失**:
- ❌ `version` - 乐观锁(防并发冲突)
- ❌ `is_deleted` - 逻辑删除(数据可追溯)
- ❌ `created_by/updated_by/deleted_by` - 操作人追溯
- ❌ `deleted_at` - 删除时间

**字段名差异**:
- 规划: `created_at` / `updated_at`
- 实际: `create_time` / `update_time`

---

### 2. **t_bag_item vs t_inventory** - 表名和字段定义差异

**规划文档** (`t_inventory`):
```sql
CREATE TABLE t_inventory (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    item_cfg_id VARCHAR(64) NOT NULL,        -- 配置ID (字符串)
    item_type TINYINT NOT NULL,              -- 物品类型
    count INT UNSIGNED NOT NULL DEFAULT 1,

    -- 装备专属字段
    quality TINYINT NOT NULL DEFAULT 1,
    enhance_level TINYINT UNSIGNED NOT NULL DEFAULT 0,
    star_level TINYINT UNSIGNED NOT NULL DEFAULT 0,
    is_locked TINYINT(1) NOT NULL DEFAULT 0,

    -- 获取信息
    obtain_way TINYINT NOT NULL DEFAULT 0,
    obtain_time BIGINT UNSIGNED DEFAULT 0,

    -- 扩展数据
    extra_data JSON DEFAULT NULL,

    -- 公共字段...
);
```

**init.sql** (`t_bag_item`):
```sql
CREATE TABLE t_bag_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    item_id INT NOT NULL,                    -- 物品ID (整数)
    count INT NOT NULL DEFAULT 1,
    slot INT NOT NULL DEFAULT 0,             -- 槽位
    locked TINYINT(1) NOT NULL DEFAULT 0,    -- 锁定

    create_time DATETIME,
    update_time DATETIME,

    UNIQUE KEY uk_player_item (player_id, item_id)
);
```

**差异**:
1. 表名不同: `t_inventory` vs `t_bag_item`
2. ❌ 缺少 `item_type` (物品分类)
3. ❌ 缺少装备专属字段 (quality/enhance_level/star_level)
4. ❌ 缺少获取信息 (obtain_way/obtain_time)
5. ❌ 缺少扩展数据 (extra_data JSON)
6. ❌ 缺少公共字段

---

### 3. **t_equipment** - 字段定义差异

**规划文档**:
```sql
CREATE TABLE t_equipment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    slot_type TINYINT NOT NULL,              -- 槽位类型 (数字枚举)
    inventory_id BIGINT DEFAULT NULL,        -- 关联背包物品ID

    -- 公共字段...
);
```

**init.sql**:
```sql
CREATE TABLE t_equipment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    slot VARCHAR(16) NOT NULL,               -- 槽位 (字符串枚举)
    item_id INT NOT NULL,                    -- 物品ID (直接存储)
    enhance_level INT NOT NULL DEFAULT 0,
    star_level INT NOT NULL DEFAULT 0,

    create_time DATETIME,
    update_time DATETIME,

    UNIQUE KEY uk_player_slot (player_id, slot)
);
```

**差异**:
1. 规划: `slot_type TINYINT` → 实际: `slot VARCHAR(16)` (类型不同)
2. 规划: `inventory_id` (关联背包) → 实际: `item_id` (直接存配置ID)
3. 实际多了 `enhance_level/star_level` (从背包物品复制)

---

### 4. **t_mail** - 字段差异

**规划文档**:
```sql
CREATE TABLE t_mail (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    mail_type TINYINT NOT NULL DEFAULT 1,    -- 邮件类型
    title VARCHAR(64) NOT NULL,
    content VARCHAR(512) NOT NULL DEFAULT '',
    sender_name VARCHAR(32) NOT NULL DEFAULT '系统',

    attachments JSON DEFAULT NULL,

    is_read TINYINT(1) NOT NULL DEFAULT 0,
    is_claimed TINYINT(1) NOT NULL DEFAULT 0,
    read_time BIGINT UNSIGNED DEFAULT NULL,
    claim_time BIGINT UNSIGNED DEFAULT NULL,

    expire_time BIGINT UNSIGNED NOT NULL,     -- 过期时间戳(毫秒)

    -- 公共字段...
);
```

**init.sql**:
```sql
CREATE TABLE t_mail (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,

    title VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,                    -- TEXT 而非 VARCHAR(512)
    sender VARCHAR(32) NOT NULL DEFAULT '系统',

    attachments JSON DEFAULT NULL,

    is_read TINYINT(1) NOT NULL DEFAULT 0,
    is_claimed TINYINT(1) NOT NULL DEFAULT 0,

    expire_time DATETIME NOT NULL,            -- DATETIME 而非 BIGINT

    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
);
```

**差异**:
1. ❌ 缺少 `mail_type` (邮件类型分类)
2. ❌ 缺少 `read_time` (阅读时间)
3. ❌ 缺少 `claim_time` (领取时间) - 只有 `is_claimed` 标志
4. 字段名: `sender_name` → `sender`
5. 类型: `content VARCHAR(512)` → `TEXT`
6. 类型: `expire_time BIGINT` → `DATETIME`

---

## 🟠 init.sql 额外添加的表

这些表在规划文档中没有定义,但在 init.sql 中实现了:

### 1. **t_quest_progress** - 任务进度表
```sql
-- 功能类似规划中的 t_daily_quest,但更通用
CREATE TABLE t_quest_progress (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    quest_id INT NOT NULL,
    progress INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 0,
    claim_time DATETIME DEFAULT NULL,
    ...
);
```

### 2. **t_idle_battle** - 挂机记录表
```sql
-- 规划文档未提及,但合理的扩展
CREATE TABLE t_idle_battle (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    map_id INT NOT NULL,
    start_time BIGINT NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    total_exp BIGINT NOT NULL DEFAULT 0,
    total_stones BIGINT NOT NULL DEFAULT 0,
    battle_count INT NOT NULL DEFAULT 0,
    ...
);
```

### 3. **t_shop_buy_record** - 商店购买记录表
```sql
-- 规划文档未提及,用于限购控制
CREATE TABLE t_shop_buy_record (
    id BIGINT NOT NULL AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    shop_item_id INT NOT NULL,
    bought_count INT NOT NULL DEFAULT 0,
    reset_date DATE NOT NULL,
    ...
);
```

### 4. **t_rank_snapshot** - 排行榜快照表
```sql
-- 规划文档提到可用Redis实现,这里选择了数据库
CREATE TABLE t_rank_snapshot (
    id BIGINT NOT NULL AUTO_INCREMENT,
    rank_type TINYINT NOT NULL,
    rank INT NOT NULL,
    player_id BIGINT NOT NULL,
    value BIGINT NOT NULL,
    snapshot_time DATETIME NOT NULL,
    ...
);
```

---

## 📋 总结建议

### 🔴 P0 优先级 (立即补充)

1. **t_currency_log** - 货币变动日志表
   - 用途: 对账、审计、作弊检测
   - 重要性: ⭐⭐⭐⭐⭐

2. **t_login_log** - 登录日志表
   - 用途: 留存分析、DAU统计
   - 重要性: ⭐⭐⭐⭐⭐

3. **t_recharge_order** - 充值订单表
   - 用途: 支付对账、收入统计
   - 重要性: ⭐⭐⭐⭐⭐

4. **规范化所有表的公共字段**
   - 添加: `version`, `is_deleted`, `created_by/updated_by/deleted_by`, `deleted_at`
   - 统一命名: `create_time` → `created_at`, `update_time` → `updated_at`
   - 重要性: ⭐⭐⭐⭐

5. **t_player_stats** - 玩家属性表
   - 用途: 战斗力计算、属性管理
   - 重要性: ⭐⭐⭐⭐

### 🟠 P1 优先级 (近期补充)

6. **t_pet** - 战宠表
7. **t_sect_member** - 宗门成员表
8. **t_daily_quest** - 每日任务表 (或改造 t_quest_progress)
9. **t_achievement** - 成就记录表
10. **t_mail_template** - 全服邮件模板表
11. **t_notice** - 公告表

### 🟡 P2 优先级 (按需添加)

12. **GM 相关表** (t_gm_operation_log 等)
13. **配置表版本管理表**
14. **其他业务表**

---

**下一步操作建议**:
1. ✅ 生成缺失的9张表的完整建表SQL
2. ✅ 生成所有表的公共字段补充ALTER语句
3. ✅ 生成字段差异修正的ALTER语句

需要我生成这些SQL脚本吗?
