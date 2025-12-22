# Service业务层使用文档

## 📋 概述

**作者**: xiaoyao
**日期**: 2025-12-22
**状态**: ✅ 已完成所有Service开发

本文档详细说明Service业务层的设计原则、使用方法和最佳实践。

---

## 🎯 设计原则

### 1. 健壮性设计

#### 参数校验
所有Service方法都进行严格的参数校验：
```java
// ✅ 正确示例
private void validatePlayerId(Long playerId) {
    if (playerId == null || playerId <= 0) {
        throw BusinessException.of(ErrorCode.PARAM_INVALID, "玩家ID无效");
    }
}
```

#### 异常处理
使用统一的BusinessException处理业务异常：
```java
// ✅ 业务异常抛出
if (petCount >= GameConstants.PET_MAX_COUNT) {
    throw BusinessException.of(ErrorCode.PET_COUNT_LIMIT,
            "战宠数量已达上限 count=%d", petCount);
}
```

#### 事务管理
关键操作使用@Transactional确保数据一致性：
```java
@Transactional(rollbackFor = Exception.class)
public void claimReward(Long playerId, String questCfgId) {
    // 事务内的操作
}
```

### 2. 可扩展性设计

#### 扩展点预留
每个Service都预留了扩展点用于回调：
```java
// 扩展点：成就完成回调
private void onAchievementCompleted(AchievementEntity achievement) {
    // TODO: 发送系统消息、更新称号等
}
```

#### 配置分离
业务逻辑与配置数据分离：
```java
// TODO: 从配置管理器读取
// PetConfig config = petConfigManager.getConfig(petCfgId);
```

#### 职责单一
每个Service只负责自己领域的业务逻辑：
- ✅ AchievementService - 只负责成就进度和领取
- ✅ 奖励发放由RewardService负责
- ✅ 日志记录由LogService负责

---

## 📚 Service清单

### 已创建的Service（6个）

| Service名称 | 职责 | 核心方法数 | 复杂度 |
|------------|------|----------|--------|
| NoticeService | 公告管理 | 8个 | ⭐⭐ |
| MailTemplateService | 邮件模板管理 | 9个 | ⭐⭐⭐ |
| AchievementService | 成就系统 | 8个 | ⭐⭐⭐ |
| DailyQuestService | 每日任务 | 9个 | ⭐⭐⭐ |
| PetService | 战宠管理 | 12个 | ⭐⭐⭐⭐ |
| SectService | 宗门管理 | 11个 | ⭐⭐⭐⭐⭐ |

### 基础设施

| 类名 | 说明 |
|------|------|
| BusinessException | 统一业务异常 |
| ErrorCode | 错误码常量（按模块划分） |
| GameConstants | 游戏业务常量 |

---

## 🔧 使用示例

### 1. NoticeService - 公告服务

#### 查询玩家可见的公告
```java
@Resource
private NoticeService noticeService;

// 查询当前平台的有效公告
List<NoticeEntity> notices = noticeService.getPlayerNotices("wechat", "1.0.0");

// 查询登录弹窗公告
List<NoticeEntity> popupNotices = noticeService.getLoginPopupNotices("wechat", "1.0.0");
```

#### 创建公告（GM后台）
```java
NoticeEntity notice = new NoticeEntity();
notice.setNoticeType(GameConstants.NOTICE_TYPE_SYSTEM);
notice.setTitle("系统维护公告");
notice.setContent("服务器将于今晚22:00进行维护...");
notice.setPriority(10);
notice.setIsPopup(GameConstants.YES);
notice.setStartTime(LocalDateTime.now());
notice.setEndTime(LocalDateTime.now().plusDays(7));
notice.setPlatforms("all");

Long noticeId = noticeService.createNotice(notice, gmId);
```

### 2. MailTemplateService - 邮件模板服务

