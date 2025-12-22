# 数据库设计重构总结

## 📋 重构概述

**日期**: 2025-12-22
**作者**: xiaoyao
**版本**: v1.0

本次重构按照企业级规范完善了数据库设计和实体类结构，主要包括：

1. ✅ 创建 `BaseEntity` 公共基类
2. ✅ 补充缺失的 10 张核心表
3. ✅ 为现有表补充规范的公共字段
4. ✅ 所有 Entity 类继承 `BaseEntity`
5. ✅ 配置 MyBatis-Plus 全局支持逻辑删除和字段自动填充

---

## 🎯 新增内容

### 1. 公共基类 `BaseEntity`

**位置**: `xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/BaseEntity.java`

**包含字段**:
- `version` - 乐观锁版本号（防止并发冲突）
- `isDeleted` - 逻辑删除标志
- `createdBy` - 创建人ID
- `createdAt` - 创建时间
- `updatedBy` - 更新人ID
- `updatedAt` - 更新时间
- `deletedBy` - 删除人ID
- `deletedAt` - 删除时间

**MyBatis-Plus 注解**:
- `@Version` - 乐观锁
- `@TableLogic` - 逻辑删除
- `@TableField(fill = FieldFill.INSERT)` - 插入时自动填充
- `@TableField(fill = FieldFill.INSERT_UPDATE)` - 更新时自动填充

### 2. 字段自动填充处理器

**位置**: `xiaoyao-logic/src/main/java/com/xiaoyao/logic/config/MetaObjectHandlerConfig.java`

**功能**:
- 插入时自动填充 `createdAt` 和 `updatedAt`
- 更新时自动填充 `updatedAt`
- 支持扩展：可从 ionet FlowContext 获取当前用户ID填充 `createdBy` 和 `updatedBy`

### 3. MyBatis-Plus 全局配置

**位置**: `xiaoyao-logic/src/main/java/com/xiaoyao/logic/config/MybatisPlusConfig.java`

**新增配置**:
```java
// 逻辑删除全局配置
dbConfig.setLogicDeleteField("isDeleted");
dbConfig.setLogicDeleteValue("1");
dbConfig.setLogicNotDeleteValue("0");

// 字段自动填充
globalConfig.setMetaObjectHandler(metaObjectHandler);
```

---

## 📊 数据库迁移SQL

### 迁移文件

| 文件 | 说明 | 内容 |
|------|------|------|
| `docs/sql/migration_v1.0_new_tables.sql` | 新建表 | 10张缺失的核心表 |
| `docs/sql/migration_v1.0_alter_tables.sql` | 修改现有表 | 为9张表补充公共字段 |

### 新建的10张表（P0-P1优先级）

#### P0 核心表（必须立即创建）

1. **t_player_stats** - 玩家属性表
   - 用途：战斗力计算、属性管理
   - 字段：base_atk, base_def, crit_rate, dodge_rate 等

2. **t_currency_log** - 货币变动日志表 ⭐⭐⭐⭐⭐
   - 用途：对账、审计、作弊检测
   - 字段：currency_type, change_amount, before_amount, after_amount, source_type

3. **t_login_log** - 登录日志表 ⭐⭐⭐⭐⭐
   - 用途：留存分析、DAU统计、异常检测
   - 字段：login_time, logout_time, online_duration, platform_type

4. **t_recharge_order** - 充值订单表 ⭐⭐⭐⭐⭐
   - 用途：支付对账、收入统计
   - 字段：order_no, product_id, pay_amount, status, platform_order_no

#### P1 功能表（近期补充）

5. **t_pet** - 战宠表
   - 用途：战宠系统
   - 字段：pet_cfg_id, pet_level, is_active, bonus_atk

6. **t_sect_member** - 宗门成员表
   - 用途：宗门系统
   - 字段：sect_id, position, contribution, last_salary_time

7. **t_daily_quest** - 每日任务表
   - 用途：每日任务系统
   - 字段：quest_date, quest_cfg_id, progress, status

8. **t_achievement** - 成就记录表
   - 用途：成就系统
   - 字段：achievement_cfg_id, progress, complete_time

9. **t_mail_template** - 全服邮件模板表
   - 用途：GM批量发送邮件
   - 字段：target_type, start_time, end_time, send_count

10. **t_notice** - 公告表
    - 用途：游戏公告管理
    - 字段：notice_type, title, content, priority, is_popup

### 修改的9张现有表

所有现有表都补充了以下公共字段：
- `version` - 乐观锁
- `is_deleted` - 逻辑删除
- `created_by` / `updated_by` / `deleted_by` - 操作人追溯
- `created_at` / `updated_at` / `deleted_at` - 时间字段
- 字段命名统一：`create_time` → `created_at`, `update_time` → `updated_at`

特别说明：
- **t_mail** 额外补充：`mail_type`, `read_time`, `claim_time`
- 所有表添加 `idx_deleted` 索引

---

## 🔄 Entity 类更新

### 已更新的 6 个 Entity 类

所有 Entity 类已继承 `BaseEntity` 并移除重复的公共字段：

| Entity 类 | 表名 | 说明 |
|-----------|------|------|
| PlayerEntity | t_player | 玩家基础实体 |
| BagItemEntity | t_bag_item | 背包物品实体 |
| EquipmentEntity | t_equipment | 装备实体 |
| PlayerSkillEntity | t_player_skill | 玩家技能实体 |
| MailEntity | t_mail | 邮件实体 |
| IdleBattleEntity | t_idle_battle | 挂机记录实体 |

