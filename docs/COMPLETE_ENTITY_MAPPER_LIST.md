# Entity类和Mapper接口完整清单

## 📋 概述

**日期**: 2025-12-22
**作者**: xiaoyao
**状态**: ✅ P0和P1优先级全部完成

---

## ✅ 已创建文件总览

### Entity类总计：10个

#### P0优先级（4个）
| 文件名 | 路径 | 对应表 | 继承BaseEntity | 说明 |
|--------|------|--------|---------------|------|
| PlayerStatsEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_player_stats | ✅ 是 | 玩家属性实体 |
| CurrencyLogEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_currency_log | ❌ 否 | 货币变动日志（日志表） |
| LoginLogEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_login_log | ❌ 否 | 登录日志（日志表） |
| RechargeOrderEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_recharge_order | ✅ 是 | 充值订单实体 |

#### P1优先级（6个）
| 文件名 | 路径 | 对应表 | 继承BaseEntity | 说明 |
|--------|------|--------|---------------|------|
| PetEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_pet | ✅ 是 | 战宠实体 |
| SectMemberEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_sect_member | ✅ 是 | 宗门成员实体 |
| DailyQuestEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_daily_quest | ✅ 是 | 每日任务实体 |
| AchievementEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_achievement | ✅ 是 | 成就记录实体 |
| MailTemplateEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_mail_template | ✅ 是 | 全服邮件模板实体 |
| NoticeEntity.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/ | t_notice | ✅ 是 | 公告实体 |

---

### Mapper接口总计：10个

#### P0优先级（4个）
| 文件名 | 路径 | 自定义查询方法 |
|--------|------|--------------|
| PlayerStatsMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | 基础CRUD |
| CurrencyLogMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectRecentLogs, sumIncome, sumExpense |
| LoginLogMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | updateLogout, selectLastLogin, selectRecentLogins, countDAU |
| RechargeOrderMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectByOrderNo, selectByPlayerId, sumTotalRecharge |

#### P1优先级（6个）
| 文件名 | 路径 | 自定义查询方法 |
|--------|------|--------------|
| PetMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectByPlayerId, selectActivePets, selectByPetCfgId, countByPlayerId |
| SectMemberMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectByPlayerId, selectBySectId, selectTopContributors, countBySectId, resetWeeklyContribution |
| DailyQuestMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectByPlayerIdAndDate, countCompletedByDate, countClaimedByDate, selectByQuestCfgId |
| AchievementMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectByPlayerId, selectByAchievementCfgId, selectUnclaimedAchievements, countCompletedByPlayerId, countClaimedByPlayerId |
| MailTemplateMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectPendingTemplates, selectActiveTemplates, selectByTimeRange, selectByTargetType |
| NoticeMapper.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/ | selectActiveNotices, selectActiveNoticesByPlatform, selectPopupNotices, selectByType, selectForceReadNotices |

---

### 枚举类（3个）
| 文件名 | 路径 | 说明 |
|--------|------|------|
| CurrencyType.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/enums/ | 货币类型（灵石、仙玉、绑定仙玉） |
| CurrencyChangeType.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/enums/ | 货币变动类型（增加、减少） |
| CurrencySourceType.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/enums/ | 货币来源类型（12种来源） |

---

### Service服务类（2个）
| 文件名 | 路径 | 核心方法 |
|--------|------|---------|
| CurrencyLogService.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/log/ | logAdd, logReduce, logChange, logGmChange |
| LoginLogService.java | xiaoyao-logic/src/main/java/com/xiaoyao/logic/log/ | logLogin, logLogout, logLogoutByPlayerId, getCurrentLoginLogId |

---

## 📊 P1 Entity详细说明

### 1. PetEntity - 战宠实体

**核心字段**:
- `petCfgId`: 战宠配置ID
- `petLevel`: 等级
- `petQuality`: 品质（1白-7金）
- `isActive`: 是否出战
- `bonusAtk/bonusDef/bonusHp`: 属性加成

**PetMapper主要功能**:
- 查询玩家所有战宠（按品质和等级排序）
- 查询出战的战宠
- 统计战宠数量

### 2. SectMemberEntity - 宗门成员实体

**核心字段**:
- `sectId`: 宗门配置ID
- `position`: 职位（0弟子-4掌门）
- `contribution`: 总贡献度
- `weeklyContribution`: 本周贡献度

**SectMemberMapper主要功能**:
- 查询玩家当前宗门
- 查询宗门成员列表
- 贡献度排行榜
- 重置周贡献（定时任务使用）

### 3. DailyQuestEntity - 每日任务实体

**核心字段**:
- `questDate`: 任务日期
- `questCfgId`: 任务配置ID
- `progress/target`: 进度/目标
- `status`: 状态（0进行中 1已完成 2已领取）

**DailyQuestMapper主要功能**:
- 按日期查询玩家任务
- 统计完成和领取数量
- 支持任务重置逻辑

### 4. AchievementEntity - 成就记录实体

**核心字段**:
- `achievementCfgId`: 成就配置ID
- `progress`: 当前进度
- `status`: 状态（0进行中 1已完成 2已领取）
- `completeTime/claimTime`: 完成/领取时间

**AchievementMapper主要功能**:
- 查询玩家所有成就
- 查询可领取成就（红点提示）
- 统计完成成就数（称号系统）

### 5. MailTemplateEntity - 全服邮件模板实体

**核心字段**:
- `title/content`: 邮件标题和内容
- `attachments`: 附件物品（JSON格式）
- `targetType`: 目标类型（1全服 2指定玩家 3条件筛选）
- `startTime/endTime`: 生效时间/结束时间
- `status`: 状态（0待发送 1已发送 2已撤回）