#### 创建全服邮件模板
```java
@Resource
private MailTemplateService mailTemplateService;

MailTemplateEntity template = new MailTemplateEntity();
template.setTitle("新手福利");
template.setContent("欢迎来到修仙世界，这是给你的新手大礼包！");
template.setSenderName("系统");
template.setAttachments("[{\"itemId\":\"gold\",\"count\":1000}]");
template.setTargetType(GameConstants.MAIL_TARGET_ALL_PLAYERS);
template.setValidDays(7);
template.setStartTime(LocalDateTime.now());
template.setEndTime(LocalDateTime.now().plusDays(30));

Long templateId = mailTemplateService.createMailTemplate(template, gmId);
```

#### 发送邮件（外部服务调用）
```java
// 查询待发送的模板
List<MailTemplateEntity> pending = mailTemplateService.getPendingTemplates();

for (MailTemplateEntity template : pending) {
    // 实际发送逻辑由MailService实现
    int sentCount = mailService.sendToPlayers(template);

    // 标记为已发送
    mailTemplateService.markAsSent(template.getId(), sentCount);
}
```

### 3. AchievementService - 成就服务

#### 更新成就进度
```java
@Resource
private AchievementService achievementService;

// 玩家完成一场战斗，更新"战斗胜利次数"成就
boolean completed = achievementService.addProgress(
    playerId,
    "achievement_battle_win",
    1,  // 增加1次
    100 // 目标100次
);

if (completed) {
    // 成就完成，显示完成特效
    showAchievementCompletedAnimation();
}
```

#### 领取成就奖励
```java
// 查询可领取的成就（红点提示）
List<AchievementEntity> unclaimed = achievementService.getUnclaimedAchievements(playerId);

// 领取指定成就
try {
    achievementService.claimReward(playerId, "achievement_battle_win");
    // 成功领取，发放奖励
} catch (BusinessException e) {
    // 处理异常（未完成、已领取等）
    log.error("领取成就失败", e);
}
```

### 4. DailyQuestService - 每日任务服务

#### 更新任务进度
```java
@Resource
private DailyQuestService dailyQuestService;

// 玩家完成一场战斗，更新"每日战斗"任务
boolean completed = dailyQuestService.addProgress(
    playerId,
    "quest_daily_battle",
    1,  // 完成1次战斗
    10  // 目标10次
);

if (completed) {
    // 任务完成提示
    showQuestCompletedTip();
}
```

#### 查询今日任务
```java
// 查询今日所有任务
List<DailyQuestEntity> quests = dailyQuestService.getTodayQuests(playerId);

// 统计完成情况
int[] stats = dailyQuestService.getTodayStatistics(playerId);
int completedCount = stats[0];  // 已完成数
int claimedCount = stats[1];    // 已领取数

// 检查红点
boolean hasRedDot = dailyQuestService.hasUnclaimedQuests(playerId);
```

#### 定时任务清理过期记录
```java
// 每天凌晨执行，清理7天前的记录
@Scheduled(cron = "0 0 0 * * ?")
public void cleanOldQuests() {
    LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
    int count = dailyQuestService.cleanExpiredQuests(sevenDaysAgo);
    log.info("清理过期任务记录 count={}", count);
}
```

### 5. PetService - 战宠服务

#### 获得战宠
```java
@Resource
private PetService petService;

// 战斗捕获战宠
PetEntity pet = petService.obtainPet(
    playerId,
    "pet_phoenix",  // 战宠配置ID
    1               // 获取途径：战斗捕获
);
```

#### 战宠管理
```java
// 设置出战
petService.setActive(playerId, petId);

// 喂养（恢复饱食度）
petService.feed(playerId, petId, 50);

// 提升心情
petService.improveMood(playerId, petId, 30);

// 升级
boolean levelUp = petService.levelUp(playerId, petId, 1000L);
```

#### 计算属性加成
```java
// 获取所有出战战宠的属性加成
int[] bonus = petService.calculateTotalBonusAttributes(playerId);
int atkBonus = bonus[0];
int defBonus = bonus[1];
int hpBonus = bonus[2];

// 应用到玩家属性
playerStats.setTotalAtk(baseAtk + atkBonus);
```

