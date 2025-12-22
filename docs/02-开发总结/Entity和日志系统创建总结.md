# Entity类和日志系统创建总结

## 📋 创建概述

**日期**: 2025-12-22
**作者**: xiaoyao
**任务**: 创建P0优先级的Entity类和Mapper，设计货币日志/登录日志记录逻辑

---

## ✅ 已创建的文件清单

### 1. Entity 类 (4个)

| 文件名 | 路径 | 对应表 | 说明 |
|--------|------|--------|------|
| PlayerStatsEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_player_stats | 玩家属性实体 |
| CurrencyLogEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_currency_log | 货币变动日志实体 |
| LoginLogEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_login_log | 登录日志实体 |
| RechargeOrderEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_recharge_order | 充值订单实体 |

**设计说明**:
- ✅ `PlayerStatsEntity` 继承 `BaseEntity`（需要更新/删除）
- ✅ `RechargeOrderEntity` 继承 `BaseEntity`（需要更新/删除）
- ❗ `CurrencyLogEntity` 和 `LoginLogEntity` 不继承 `BaseEntity`（日志表只记录创建，不需要更新/删除）

### 2. Mapper 接口 (4个)

| 文件名 | 路径 | 说明 |
|--------|------|------|
| PlayerStatsMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | 玩家属性Mapper |
| CurrencyLogMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | 货币日志Mapper（含自定义查询） |
| LoginLogMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | 登录日志Mapper（含自定义查询） |
| RechargeOrderMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | 充值订单Mapper（含自定义查询） |

**功能亮点**:
- `CurrencyLogMapper`: 支持查询最近日志、统计收入/支出
- `LoginLogMapper`: 支持查询最近登录、统计DAU、更新登出时间
- `RechargeOrderMapper`: 支持按订单号查询、统计总充值

### 3. 枚举类 (3个)

| 文件名 | 路径 | 说明 |
|--------|------|------|
| CurrencyType.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/enums/ | 货币类型枚举 |
| CurrencyChangeType.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/enums/ | 货币变动类型枚举 |
| CurrencySourceType.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/enums/ | 货币来源类型枚举 |

**枚举定义**:
- `CurrencyType`: 灵石、仙玉、绑定仙玉
- `CurrencyChangeType`: 增加、减少
- `CurrencySourceType`: 战斗、任务、商店、充值、GM、邮件、成就等12种来源

### 4. Service 服务类 (2个)

| 文件名 | 路径 | 说明 |
|--------|------|------|
| CurrencyLogService.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/log/ | 货币日志服务 |
| LoginLogService.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/log/ | 登录日志服务 |

### 5. 文档 (1个)

| 文件名 | 路径 | 说明 |
|--------|------|------|
| LOG_USAGE_GUIDE.md | docs/ | 日志使用完整指南 |

---

## 🎯 核心功能说明

### CurrencyLogService - 货币日志服务

**核心方法**:
```java
// 记录货币增加
logAdd(playerId, currencyType, amount, beforeAmount, sourceType, sourceId)

// 记录货币减少
logReduce(playerId, currencyType, amount, beforeAmount, sourceType, sourceId)

// 记录GM操作
logGmChange(playerId, gmId, currencyType, changeType, amount, beforeAmount, remark)
```

**特性**:
- ✅ 自动计算 `afterAmount`
- ✅ 记录完整的变动信息（前/后/差额）
- ✅ 支持来源追溯（sourceType + sourceId）
- ✅ GM操作单独记录操作人
- ✅ 异常不影响主流程（内部捕获）

**使用场景**:
- 战斗获得灵石/仙玉
- 商店购买消耗货币
- 任务奖励发放
- GM补偿/扣除
- 充值到账
- 邮件领取
- 成就奖励

### LoginLogService - 登录日志服务

**核心方法**:
```java
// 记录登录
logLogin(playerId, platformType, deviceId, clientIp)

// 记录登出
logLogout(loginLogId)

// 通过玩家ID登出
logLogoutByPlayerId(playerId)

// 获取当前登录日志ID
getCurrentLoginLogId(playerId)
```

**特性**:
- ✅ 记录完整设备信息
- ✅ 自动计算在线时长
- ✅ 支持断线重连
- ✅ 支持DAU统计
- ✅ 返回loginLogId用于后续登出

**使用场景**:
- 玩家登录
- 玩家登出
- 断线重连
- 服务器关闭批量登出
- 留存分析
- 异常登录检测

---

## 📊 数据流程图

### 货币变动流程

```
┌─────────────┐
│ 业务操作    │ (完成任务、购买商品等)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 更新玩家数据 │ playerService.updateById(player)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 记录货币日志 │ currencyLogService.logAdd/logReduce
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 插入日志表  │ t_currency_log
└─────────────┘
```

### 登录登出流程

```
登录流程:
┌─────────────┐
│ 玩家登录    │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 验证/创建账号│
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 记录登录日志 │ loginLogService.logLogin
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 保存loginLogId│ 到Redis会话
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 返回登录响应 │
└─────────────┘

登出流程:
┌─────────────┐
│ 玩家登出    │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 获取loginLogId│ 从Redis会话
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 更新登出时间 │ loginLogService.logLogout
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 计算在线时长 │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ 更新日志表  │ t_login_log
└─────────────┘
```

---

## 🔧 集成步骤

### 步骤1：执行数据库迁移（如果还没有）

```bash
# 1. 先修改现有表
mysql -u root -p xiaoyao_game < docs/sql/migration_v1.0_alter_tables.sql

# 2. 再创建新表
mysql -u root -p xiaoyao_game < docs/sql/migration_v1.0_new_tables.sql
```