### 示例代码

```java
@Data
@TableName("t_player")
public class PlayerEntity extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String nickname;
    private Integer realmId;
    // ... 业务字段

    // 公共字段 (version, is_deleted, created_at等) 已由 BaseEntity 提供
}
```

---

## 🚀 执行数据库迁移

### 步骤1：备份现有数据库

```bash
# 备份数据库
mysqldump -u root -p xiaoyao_game > backup_before_migration_$(date +%Y%m%d).sql
```

### 步骤2：执行迁移SQL

```bash
# 1. 先修改现有表（补充公共字段）
mysql -u root -p xiaoyao_game < docs/sql/migration_v1.0_alter_tables.sql

# 2. 再创建新表
mysql -u root -p xiaoyao_game < docs/sql/migration_v1.0_new_tables.sql
```

### 步骤3：验证迁移结果

```sql
-- 检查公共字段是否添加成功
DESC t_player;

-- 检查新表是否创建成功
SHOW TABLES LIKE 't_%';

-- 检查逻辑删除配置
SELECT * FROM t_player WHERE is_deleted = 0;
```

---

## ⚠️ 注意事项

### 1. 数据兼容性

迁移后现有数据的公共字段默认值：
- `version` = 0
- `is_deleted` = 0
- `created_by` / `updated_by` = 0
- `deleted_by` = NULL
- `deleted_at` = NULL

### 2. 代码修改影响

**原字段名变更**:
```java
// 旧代码
entity.getCreateTime();  // ❌ 不存在
entity.getUpdateTime();  // ❌ 不存在

// 新代码
entity.getCreatedAt();   // ✅ 正确
entity.getUpdatedAt();   // ✅ 正确
```

**逻辑删除查询**:
```java
// MyBatis-Plus 会自动添加 is_deleted = 0 条件
playerMapper.selectById(playerId);  // 自动过滤已删除数据

// 如需查询包含已删除的数据
playerMapper.selectList(
    Wrappers.<PlayerEntity>lambdaQuery()
        .eq(PlayerEntity::getId, playerId)
        // 不添加 is_deleted 条件即可查询全部
);
```

### 3. 逻辑删除 vs 物理删除

```java
// 逻辑删除（推荐）- 数据可追溯
playerMapper.deleteById(playerId);
// SQL: UPDATE t_player SET is_deleted=1, deleted_at=NOW() WHERE id=?

// 物理删除（谨慎使用）
playerMapper.delete(
    Wrappers.<PlayerEntity>lambdaQuery()
        .eq(PlayerEntity::getId, playerId)
        .eq(PlayerEntity::getIsDeleted, 1)  // 仅删除已标记删除的数据
);
```

### 4. 乐观锁使用

```java
// 更新数据时自动应用乐观锁
PlayerEntity player = playerMapper.selectById(playerId);
player.setNickname("新昵称");

int rows = playerMapper.updateById(player);
// SQL: UPDATE t_player SET nickname=?, version=version+1
//      WHERE id=? AND version=?

if (rows == 0) {
    // 更新失败 - 数据已被其他线程修改
    throw new RuntimeException("数据已被修改，请刷新后重试");
}
```

---

## 📋 TODO：待补充的Entity类

根据新建的10张表，还需要创建对应的Entity类：

### P0 优先级
1. **PlayerStatsEntity** - 对应 t_player_stats
2. **CurrencyLogEntity** - 对应 t_currency_log
3. **LoginLogEntity** - 对应 t_login_log
4. **RechargeOrderEntity** - 对应 t_recharge_order

### P1 优先级
5. **PetEntity** - 对应 t_pet
6. **SectMemberEntity** - 对应 t_sect_member
7. **DailyQuestEntity** - 对应 t_daily_quest
8. **AchievementEntity** - 对应 t_achievement
9. **MailTemplateEntity** - 对应 t_mail_template
10. **NoticeEntity** - 对应 t_notice

### Entity 创建模板

```java
package com.xiaoyao.logic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * XXX实体
 *
 * @author xiaoyao
 */
@Data
@TableName("t_xxx")
public class XxxEntity extends BaseEntity {

    /** 主键ID */
    @TableId(type = IdType.AUTO)  // 或 IdType.ASSIGN_ID
    private Long id;

    // ... 业务字段

    // 公共字段已由 BaseEntity 提供
}
```

---

## ✅ 重构完成清单

- [x] 创建 BaseEntity 公共基类
- [x] 创建 MetaObjectHandlerConfig 自动填充处理器
- [x] 更新 MybatisPlusConfig 全局配置
- [x] 生成 10 张缺失表的建表SQL
- [x] 生成 9 张现有表的ALTER SQL
- [x] 更新 6 个现有Entity类继承BaseEntity
- [x] 统一所有类的 @author 为 xiaoyao
- [ ] 执行数据库迁移SQL（需手动执行）
- [ ] 创建 10 个新表对应的Entity类（按优先级）
- [ ] 创建对应的 Mapper 接口
- [ ] 创建对应的 Service 和 Action

---

## 📚 参考文档

- [规划文档对比分析](/Users/kuankuan/games/xiaoyao-server/docs/table_comparison.md)
- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [逻辑删除配置](https://baomidou.com/pages/6b03c5/)
- [乐观锁插件](https://baomidou.com/pages/0d93c0/)

---

**重构完成时间**: 2025-12-22
**下一步**: 执行数据库迁移 → 补充新Entity类 → 完善业务逻辑