### 6. SectService - 宗门服务

#### 加入宗门
```java
@Resource
private SectService sectService;

try {
    SectMemberEntity member = sectService.joinSect(playerId, "sect_tianjian");
    log.info("成功加入宗门 position={}", member.getPosition());
} catch (BusinessException e) {
    // 处理异常（已加入、人数已满等）
    showErrorMessage(e.getErrorMessage());
}
```

#### 贡献度管理
```java
// 完成宗门任务，增加贡献度
sectService.addContribution(playerId, 100L);

// 查询贡献榜
List<SectMemberEntity> rank = sectService.getContributionRank("sect_tianjian", 10);

// 领取俸禄
try {
    sectService.claimSalary(playerId);
    // 领取成功
} catch (BusinessException e) {
    if (e.getErrorCode() == ErrorCode.SECT_SALARY_COOLDOWN) {
        // 冷却中
        showCooldownTip(e.getErrorMessage());
    }
}
```

#### 权限管理
```java
// 晋升成员（需要权限）
try {
    sectService.changePosition(
        operatorId,
        targetPlayerId,
        GameConstants.SECT_POSITION_ELDER  // 晋升为长老
    );
} catch (BusinessException e) {
    if (e.getErrorCode() == ErrorCode.SECT_POSITION_NOT_ENOUGH) {
        // 权限不足
        showErrorMessage("您的权限不足");
    }
}

// 踢出成员（需要权限）
sectService.kickMember(operatorId, targetPlayerId);
```

---

## ⚠️ 注意事项

### 1. 异常处理

#### ✅ 正确处理
```java
try {
    achievementService.claimReward(playerId, achievementCfgId);
    // 成功处理
} catch (BusinessException e) {
    // 根据错误码进行不同处理
    switch (e.getErrorCode()) {
        case ErrorCode.ACHIEVEMENT_NOT_COMPLETED:
            showTip("成就未完成");
            break;
        case ErrorCode.ACHIEVEMENT_ALREADY_CLAIMED:
            showTip("成就已领取");
            break;
        default:
            showTip(e.getErrorMessage());
    }
}
```

#### ❌ 错误处理
```java
// 不要吞掉异常
try {
    achievementService.claimReward(playerId, achievementCfgId);
} catch (Exception e) {
    // 什么都不做 - 错误！
}
```

### 2. 事务边界

#### ✅ 正确的事务范围
```java
@Transactional(rollbackFor = Exception.class)
public void completeQuestAndReward(Long playerId, String questCfgId) {
    // 领取任务奖励
    dailyQuestService.claimReward(playerId, questCfgId);

    // 发放奖励
    rewardService.grantQuestReward(playerId, questCfgId);

    // 两个操作在同一事务中
}
```

#### ❌ 事务范围过大
```java
@Transactional(rollbackFor = Exception.class)
public void processPlayerData(Long playerId) {
    // 包含了大量查询操作 - 不应该用事务
    List<PetEntity> pets = petService.getPlayerPets(playerId);
    List<AchievementEntity> achievements = achievementService.getPlayerAchievements(playerId);
    // ... 更多查询
}
```

### 3. 参数校验

所有Service方法的参数都会被校验，调用方不需要重复校验：
```java
// ✅ Service已经校验
petService.setActive(playerId, petId);  // Service内部会校验参数

// ❌ 不需要重复校验
if (playerId != null && playerId > 0) {  // 不需要
    petService.setActive(playerId, petId);
}
```

### 4. 并发安全

使用乐观锁防止并发冲突：
```java
// Entity已经包含version字段
// MyBatis-Plus会自动处理乐观锁
pet.setIsActive(1);
petMapper.updateById(pet);  // 如果version不匹配会更新失败
```

---

## 🚀 最佳实践

