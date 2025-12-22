# 货币日志和登录日志使用指南

## 📋 概述

本文档说明如何在业务代码中正确使用 `CurrencyLogService` 和 `LoginLogService` 记录日志。

**作者**: xiaoyao
**日期**: 2025-12-22

---

## 🪙 货币日志使用

### 1. 基本用法

#### 记录货币增加

```java
@Resource
private CurrencyLogService currencyLogService;

@Resource
private PlayerService playerService;

// 示例：玩家完成任务获得灵石
public void completeQuest(Long playerId, String questId) {
    PlayerEntity player = playerService.getById(playerId);

    // 获取变动前的数量
    Long beforeAmount = player.getSpiritStone();

    // 增加货币
    Long rewardAmount = 1000L;
    player.setSpiritStone(beforeAmount + rewardAmount);
    playerService.updateById(player);

    // ✅ 记录日志（必须在更新数据库后）
    currencyLogService.logAdd(
        playerId,
        CurrencyType.SPIRIT_STONE,
        rewardAmount,
        beforeAmount,
        CurrencySourceType.QUEST,
        questId  // 任务ID作为来源ID
    );
}
```

#### 记录货币减少

```java
// 示例：玩家购买商品消耗灵石
public void buyShopItem(Long playerId, String itemId, Long price) {
    PlayerEntity player = playerService.getById(playerId);

    // 检查余额
    if (player.getSpiritStone() < price) {
        throw new RuntimeException("灵石不足");
    }

    // 获取变动前的数量
    Long beforeAmount = player.getSpiritStone();

    // 扣除货币
    player.setSpiritStone(beforeAmount - price);
    playerService.updateById(player);

    // ✅ 记录日志
    currencyLogService.logReduce(
        playerId,
        CurrencyType.SPIRIT_STONE,
        price,
        beforeAmount,
        CurrencySourceType.SHOP,
        itemId  // 商品ID作为来源ID
    );
}
```

### 2. 高级用法

#### 带备注的日志

```java
// 记录带备注的货币变动
currencyLogService.logChange(
    playerId,
    CurrencyType.JADE,
    CurrencyChangeType.ADD,
    100L,
    player.getJade(),
    CurrencySourceType.COMPENSATION,
    "bug_fix_001",
    "因BUG补偿100仙玉"  // 备注说明
);
```

#### GM操作日志

```java
// GM给玩家发放货币
public void gmGiveCurrency(Long gmId, Long playerId, Long amount, String reason) {
    PlayerEntity player = playerService.getById(playerId);
    Long beforeAmount = player.getSpiritStone();

    // 增加货币
    player.setSpiritStone(beforeAmount + amount);
    playerService.updateById(player);

    // ✅ 记录GM操作日志（会记录GM的ID）
    currencyLogService.logGmChange(
        playerId,
        gmId,
        CurrencyType.SPIRIT_STONE,
        CurrencyChangeType.ADD,
        amount,
        beforeAmount,
        reason
    );
}
```

### 3. 批量操作示例

```java
// 示例：战斗结算，一次性获得多种货币
public void settleBattle(Long playerId, BattleReward reward) {
    PlayerEntity player = playerService.getById(playerId);

    // 记录灵石变动
    if (reward.getSpiritStone() > 0) {
        Long before = player.getSpiritStone();
        player.setSpiritStone(before + reward.getSpiritStone());

        currencyLogService.logAdd(
            playerId,
            CurrencyType.SPIRIT_STONE,
            reward.getSpiritStone(),
            before,
            CurrencySourceType.COMBAT,
            reward.getBattleId()
        );
    }

    // 记录仙玉变动
    if (reward.getJade() > 0) {
        Long before = player.getJade();
        player.setJade(before + reward.getJade());

        currencyLogService.logAdd(
            playerId,
            CurrencyType.JADE,
            reward.getJade(),
            before,
            CurrencySourceType.COMBAT,
            reward.getBattleId()
        );
    }

    // 最后统一更新玩家数据
    playerService.updateById(player);
}
```

---

## 🔐 登录日志使用

### 1. 登录流程