**MailTemplateMapper主要功能**:
- 查询待发送模板（定时任务）
- 查询当前有效模板
- 支持按目标类型筛选

### 6. NoticeEntity - 公告实体

**核心字段**:
- `noticeType`: 公告类型（1系统 2活动 3更新 4紧急）
- `priority`: 优先级（排序用）
- `isPopup/isForceRead`: 弹窗/强制阅读标记
- `platforms`: 目标平台（all/wechat/douyin/ios/android）
- `startTime/endTime`: 生效时间/结束时间

**NoticeMapper主要功能**:
- 查询当前有效公告（按优先级排序）
- 按平台筛选公告
- 查询弹窗公告
- 查询强制阅读公告

---

## 🎯 设计原则

### 1. 继承规则
- ✅ **业务表继承BaseEntity**: 需要更新、删除、版本控制
- ❌ **日志表不继承BaseEntity**: 只记录创建，不允许更新/删除

### 2. 字段命名规范
- 配置ID统一用 `xxx_cfg_id` 格式
- 时间戳统一用 `Long` 类型存储毫秒数
- 日期统一用 `LocalDate` 类型
- 日期时间统一用 `LocalDateTime` 类型
- 布尔类型用 `Integer` (0/1)

### 3. 索引设计
- 所有表都有 `is_deleted` 索引（逻辑删除）
- 外键字段（如 playerId, sectId）都有索引
- 常用查询条件（如 status, priority）都有索引
- 时间范围查询字段有索引

---

## 📚 使用示例

### 战宠系统集成示例

```java
@Service
public class PetService {

    @Resource
    private PetMapper petMapper;

    /**
     * 查询玩家所有战宠
     */
    public List<PetEntity> getPlayerPets(Long playerId) {
        return petMapper.selectByPlayerId(playerId);
    }

    /**
     * 设置出战战宠
     */
    public void setActivePet(Long playerId, Long petId) {
        // 1. 取消当前出战的战宠
        List<PetEntity> activePets = petMapper.selectActivePets(playerId);
        activePets.forEach(pet -> {
            pet.setIsActive(0);
            petMapper.updateById(pet);
        });

        // 2. 设置新的出战战宠
        PetEntity pet = petMapper.selectById(petId);
        pet.setIsActive(1);
        petMapper.updateById(pet);
    }
}
```

### 每日任务系统集成示例

```java
@Service
public class DailyQuestService {

    @Resource
    private DailyQuestMapper dailyQuestMapper;

    /**
     * 更新任务进度
     */
    public void updateQuestProgress(Long playerId, String questCfgId, int addProgress) {
        LocalDate today = LocalDate.now();

        // 查询今日任务记录
        DailyQuestEntity quest = dailyQuestMapper.selectByQuestCfgId(
            playerId, today, questCfgId
        );

        if (quest == null) {
            // 创建新任务记录
            quest = new DailyQuestEntity();
            quest.setPlayerId(playerId);
            quest.setQuestDate(today);
            quest.setQuestCfgId(questCfgId);
            quest.setProgress(0);
            quest.setTarget(10); // 从配置读取
            quest.setStatus(0);
            dailyQuestMapper.insert(quest);
        }

        // 更新进度
        quest.setProgress(quest.getProgress() + addProgress);

        // 检查是否完成
        if (quest.getProgress() >= quest.getTarget() && quest.getStatus() == 0) {
            quest.setStatus(1); // 已完成
        }

        dailyQuestMapper.updateById(quest);
    }
}
```

### 公告系统集成示例

```java
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 获取玩家应该看到的公告列表
     */
    public List<NoticeEntity> getPlayerNotices(String platform) {
        LocalDateTime now = LocalDateTime.now();

        // 查询当前平台的有效公告
        return noticeMapper.selectActiveNoticesByPlatform(now, platform);
    }

    /**
     * 获取登录时应该弹窗的公告
     */
    public List<NoticeEntity> getLoginPopupNotices() {
        LocalDateTime now = LocalDateTime.now();

        // 查询弹窗公告
        return noticeMapper.selectPopupNotices(now);
    }
}
```

---

## 🔄 数据库迁移

### 执行顺序

```bash
# 1. 先修改现有表（添加公共字段）
mysql -u root -p xiaoyao_game < docs/sql/migration_v1.0_alter_tables.sql

# 2. 再创建新表（P0+P1共10张表）
mysql -u root -p xiaoyao_game < docs/sql/migration_v1.0_new_tables.sql
```

---

## 📈 统计信息

### 代码统计
- Entity类: 10个
- Mapper接口: 10个
- 枚举类: 3个
- Service类: 2个
- 自定义SQL方法: 35+ 个

### 覆盖功能
✅ 玩家属性系统
✅ 货币日志系统
✅ 登录日志系统
✅ 充值订单系统
✅ 战宠系统
✅ 宗门系统
✅ 每日任务系统
✅ 成就系统
✅ 邮件模板系统
✅ 公告系统

---

## 🎯 下一步计划

### 待实现功能

1. **Service层补充**:
   - PetService（战宠管理）
   - SectService（宗门管理）
   - DailyQuestService（每日任务）
   - AchievementService（成就系统）
   - NoticeService（公告服务）

2. **业务集成**:
   - 在PlayerAction中集成日志服务
   - 在BattleAction中集成战宠系统
   - 在QuestAction中集成每日任务
   - 在MailAction中集成邮件模板

3. **定时任务**:
   - 每日任务重置（每天0点）
   - 宗门周贡献重置（每周一0点）
   - 邮件自动发送
   - DAU统计

4. **GM后台**:
   - 货币操作界面
   - 邮件发送界面
   - 公告管理界面

---

**完成时间**: 2025-12-22
**作者**: xiaoyao
**状态**: ✅ P0和P1优先级全部完成