### 步骤2：在PlayerService中集成

```java
@Service
public class PlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Resource
    private CurrencyLogService currencyLogService;  // ✅ 注入日志服务

    /**
     * 增加玩家灵石
     */
    public void addSpiritStone(Long playerId, Long amount,
                               CurrencySourceType sourceType, String sourceId) {
        PlayerEntity player = playerMapper.selectById(playerId);

        // 保存变动前的数量
        Long beforeAmount = player.getSpiritStone();

        // 更新数据
        player.setSpiritStone(beforeAmount + amount);
        playerMapper.updateById(player);

        // ✅ 记录日志
        currencyLogService.logAdd(
            playerId,
            CurrencyType.SPIRIT_STONE,
            amount,
            beforeAmount,
            sourceType,
            sourceId
        );
    }
}
```

### 步骤3：在PlayerAction中集成

```java
@ActionController(PlayerCmd.cmd)
public class PlayerAction {

    @Resource
    private PlayerService playerService;

    @Resource
    private LoginLogService loginLogService;  // ✅ 注入日志服务

    @ActionMethod(PlayerCmd.login)
    public LoginResp login(LoginReq req, FlowContext ctx) {
        // 1. 登录逻辑
        PlayerEntity player = playerService.loginOrRegister(req.getDeviceId());

        // 2. ✅ 记录登录日志
        Long loginLogId = loginLogService.logLogin(
            player.getId(),
            req.getPlatform(),
            req.getDeviceId(),
            getClientIp(ctx)
        );

        // 3. 保存loginLogId到会话（用于登出）
        // TODO: 实现会话管理

        // 4. 返回响应
        return buildLoginResp(player);
    }

    @ActionMethod(PlayerCmd.logout)
    public void logout(FlowContext ctx) {
        Long playerId = ctx.getUserId();

        // ✅ 记录登出
        loginLogService.logLogoutByPlayerId(playerId);

        // 清理会话
        // TODO: 实现会话清理
    }
}
```

---

## ⚠️ 重要注意事项

### 1. 货币日志必须在更新后记录

```java
// ❌ 错误
currencyLogService.logAdd(...);  // 先记录
playerMapper.updateById(player);  // 后更新 - 如果失败日志就不准确

// ✅ 正确
playerMapper.updateById(player);  // 先更新
currencyLogService.logAdd(...);   // 后记录
```

### 2. beforeAmount必须准确

```java
// ❌ 错误
player.setSpiritStone(player.getSpiritStone() + 100);
currencyLogService.logAdd(..., player.getSpiritStone(), ...);  // 这是更新后的值

// ✅ 正确
Long beforeAmount = player.getSpiritStone();  // 先保存
player.setSpiritStone(beforeAmount + 100);
currencyLogService.logAdd(..., beforeAmount, ...);  // 使用保存的值
```

### 3. 日志表不需要逻辑删除

`CurrencyLogEntity` 和 `LoginLogEntity` 不继承 `BaseEntity`，因为：
- ❌ 日志表不需要更新
- ❌ 日志表不需要删除
- ✅ 日志表只记录创建信息
- ✅ 保持日志的完整性和准确性

---

## 📈 数据分析能力

### 货币日志支持的分析

1. **对账能力**: 通过 beforeAmount 和 afterAmount 对账
2. **来源分析**: 统计各来源的货币产出/消耗
3. **玩家行为**: 分析玩家的消费习惯
4. **经济平衡**: 监控游戏内经济系统健康度
5. **作弊检测**: 发现异常的货币变动

### 登录日志支持的分析

1. **DAU统计**: 每日活跃用户数
2. **留存分析**: 次日留存、7日留存
3. **在线时长**: 玩家平均在线时长
4. **设备分布**: 平台、设备型号分布
5. **异常检测**: 同一账号多设备登录

---

## 🎯 下一步计划

### 待补充的Entity类（P1优先级）

1. **PetEntity** - 战宠实体（对应 t_pet）
2. **SectMemberEntity** - 宗门成员实体（对应 t_sect_member）
3. **DailyQuestEntity** - 每日任务实体（对应 t_daily_quest）
4. **AchievementEntity** - 成就实体（对应 t_achievement）
5. **MailTemplateEntity** - 邮件模板实体（对应 t_mail_template）
6. **NoticeEntity** - 公告实体（对应 t_notice）

### 待实现的功能

1. **会话管理**: Redis存储loginLogId
2. **PlayerStats自动创建**: 玩家注册时自动创建属性记录
3. **充值流程**: 对接支付平台
4. **GM后台**: 管理界面操作货币/查看日志
5. **数据统计**: 定时任务统计DAU、留存等

---

## 📚 相关文档

| 文档 | 路径 | 说明 |
|------|------|------|
| 日志使用指南 | docs/LOG_USAGE_GUIDE.md | 详细的使用示例和最佳实践 |
| 重构总结 | docs/REFACTOR_SUMMARY.md | 数据库重构完整说明 |
| 表对比分析 | docs/table_comparison.md | 规划文档vs实际对比 |
| 新建表SQL | docs/sql/migration_v1.0_new_tables.sql | 10张新表的建表SQL |
| 修改表SQL | docs/sql/migration_v1.0_alter_tables.sql | 现有表补充公共字段 |

---

**完成时间**: 2025-12-22
**作者**: xiaoyao
**状态**: ✅ 已完成P0优先级所有任务