```java
@Resource
private LoginLogService loginLogService;

// 在 PlayerAction 的登录方法中
@ActionMethod(PlayerCmd.login)
public LoginResp login(LoginReq req, FlowContext ctx) {
    // 1. 验证登录
    PlayerEntity player = playerService.loginOrRegister(req.getDeviceId());

    // 2. 获取客户端信息（可从请求头或FlowContext中获取）
    String clientIp = getClientIp(ctx);

    // 3. ✅ 记录登录日志
    Long loginLogId = loginLogService.logLogin(
        player.getId(),
        req.getPlatform(),      // 平台类型
        req.getDeviceId(),      // 设备ID
        clientIp                // 客户端IP
    );

    // 4. 将 loginLogId 保存到会话中（用于后续登出）
    // 方式1: 保存到 Redis 会话
    sessionService.saveLoginLogId(player.getId(), loginLogId);

    // 方式2: 或者保存到 FlowContext（如果支持）
    // ctx.option().put("loginLogId", loginLogId);

    // 5. 返回登录响应
    return buildLoginResp(player);
}
```

### 2. 完整登录（带设备信息）

```java
// 如果客户端传递了完整的设备信息
@ActionMethod(PlayerCmd.login)
public LoginResp loginWithDeviceInfo(LoginReq req, FlowContext ctx) {
    PlayerEntity player = playerService.loginOrRegister(req.getDeviceId());

    // ✅ 记录完整的登录信息
    Long loginLogId = loginLogService.logLogin(
        player.getId(),
        0,                          // loginType: 0-正常登录
        req.getPlatform(),          // platformType
        req.getDeviceId(),          // deviceId
        req.getDeviceModel(),       // deviceModel: "iPhone 15 Pro"
        req.getOsVersion(),         // osVersion: "iOS 17.1"
        req.getAppVersion(),        // appVersion: "1.0.0"
        getClientIp(ctx)            // clientIp
    );

    sessionService.saveLoginLogId(player.getId(), loginLogId);

    return buildLoginResp(player);
}
```

### 3. 登出流程

```java
// 方式1: 通过 loginLogId 登出（推荐）
@ActionMethod(PlayerCmd.logout)
public void logout(FlowContext ctx) {
    Long playerId = ctx.getUserId();

    // 从会话中获取 loginLogId
    Long loginLogId = sessionService.getLoginLogId(playerId);

    // ✅ 记录登出
    loginLogService.logLogout(loginLogId);

    // 清理会话
    sessionService.removeSession(playerId);
}

// 方式2: 通过 playerId 登出（自动查找最后一次登录）
@ActionMethod(PlayerCmd.logout)
public void logoutSimple(FlowContext ctx) {
    Long playerId = ctx.getUserId();

    // ✅ 自动查找最后一次登录记录并更新登出时间
    loginLogService.logLogoutByPlayerId(playerId);

    sessionService.removeSession(playerId);
}
```

### 4. 断线重连

```java
// 处理断线重连（不记录新的登录日志，使用已有的）
@ActionMethod(PlayerCmd.reconnect)
public LoginResp reconnect(FlowContext ctx) {
    Long playerId = ctx.getUserId();

    // 获取当前的 loginLogId（未登出的）
    Long loginLogId = loginLogService.getCurrentLoginLogId(playerId);

    if (loginLogId != null) {
        // 使用现有的登录日志
        log.info("玩家重连 playerId={} loginLogId={}", playerId, loginLogId);
    } else {
        // 如果没有有效的登录日志，记录新的
        loginLogId = loginLogService.logLogin(
            playerId,
            1,  // loginType: 1-断线重连
            getPlatformType(ctx),
            getDeviceId(ctx),
            getClientIp(ctx)
        );
    }

    sessionService.saveLoginLogId(playerId, loginLogId);

    return buildLoginResp(playerService.getById(playerId));
}
```

### 5. 服务器关闭时批量登出

```java
// 在服务器关闭时，为所有在线玩家记录登出
@PreDestroy
public void onServerShutdown() {
    log.info("服务器关闭，批量记录玩家登出");

    // 获取所有在线玩家
    List<Long> onlinePlayers = sessionService.getAllOnlinePlayers();

    for (Long playerId : onlinePlayers) {
        loginLogService.logLogoutByPlayerId(playerId);
    }
}
```