### 1. Service调用链

#### 推荐的分层调用
```
Controller/Action
    ↓
Service（业务逻辑）
    ↓
Mapper（数据访问）
```

#### 示例
```java
// PlayerAction.java
@ActionMethod(PlayerCmd.claimQuestReward)
public ClaimRewardResp claimQuestReward(ClaimRewardReq req, FlowContext ctx) {
    Long playerId = ctx.getUserId();

    // 调用Service
    dailyQuestService.claimReward(playerId, req.getQuestCfgId());

    return buildSuccessResp();
}
```

### 2. 批量操作优化

#### ✅ 批量查询
```java
// 一次性查询所有战宠
List<PetEntity> pets = petService.getPlayerPets(playerId);

// 而不是循环查询
for (Long petId : petIds) {
    PetEntity pet = petService.getPetById(playerId, petId);  // 多次查询 - 低效
}
```

### 3. 缓存使用

对于频繁查询且变化不频繁的数据，建议使用缓存：
```java
// 可以在Service层添加缓存注解
@Cacheable(value = "playerPets", key = "#playerId")
public List<PetEntity> getPlayerPets(Long playerId) {
    return petMapper.selectByPlayerId(playerId);
}

@CacheEvict(value = "playerPets", key = "#playerId")
public void setActive(Long playerId, Long petId) {
    // 操作会清除缓存
}
```

### 4. 日志记录

所有关键操作都已记录日志：
```java
log.info("[战宠服务] 获得战宠 playerId={} petCfgId={} way={} petId={}",
        playerId, petCfgId, obtainWay, pet.getId());
```

日志级别使用：
- `log.error()` - 系统异常、不应该发生的错误
- `log.warn()` - 业务警告、需要关注的情况
- `log.info()` - 关键业务操作
- `log.debug()` - 调试信息、详细流程

---

## 🔌 扩展点说明

每个Service都预留了扩展点，可以通过以下方式扩展：

### 1. 配置管理器接入
```java
// 在Service中注入配置管理器
@Resource
private PetConfigManager petConfigManager;

// 读取配置
PetConfig config = petConfigManager.getConfig(petCfgId);
```

### 2. 奖励系统接入
```java
// 在Service中注入奖励服务
@Resource
private RewardService rewardService;

// 发放奖励
rewardService.grantQuestReward(playerId, questCfgId);
```

### 3. 回调系统接入
```java
// 实现回调方法
private void onAchievementCompleted(AchievementEntity achievement) {
    // 发送系统消息
    messageService.sendAchievementMessage(achievement.getPlayerId());

    // 触发其他业务
    eventBus.post(new AchievementCompletedEvent(achievement));
}
```

---

## 📊 性能优化建议

### 1. 索引优化
所有Entity对应的表都已创建合适的索引：
- 主键索引
- 玩家ID索引
- 常用查询条件索引（status, date等）

### 2. 批量操作
使用Mapper的批量方法：
```java
// 批量插入
petMapper.insertBatch(pets);

// 批量更新
petMapper.updateBatchById(pets);
```

### 3. 分页查询
对于大量数据，使用分页：
```java
Page<AchievementEntity> page = new Page<>(pageNum, pageSize);
achievementMapper.selectPage(page, wrapper);
```

---

## 📝 待完善功能

### 优先级P2（后续开发）

1. **配置管理系统**
   - PetConfigManager - 战宠配置管理
   - AchievementConfigManager - 成就配置管理
   - QuestConfigManager - 任务配置管理

2. **奖励系统**
   - RewardService - 统一的奖励发放服务
   - 支持多种奖励类型（货币、道具、战宠等）

3. **缓存系统**
   - 玩家数据缓存
   - 配置数据缓存
   - 排行榜缓存

4. **事件系统**
   - EventBus集成
   - 业务事件发布/订阅

---

**文档完成时间**: 2025-12-22
**维护者**: xiaoyao
**版本**: v1.0
