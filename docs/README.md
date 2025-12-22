# 逍遥游项目文档

## 📚 文档目录

本目录包含逍遥游项目的所有开发文档，按类型组织如下：

### 01-设计文档

设计阶段的分析和规划文档。

| 文档名称 | 说明 |
|---------|------|
| [数据库表对比分析.md](./01-设计文档/数据库表对比分析.md) | 规划文档与实际实现的数据库表对比分析，识别缺失的表和字段 |

### 02-开发总结

开发完成后的总结文档，记录实现细节和架构决策。

| 文档名称 | 说明 |
|---------|------|
| [数据库重构总结.md](./02-开发总结/数据库重构总结.md) | 数据库重构的完整说明，包括BaseEntity设计、公共字段标准化、迁移步骤 |
| [Entity和日志系统创建总结.md](./02-开发总结/Entity和日志系统创建总结.md) | P0优先级Entity类和日志服务的创建总结，包括设计原则和使用流程 |
| [Entity和Mapper完整清单.md](./02-开发总结/Entity和Mapper完整清单.md) | 所有P0和P1优先级Entity类、Mapper接口的完整清单和使用示例 |

### 03-使用指南

面向开发人员的使用指南，提供实际开发中的最佳实践。

| 文档名称 | 说明 |
|---------|------|
| [日志系统使用指南.md](./03-使用指南/日志系统使用指南.md) | 货币日志和登录日志的使用方法、注意事项和代码示例 |
| [Service业务层使用指南.md](./03-使用指南/Service业务层使用指南.md) | 所有Service业务层的设计原则、使用示例和最佳实践 |

### sql

数据库SQL脚本。

| 文件名称 | 说明 |
|---------|------|
| [init.sql](./sql/init.sql) | 数据库初始化脚本（原始版本） |
| [migration_v1.0_alter_tables.sql](./sql/migration_v1.0_alter_tables.sql) | v1.0迁移脚本 - 为现有表添加公共字段 |
| [migration_v1.0_new_tables.sql](./sql/migration_v1.0_new_tables.sql) | v1.0迁移脚本 - 创建10张新表（P0+P1） |

---

## 📖 阅读建议

### 新加入项目的开发者

建议按以下顺序阅读：

1. **先看总结** → `02-开发总结/数据库重构总结.md`
   - 了解项目的整体架构和设计原则

2. **再看清单** → `02-开发总结/Entity和Mapper完整清单.md`
   - 了解所有可用的Entity和Mapper

3. **最后看指南** → `03-使用指南/Service业务层使用指南.md`
   - 学习如何正确使用Service层

### 需要实现具体功能时

1. 查看 `02-开发总结/Entity和Mapper完整清单.md` 找到相关的Entity
2. 查看 `03-使用指南/Service业务层使用指南.md` 找到对应的Service使用方法
3. 参考文档中的代码示例进行开发

### 需要记录日志时

直接查看 `03-使用指南/日志系统使用指南.md`，里面有完整的使用示例。

---

## 🎯 快速导航

### 核心功能模块

| 模块 | Entity | Service | 文档位置 |
|------|--------|---------|---------|
| **战宠系统** | PetEntity | PetService | [完整清单](./02-开发总结/Entity和Mapper完整清单.md#1-petentity---战宠实体) / [使用指南](./03-使用指南/Service业务层使用指南.md#5-petservice---战宠服务) |
| **宗门系统** | SectMemberEntity | SectService | [完整清单](./02-开发总结/Entity和Mapper完整清单.md#2-sectmemberentity---宗门成员实体) / [使用指南](./03-使用指南/Service业务层使用指南.md#6-sectservice---宗门服务) |
| **每日任务** | DailyQuestEntity | DailyQuestService | [完整清单](./02-开发总结/Entity和Mapper完整清单.md#3-dailyquestentity---每日任务实体) / [使用指南](./03-使用指南/Service业务层使用指南.md#4-dailyquestservice---每日任务服务) |
| **成就系统** | AchievementEntity | AchievementService | [完整清单](./02-开发总结/Entity和Mapper完整清单.md#4-achievemententity---成就记录实体) / [使用指南](./03-使用指南/Service业务层使用指南.md#3-achievementservice---成就服务) |
| **邮件系统** | MailTemplateEntity | MailTemplateService | [完整清单](./02-开发总结/Entity和Mapper完整清单.md#5-mailtemplateentity---全服邮件模板实体) / [使用指南](./03-使用指南/Service业务层使用指南.md#2-mailtemplateservice---邮件模板服务) |
| **公告系统** | NoticeEntity | NoticeService | [完整清单](./02-开发总结/Entity和Mapper完整清单.md#6-noticeentity---公告实体) / [使用指南](./03-使用指南/Service业务层使用指南.md#1-noticeservice---公告服务) |
| **货币日志** | CurrencyLogEntity | CurrencyLogService | [日志总结](./02-开发总结/Entity和日志系统创建总结.md#currencylogservice---货币日志服务) / [使用指南](./03-使用指南/日志系统使用指南.md#-货币日志使用) |
| **登录日志** | LoginLogEntity | LoginLogService | [日志总结](./02-开发总结/Entity和日志系统创建总结.md#loginlogservice---登录日志服务) / [使用指南](./03-使用指南/日志系统使用指南.md#-登录日志使用) |

---

## 📝 文档维护

- **作者**: xiaoyao
- **创建日期**: 2025-12-22
- **最后更新**: 2025-12-22
- **文档版本**: v1.0

### 文档更新记录

| 日期 | 版本 | 更新内容 |
|------|------|---------|
| 2025-12-22 | v1.0 | 初始版本，包含所有核心模块文档 |

---

## 💡 贡献指南

如果你需要添加新的文档或更新现有文档，请遵循以下规范：

1. **文档命名**: 使用中文名称，清晰描述文档内容
2. **文档分类**: 根据文档类型放入对应目录
   - 设计阶段文档 → `01-设计文档/`
   - 开发总结文档 → `02-开发总结/`
   - 使用指南文档 → `03-使用指南/`
3. **文档格式**: 使用Markdown格式，包含清晰的标题和目录
4. **代码示例**: 提供完整的、可运行的代码示例
5. **更新README**: 在本文件中添加新文档的索引

---

**祝你编码愉快！** 🚀