---

## ⚠️ 重要注意事项

### 1. 货币日志记录时机

```java
// ❌ 错误：在更新数据库之前记录日志
Long beforeAmount = player.getSpiritStone();
currencyLogService.logAdd(...);  // ❌ 错误位置
player.setSpiritStone(beforeAmount + 100);
playerService.updateById(player);  // 如果这里失败，日志就不准确了

// ✅ 正确：先更新数据库，再记录日志
Long beforeAmount = player.getSpiritStone();
player.setSpiritStone(beforeAmount + 100);
playerService.updateById(player);  // 先更新
currencyLogService.logAdd(...);    // ✅ 再记录日志
```

### 2. beforeAmount 必须准确

```java
// ❌ 错误：beforeAmount 使用了更新后的值
player.setSpiritStone(player.getSpiritStone() + 100);
currencyLogService.logAdd(
    playerId,
    CurrencyType.SPIRIT_STONE,
    100L,
    player.getSpiritStone(),  // ❌ 这是更新后的值！
    CurrencySourceType.QUEST,
    questId
);

// ✅ 正确：先保存 beforeAmount
Long beforeAmount = player.getSpiritStone();  // ✅ 先保存
player.setSpiritStone(beforeAmount + 100);
currencyLogService.logAdd(
    playerId,
    CurrencyType.SPIRIT_STONE,
    100L,
    beforeAmount,  // ✅ 使用保存的值
    CurrencySourceType.QUEST,
    questId
);
```

### 3. 日志记录失败不影响主流程

```java
// 日志服务内部已捕获异常，不会影响主业务流程
// 即使日志记录失败，货币变动仍然会成功
player.setSpiritStone(player.getSpiritStone() + 100);
playerService.updateById(player);

// 即使这里失败，也只会记录错误日志，不会抛出异常
currencyLogService.logAdd(...);  // 内部已捕获异常
```

### 4. sourceId 的规范

```java
// 建议格式：
// - 任务: questId (如 "quest_001")
// - 商店: itemId (如 "item_potion_001")
// - 战斗: battleId (如 "battle_12345")
// - 充值: orderNo (如 "order_20231221001")
// - GM: "GM:" + gmId (如 "GM:10001")

currencyLogService.logAdd(
    playerId,
    CurrencyType.SPIRIT_STONE,
    1000L,
    beforeAmount,
    CurrencySourceType.QUEST,
    "quest_daily_001"  // ✅ 清晰的来源ID
);
```

---

## 📊 日志查询示例

### 1. 查询玩家货币变动记录

```java
// 查询最近50条灵石变动记录
List<CurrencyLogEntity> logs = currencyLogMapper.selectRecentLogs(
    playerId,
    CurrencyType.SPIRIT_STONE.getCode(),
    50
);

// 统计玩家灵石总收入
Long totalIncome = currencyLogMapper.sumIncome(
    playerId,
    CurrencyType.SPIRIT_STONE.getCode()
);

// 统计玩家灵石总支出
Long totalExpense = currencyLogMapper.sumExpense(
    playerId,
    CurrencyType.SPIRIT_STONE.getCode()
);
```

### 2. 查询玩家登录记录

```java
// 查询最近10次登录记录
List<LoginLogEntity> logs = loginLogMapper.selectRecentLogins(playerId, 10);

// 统计今日DAU
long todayStart = getTodayStartTimestamp();
long todayEnd = getTodayEndTimestamp();
Long dau = loginLogMapper.countDAU(todayStart, todayEnd);
```

---

## 🎯 最佳实践总结

1. ✅ **务必先更新数据库，再记录日志**
2. ✅ **准确保存 beforeAmount，不要用更新后的值**
3. ✅ **sourceId 要规范命名，方便追溯**
4. ✅ **GM操作使用专用方法 `logGmChange`**
5. ✅ **登录时记录 loginLogId 到会话，登出时使用**
6. ✅ **服务器关闭时批量记录登出**
7. ✅ **重要操作添加备注 (remark) 说明**

---

**文档完成时间**: 2025-12-22
**维护者**: xiaoyao
