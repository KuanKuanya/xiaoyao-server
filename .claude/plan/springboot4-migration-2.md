# Spring Boot 4.0 升级与 MyBatis-Flex 迁移规划 (第三版)

## 已明确的决策

- **目标版本**: Spring Boot 4.0.x (当前: 3.4.1)
- **✅ ORM框架迁移**: MyBatis Plus 3.5.9 → MyBatis-Flex v1.11.5
- **✅ Spring Boot 4 Starter**: 使用 `mybatis-flex-spring-boot4-starter`
- **✅ 连接池策略**: Druid → HikariCP (Spring Boot 4.0 默认)
- **✅ 移除依赖**: Redisson 3.34.1 (项目中未实际使用)
- **参考资源**: MyBatis-Flex官网 https://mybatis-flex.com
- **Java版本**: JDK 25 (已符合要求)

## 整体规划概述

### 项目目标

将 xiaoyao-server 从 Spring Boot 3.4.1 + MyBatis Plus 升级到 Spring Boot 4.0 + MyBatis-Flex，确保：

1. 所有业务功能正常运行
2. 数据访问层API平滑迁移（相比Spring Data JDBC方案更简单）
3. 性能有所提升（MyBatis-Flex无第三方依赖，性能优于MyBatis-Plus）
4. 代码可维护性增强（类型安全的QueryWrapper、多表查询支持）
5. 充分利用MyBatis-Flex的企业级特性（逻辑删除、乐观锁、自动填充等）
6. 降低迁移复杂度（同为MyBatis生态，API相似度高）

### 技术栈

**当前技术栈:**
- Spring Boot 3.4.1
- MyBatis Plus 3.5.9 (+ jsqlparser)
- Druid 1.2.23
- MySQL 8.0.33
- Redisson 3.34.1 (未实际使用)
- ionet 25.2
- JDK 25

**目标技术栈:**
- Spring Boot 4.0.0+
- MyBatis-Flex v1.11.5 (2024-12-24发布，官方支持Spring Boot 4.0)
- HikariCP (Spring Boot 4.0 内置版本)
- MySQL 8.0.33+
- ionet 25.2
- JDK 25

### MyBatis-Flex 核心优势

**相比 MyBatis-Plus:**
1. **零第三方依赖**: 仅依赖MyBatis本身，更稳定
2. **多表查询支持**: 原生支持 LEFT JOIN、INNER JOIN、UNION
3. **复合主键支持**: 支持多主键和复合主键场景
4. **类型安全查询**: APT自动生成表定义类，支持IDE重构
5. **性能更优**: 不使用拦截器和SQL解析，执行效率更高
6. **企业级特性开源**: 数据脱敏、字段加密、审计日志等（MP需付费）

**相比 Spring Data JDBC:**
1. **迁移成本低**: API与MyBatis-Plus高度相似，学习曲线平缓
2. **SQL可控性强**: 支持@Select注解、XML Mapper、QueryWrapper三种方式
3. **灵活性高**: 可混用JPA式查询和原生SQL
4. **功能更全**: 原生支持逻辑删除、乐观锁、分页等
5. **开发效率高**: 无需手动编写大量SQL和Mapper

### 主要阶段

1. **依赖分析与准备阶段** - 学习MyBatis-Flex，对比API差异，设计迁移方案
2. **Spring Boot 4.0 升级阶段** - 升级核心框架，移除Redisson和Druid
3. **MyBatis-Flex 迁移阶段** - 替换MyBatis-Plus，调整注解和API
4. **测试与优化阶段** - 功能测试、性能调优、代码优化

---

## MyBatis-Plus vs MyBatis-Flex API映射表

### 一、依赖项对比

| MyBatis-Plus | MyBatis-Flex | 说明 |
|--------------|--------------|------|
| `mybatis-plus-spring-boot3-starter` | `mybatis-flex-spring-boot4-starter` | Spring Boot集成 |
| `mybatis-plus-jsqlparser` | **不需要** | Flex无需SQL解析器 |
| 单独配置Druid | 使用Spring Boot默认HikariCP | 连接池简化 |

### 二、注解对比

| MyBatis-Plus | MyBatis-Flex | 说明 |
|--------------|--------------|------|
| `@TableName("t_player")` | `@Table("t_player")` | 表名映射 |
| `@TableId(type = IdType.ASSIGN_ID)` | `@Id(keyType = KeyType.Generator, value = "snowFlakeId")` | 主键策略 |
| `@TableField("device_id")` | `@Column("device_id")` | 字段映射 |
| `@TableLogic` | `@Column(isLogicDelete = true)` | 逻辑删除 |
| `@Version` | `@Column(version = true)` | 乐观锁 |
| `@TableField(fill = FieldFill.INSERT)` | `@Column(onInsertValue = "now()")` | 插入时填充 |
| `@TableField(fill = FieldFill.INSERT_UPDATE)` | `@Column(onUpdateValue = "now()")` | 更新时填充 |

### 三、Mapper接口对比

| MyBatis-Plus | MyBatis-Flex | 说明 |
|--------------|--------------|------|
| `extends BaseMapper<T>` | `extends BaseMapper<T>` | **完全兼容** |
| `selectById(id)` | `selectOneById(id)` | 单个查询 |
| `selectBatchIds(ids)` | `selectListByIds(ids)` | 批量查询 |
| `selectList(wrapper)` | `selectListByQuery(queryWrapper)` | 条件查询 |
| `insert(entity)` | `insert(entity)` | **完全兼容** |
| `updateById(entity)` | `update(entity)` | 更新操作 |
| `deleteById(id)` | `deleteById(id)` | **完全兼容** |

### 四、QueryWrapper对比

| MyBatis-Plus | MyBatis-Flex | 说明 |
|--------------|--------------|------|
| `new LambdaQueryWrapper<>()` | `QueryWrapper.create()` | 创建查询 |
| `.eq("device_id", value)` | `.where(PLAYER.DEVICE_ID.eq(value))` | 等值条件（类型安全） |
| `.like("nickname", "%张%")` | `.where(PLAYER.NICKNAME.like("%张%"))` | 模糊查询 |
| `.ge("level", 10)` | `.where(PLAYER.LEVEL.ge(10))` | 大于等于 |
| `.orderByDesc("created_at")` | `.orderBy(PLAYER.CREATED_AT.desc())` | 排序 |
| **不支持** | `.leftJoin(PET).on(...)` | **支持多表关联** |
| `.select("id", "name")` | `.select(PLAYER.ID, PLAYER.NAME)` | 字段选择（类型安全） |

### 五、分页查询对比

| MyBatis-Plus | MyBatis-Flex | 说明 |
|--------------|--------------|------|
| `Page<T> page = new Page<>(1, 10)` | `Page<T> page = Page.of(1, 10)` | 创建分页对象 |
| `mapper.selectPage(page, wrapper)` | `mapper.paginate(page, queryWrapper)` | 执行分页查询 |
| `page.getRecords()` | `page.getRecords()` | **完全兼容** |
| `page.getTotal()` | `page.getTotalRow()` | 获取总数（方法名不同） |
| `PaginationInnerInterceptor` | **无需配置拦截器** | Flex自动处理 |

### 六、配置类对比

| MyBatis-Plus | MyBatis-Flex | 说明 |
|--------------|--------------|------|
| `MybatisPlusInterceptor` | **不需要** | Flex无拦截器架构 |
| `GlobalConfig.DbConfig` | `FlexGlobalConfig` | 全局配置 |
| `MetaObjectHandler` | `@Column(onInsertValue/onUpdateValue)` | 字段填充（注解配置） |
| 手动配置分页插件 | **自动启用分页** | 简化配置 |
| 手动配置逻辑删除 | **注解配置** | 简化配置 |

---

## 详细任务分解

### 阶段 1: 依赖分析与准备阶段

#### 任务 1.1: MyBatis-Flex 学习与最佳实践研究

- **目标**: 深入理解MyBatis-Flex的核心特性和最佳实践
- **输入**: MyBatis-Flex官方文档、示例代码
- **输出**: 学习笔记、API对照表、代码示例
- **涉及文件**: 无代码变更

- **具体工作**:
  1. 学习MyBatis-Flex核心概念:
     - APT自动生成表定义类（如`PLAYER`、`PET`等）
     - QueryWrapper的类型安全查询
     - 多表关联查询（leftJoin、innerJoin）
     - 分页插件自动启用机制
  2. 研究注解系统:
     - `@Table`、`@Id`、`@Column` 注解详解
     - 逻辑删除: `@Column(isLogicDelete = true)`
     - 乐观锁: `@Column(version = true)`
     - 自动填充: `onInsertValue`、`onUpdateValue`
  3. 学习BaseMapper接口:
     - CRUD方法映射（selectOne、selectList、insert等）
     - 分页方法: `paginate(page, queryWrapper)`
     - 批量操作: `insertBatch`、`updateBatch`
  4. 了解配置机制:
     - Spring Boot Starter自动配置
     - application.yml配置项
     - 无需手动配置拦截器和插件
  5. 研究高级特性:
     - Db + Row 模式（类似ActiveRecord）
     - SQL审计和实体监听器
     - 字段加密和数据脱敏
  6. 对比MyBatis-Plus API差异，制定迁移清单

- **预估工作量**: 8小时

#### 任务 1.2: 当前项目依赖梳理与兼容性分析

- **目标**: 识别所有需要升级的依赖及潜在兼容性问题
- **输入**: 当前pom.xml、现有代码库
- **输出**: 依赖清单、兼容性报告、升级计划
- **涉及文件**:
  - `pom.xml` (根目录)
  - `xiaoyao-logic/pom.xml`
  - `xiaoyao-external/pom.xml`
  - `xiaoyao-starter/pom.xml`

- **具体工作**:
  1. 分析当前16个MyBatis-Plus Mapper的使用模式:
     - 已知使用`@Select`注解（如PlayerMapper.selectByDeviceId）
     - 继承BaseMapper的CRUD方法
     - LambdaQueryWrapper的使用位置（需搜索Service层）
  2. 识别MyBatis-Plus特有功能的使用位置:
     - `@TableName`、`@TableId`、`@TableField` 注解 (已在BaseEntity和各Entity中使用)
     - `@TableLogic` 逻辑删除 (已在BaseEntity中使用)
     - `@Version` 乐观锁 (已在BaseEntity中使用)
     - `@TableField(fill = FieldFill.xxx)` 自动填充 (已在BaseEntity中使用)
     - `MetaObjectHandler` 实现 (已有MetaObjectHandlerConfig)
     - `PaginationInnerInterceptor` 分页插件 (已在MybatisPlusConfig中配置)
  3. 检查ionet 25.2与Spring Boot 4.0的兼容性
  4. 验证MySQL Connector 8.0.33与Spring Boot 4.0的兼容性
  5. **✅ 确认Redisson未实际使用** (仅在pom.xml声明，无代码引用)
  6. 搜索Druid相关配置，准备迁移到HikariCP

- **预估工作量**: 4小时

#### 任务 1.3: 数据访问层改造方案设计

- **目标**: 设计完整的MyBatis-Plus到MyBatis-Flex的迁移方案
- **输入**: 任务1.1、1.2的输出
- **输出**: 迁移方案文档、实体类改造规范、Mapper改造规范、代码示例
- **涉及文件**:
  - 新增: `.claude/docs/mybatis-flex-migration-guide.md`

- **具体工作**:
  1. **实体类改造规范**:
     ```java
     // MyBatis-Plus (旧)
     @TableName("t_player")
     public class PlayerEntity extends BaseEntity {
         @TableId(type = IdType.ASSIGN_ID)
         private Long id;

         @TableField("device_id")
         private String deviceId;
     }

     // MyBatis-Flex (新)
     @Table("t_player")
     public class PlayerEntity extends BaseEntity {
         @Id(keyType = KeyType.Generator, value = "snowFlakeId")
         private Long id;

         @Column("device_id")
         private String deviceId;
     }
     ```

  2. **BaseEntity改造规范**:
     ```java
     // MyBatis-Plus (旧)
     public abstract class BaseEntity {
         @Version
         @TableField(value = "version")
         private Integer version;

         @TableLogic
         @TableField(value = "is_deleted")
         private Integer isDeleted;

         @TableField(value = "created_at", fill = FieldFill.INSERT)
         private LocalDateTime createdAt;

         @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
         private LocalDateTime updatedAt;
     }

     // MyBatis-Flex (新)
     public abstract class BaseEntity {
         @Column(value = "version", version = true)
         private Integer version;

         @Column(value = "is_deleted", isLogicDelete = true)
         private Integer isDeleted;

         @Column(value = "created_at", onInsertValue = "now()")
         private LocalDateTime createdAt;

         @Column(value = "updated_at", onInsertValue = "now()", onUpdateValue = "now()")
         private LocalDateTime updatedAt;

         // created_by、updated_by 需要在Service层手动设置
         @Column("created_by")
         private Long createdBy;

         @Column("updated_by")
         private Long updatedBy;
     }
     ```

  3. **Mapper接口改造规范**:
     ```java
     // MyBatis-Plus (旧)
     @Mapper
     public interface PlayerMapper extends BaseMapper<PlayerEntity> {
         @Select("SELECT * FROM t_player WHERE device_id = #{deviceId} AND is_deleted = 0 LIMIT 1")
         PlayerEntity selectByDeviceId(@Param("deviceId") String deviceId);
     }

     // MyBatis-Flex (新) - 方式1: 保留@Select注解
     @Mapper
     public interface PlayerMapper extends BaseMapper<PlayerEntity> {
         @Select("SELECT * FROM t_player WHERE device_id = #{deviceId} AND is_deleted = 0 LIMIT 1")
         PlayerEntity selectByDeviceId(@Param("deviceId") String deviceId);
     }

     // MyBatis-Flex (新) - 方式2: 使用QueryWrapper (推荐，类型安全)
     @Mapper
     public interface PlayerMapper extends BaseMapper<PlayerEntity> {
         // 在Service中实现:
         // QueryWrapper query = QueryWrapper.create()
         //     .where(PLAYER.DEVICE_ID.eq(deviceId))
         //     .limit(1);
         // PlayerEntity player = playerMapper.selectOneByQuery(query);
     }
     ```

  4. **Service层改造规范**:
     ```java
     // MyBatis-Plus (旧)
     @Service
     public class PlayerService {
         @Autowired
         private PlayerMapper mapper;

         public PlayerEntity getByDeviceId(String deviceId) {
             return mapper.selectByDeviceId(deviceId);
         }

         public List<PlayerEntity> getByLevel(int minLevel) {
             LambdaQueryWrapper<PlayerEntity> wrapper = new LambdaQueryWrapper<>();
             wrapper.ge(PlayerEntity::getLevel, minLevel)
                    .orderByDesc(PlayerEntity::getCreatedAt);
             return mapper.selectList(wrapper);
         }
     }

     // MyBatis-Flex (新)
     @Service
     public class PlayerService {
         @Autowired
         private PlayerMapper mapper;

         public PlayerEntity getByDeviceId(String deviceId) {
             // 方式1: 保留原有@Select方法
             return mapper.selectByDeviceId(deviceId);

             // 方式2: 使用QueryWrapper (推荐)
             QueryWrapper query = QueryWrapper.create()
                 .where(PLAYER.DEVICE_ID.eq(deviceId))
                 .limit(1);
             return mapper.selectOneByQuery(query);
         }

         public List<PlayerEntity> getByLevel(int minLevel) {
             QueryWrapper query = QueryWrapper.create()
                 .where(PLAYER.LEVEL.ge(minLevel))
                 .orderBy(PLAYER.CREATED_AT.desc());
             return mapper.selectListByQuery(query);
         }
     }
     ```

  5. **分页查询改造规范**:
     ```java
     // MyBatis-Plus (旧)
     public Page<PlayerEntity> pageQuery(int pageNum, int pageSize) {
         Page<PlayerEntity> page = new Page<>(pageNum, pageSize);
         LambdaQueryWrapper<PlayerEntity> wrapper = new LambdaQueryWrapper<>();
         wrapper.orderByDesc(PlayerEntity::getCreatedAt);
         return mapper.selectPage(page, wrapper);
     }

     // MyBatis-Flex (新)
     public Page<PlayerEntity> pageQuery(int pageNum, int pageSize) {
         Page<PlayerEntity> page = Page.of(pageNum, pageSize);
         QueryWrapper query = QueryWrapper.create()
             .orderBy(PLAYER.CREATED_AT.desc());
         return mapper.paginate(page, query);
         // 获取结果: page.getRecords(), page.getTotalRow()
     }
     ```

  6. 制定迁移优先级:
     - **第一批**: 核心模块 (Player, Pet, BagItem) - 3个Mapper
     - **第二批**: 业务模块 (Achievement, DailyQuest, Equipment, PlayerSkill, PlayerStats) - 5个Mapper
     - **第三批**: 日志与系统模块 (LoginLog, CurrencyLog, Mail等) - 8个Mapper

  7. 编写APT注解处理器配置:
     ```xml
     <!-- pom.xml添加APT插件 -->
     <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-compiler-plugin</artifactId>
         <configuration>
             <annotationProcessorPaths>
                 <path>
                     <groupId>com.mybatis-flex</groupId>
                     <artifactId>mybatis-flex-processor</artifactId>
                     <version>1.11.5</version>
                 </path>
             </annotationProcessorPaths>
         </configuration>
     </plugin>
     ```

- **预估工作量**: 6小时

---

### 阶段 2: Spring Boot 4.0 升级阶段

#### 任务 2.1: 升级Spring Boot版本到4.0.0

- **目标**: 升级Spring Boot核心框架，解决基本兼容性问题
- **输入**: 阶段1的分析结果
- **输出**: 升级后的pom.xml、兼容性修复代码
- **涉及文件**:
  - `pom.xml` (根目录) - 修改spring-boot.version
  - 所有子模块的配置类

- **具体工作**:
  1. 修改根pom.xml:
     ```xml
     <spring-boot.version>4.0.0</spring-boot.version>
     ```
  2. 暂时保留MyBatis-Plus依赖（后续替换为MyBatis-Flex）
  3. 升级MySQL Connector（如需要）:
     ```xml
     <mysql.version>8.0.33</mysql.version> <!-- 保持或升级 -->
     ```
  4. 处理Spring Boot 4的Breaking Changes:
     - 配置属性路径变更（如有）
     - 废弃API替换
     - 自动配置调整
  5. 执行`mvn clean compile`验证编译通过
  6. 启动应用验证基本功能

- **预估工作量**: 4小时

#### 任务 2.2: 移除Redisson依赖和Druid配置

- **目标**: 清理未使用的Redisson依赖，准备切换到HikariCP
- **输入**: 依赖分析结果
- **输出**: 清理后的pom.xml、简化的数据源配置
- **涉及文件**:
  - `pom.xml` (根目录)
  - `xiaoyao-logic/pom.xml`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/config/MybatisPlusConfig.java`
  - `xiaoyao-logic/src/main/resources/application.yml`

- **具体工作**:
  1. **移除Redisson依赖**:
     - 从根pom.xml的`<properties>`中移除:
       ```xml
       <redisson.version>3.34.1</redisson.version>
       ```
     - 从`<dependencyManagement>`中移除Redisson依赖
     - 从`xiaoyao-logic/pom.xml`中移除Redisson依赖

  2. **移除Druid配置，切换到HikariCP**:
     - 删除MybatisPlusConfig中的DruidDataSource配置:
       ```java
       // 删除整个dataSource() Bean方法
       @Bean
       public DataSource dataSource() { ... } // 删除
       ```
     - 使用Spring Boot 4默认的HikariCP自动配置
     - 在`application.yml`中配置HikariCP:
       ```yaml
       spring:
         datasource:
           url: jdbc:mysql://127.0.0.1:3306/xiaoyao_game?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
           username: root
           password: 123456
           driver-class-name: com.mysql.cj.jdbc.Driver
           hikari:
             maximum-pool-size: 20
             minimum-idle: 5
             connection-timeout: 30000
             idle-timeout: 600000
             max-lifetime: 1800000
             connection-test-query: SELECT 1
       ```

  3. 从pom.xml移除Druid依赖:
     ```xml
     <!-- 移除 -->
     <druid.version>1.2.23</druid.version>
     <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
     </dependency>
     ```

  4. 执行`mvn clean compile`验证无编译错误
  5. 更新README.md，移除Redis和Druid相关说明

- **预估工作量**: 2小时

#### 任务 2.3: 验证ionet框架兼容性

- **目标**: 确保ionet 25.2与Spring Boot 4.0协同工作
- **输入**: 升级后的项目
- **输出**: 兼容性测试报告、必要的适配代码
- **涉及文件**:
  - `xiaoyao-starter/src/main/java/com/xiaoyao/starter/**`

- **具体工作**:
  1. 启动应用验证ionet组件初始化
  2. 测试Spring容器集成 (extension-spring)
  3. 验证领域事件 (extension-domain-event)
  4. 测试网络通信模块
  5. 检查日志输出是否正常

- **预估工作量**: 3小时

#### 任务 2.4: 修复编译与运行时错误

- **目标**: 解决Spring Boot 4升级后的所有错误
- **输入**: 编译错误日志、运行时异常
- **输出**: 修复后的稳定版本
- **涉及文件**:
  - 根据错误日志确定 (可能涉及所有配置类)

- **具体工作**:
  1. 修复废弃API调用
  2. 调整配置属性路径
  3. 处理Spring Security变更 (如果有)
  4. 更新测试用例
  5. 确保应用可以正常启动

- **预估工作量**: 4小时

---

### 阶段 3: MyBatis-Flex 迁移阶段

#### 任务 3.1: 引入MyBatis-Flex依赖并移除MyBatis-Plus

- **目标**: 替换依赖项，配置MyBatis-Flex环境
- **输入**: Spring Boot 4.0稳定版本
- **输出**: 配置完成的MyBatis-Flex环境
- **涉及文件**:
  - `pom.xml` (根目录)
  - `xiaoyao-logic/pom.xml`
  - `xiaoyao-logic/src/main/resources/application.yml`

- **具体工作**:
  1. 在根pom.xml的`<properties>`中添加:
     ```xml
     <mybatis-flex.version>1.11.5</mybatis-flex.version>
     ```

  2. 在`<dependencyManagement>`中替换依赖:
     ```xml
     <!-- 移除 MyBatis-Plus -->
     <!--
     <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
       <version>${mybatis-plus.version}</version>
     </dependency>
     <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-jsqlparser</artifactId>
       <version>${mybatis-plus.version}</version>
     </dependency>
     -->

     <!-- 新增 MyBatis-Flex (Spring Boot 4专用) -->
     <dependency>
       <groupId>com.mybatis-flex</groupId>
       <artifactId>mybatis-flex-spring-boot4-starter</artifactId>
       <version>${mybatis-flex.version}</version>
     </dependency>
     ```

  3. 在`xiaoyao-logic/pom.xml`中替换依赖:
     ```xml
     <!-- 移除 -->
     <!--
     <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
     </dependency>
     <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-jsqlparser</artifactId>
     </dependency>
     -->

     <!-- 新增 -->
     <dependency>
       <groupId>com.mybatis-flex</groupId>
       <artifactId>mybatis-flex-spring-boot4-starter</artifactId>
     </dependency>
     ```

  4. 配置APT注解处理器（根pom.xml的maven-compiler-plugin）:
     ```xml
     <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-compiler-plugin</artifactId>
       <configuration>
         <annotationProcessorPaths>
           <path>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
             <version>${lombok.version}</version>
           </path>
           <!-- 新增MyBatis-Flex APT处理器 -->
           <path>
             <groupId>com.mybatis-flex</groupId>
             <artifactId>mybatis-flex-processor</artifactId>
             <version>${mybatis-flex.version}</version>
           </path>
         </annotationProcessorPaths>
       </configuration>
     </plugin>
     ```

  5. 在`application.yml`中配置MyBatis-Flex:
     ```yaml
     mybatis-flex:
       # 开启SQL日志（开发环境）
       configuration:
         log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
       # 全局配置
       global-config:
         # 逻辑删除配置
         logic-delete-column: is_deleted
         logic-delete-value: 1
         logic-not-delete-value: 0
         # 自动配置
         banner: true
     ```

  6. 执行`mvn clean compile`，验证APT生成表定义类:
     - 检查`target/generated-sources/annotations`目录
     - 应生成`PLAYER.java`、`PET.java`等表定义类

- **预估工作量**: 3小时

#### 任务 3.2: BaseEntity改造

- **目标**: 将BaseEntity从MyBatis-Plus注解迁移到MyBatis-Flex注解
- **输入**: 任务1.3的设计方案
- **输出**: 符合MyBatis-Flex规范的BaseEntity
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/BaseEntity.java`

- **具体工作**:
  1. 替换import语句:
     ```java
     // 删除MyBatis-Plus导入
     // import com.baomidou.mybatisplus.annotation.*;

     // 新增MyBatis-Flex导入
     import com.mybatisflex.annotation.Column;
     ```

  2. 修改注解:
     ```java
     @Data
     public abstract class BaseEntity {

         /**
          * 乐观锁版本号
          */
         @Column(value = "version", version = true)
         private Integer version;

         /**
          * 逻辑删除标志
          */
         @Column(value = "is_deleted", isLogicDelete = true)
         private Integer isDeleted;

         /**
          * 创建人ID
          */
         @Column(value = "created_by")
         private Long createdBy;

         /**
          * 创建时间
          */
         @Column(value = "created_at", onInsertValue = "now()")
         private LocalDateTime createdAt;

         /**
          * 更新人ID
          */
         @Column(value = "updated_by")
         private Long updatedBy;

         /**
          * 更新时间
          */
         @Column(value = "updated_at", onInsertValue = "now()", onUpdateValue = "now()")
         private LocalDateTime updatedAt;

         /**
          * 删除人ID
          */
         @Column(value = "deleted_by")
         private Long deletedBy;

         /**
          * 删除时间
          */
         @Column(value = "deleted_at")
         private LocalDateTime deletedAt;
     }
     ```

  3. 注意事项:
     - `createdBy`和`updatedBy`需要在Service层手动设置（因为需要从FlowContext获取玩家ID）
     - `onInsertValue = "now()"`会在插入时自动填充数据库时间
     - `onUpdateValue = "now()"`会在更新时自动填充数据库时间

- **预估工作量**: 1小时

#### 任务 3.3: 实体类改造 - 第一批 (核心模块)

- **目标**: 将核心实体类从MyBatis-Plus注解迁移到MyBatis-Flex注解
- **输入**: BaseEntity改造完成
- **输出**: 符合MyBatis-Flex规范的实体类
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/PlayerEntity.java`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/PetEntity.java`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/BagItemEntity.java`

- **具体工作**:
  1. **PlayerEntity改造示例**:
     ```java
     // 替换import
     // import com.baomidou.mybatisplus.annotation.*;
     import com.mybatisflex.annotation.Id;
     import com.mybatisflex.annotation.Table;
     import com.mybatisflex.annotation.Column;
     import com.mybatisflex.annotation.KeyType;

     @Data
     @EqualsAndHashCode(callSuper = true)
     @Table("t_player")  // 替换 @TableName
     public class PlayerEntity extends BaseEntity {

         /** 玩家ID (雪花算法) */
         @Id(keyType = KeyType.Generator, value = "snowFlakeId")  // 替换 @TableId
         private Long id;

         /** 设备ID */
         @Column("device_id")  // 如果字段名与数据库列名一致，可省略
         private String deviceId;

         /** 平台类型 */
         private Integer platform;

         /** 昵称 */
         private String nickname;

         /** 头像URL */
         private String avatar;

         /** VIP等级 */
         @Column("vip_level")
         private Integer vipLevel;

         /** 当前佩戴的称号ID */
         @Column("current_title_id")
         private Integer currentTitleId;

         /** 上次登录时间 */
         @Column("last_login_time")
         private LocalDateTime lastLoginTime;
     }
     ```

  2. 同样改造PetEntity和BagItemEntity

  3. 执行`mvn clean compile`，验证APT生成:
     - `PLAYER.java` (表定义类)
     - `PET.java`
     - `BAG_ITEM.java`

- **预估工作量**: 2小时

#### 任务 3.4: 实体类改造 - 第二批和第三批 (其他模块)

- **目标**: 完成所有实体类的注解迁移
- **输入**: 第一批实体类改造经验
- **输出**: 完整的MyBatis-Flex实体类
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/entity/*.java` (剩余13个实体类)

- **具体工作**:
  1. 批量替换注解:
     - `@TableName` → `@Table`
     - `@TableId(type = IdType.ASSIGN_ID)` → `@Id(keyType = KeyType.Generator, value = "snowFlakeId")`
     - `@TableField` → `@Column` (或省略，如果字段名一致)
  2. 验证APT生成所有表定义类
  3. 处理特殊字段（如JSON字段、枚举类型等）

- **预估工作量**: 3小时

#### 任务 3.5: 配置类改造

- **目标**: 替换MyBatis-Plus配置为MyBatis-Flex配置
- **输入**: MyBatis-Flex依赖已引入
- **输出**: 简化的配置类
- **涉及文件**:
  - 删除: `xiaoyao-logic/src/main/java/com/xiaoyao/logic/config/MybatisPlusConfig.java`
  - 删除: `xiaoyao-logic/src/main/java/com/xiaoyao/logic/config/MetaObjectHandlerConfig.java`
  - 新增: `xiaoyao-logic/src/main/java/com/xiaoyao/logic/config/MybatisFlexConfig.java` (可选)

- **具体工作**:
  1. **删除MybatisPlusConfig.java** (整个文件):
     - Spring Boot 4 + MyBatis-Flex自动配置更强大
     - 数据源使用HikariCP自动配置
     - 分页插件自动启用，无需手动配置

  2. **删除MetaObjectHandlerConfig.java**:
     - MyBatis-Flex使用注解配置字段填充
     - `onInsertValue`、`onUpdateValue`已在BaseEntity中配置

  3. **可选: 创建MybatisFlexConfig.java** (仅用于特殊配置):
     ```java
     package com.xiaoyao.logic.config;

     import com.mybatisflex.core.FlexGlobalConfig;
     import com.mybatisflex.core.audit.AuditManager;
     import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
     import lombok.extern.slf4j.Slf4j;
     import org.springframework.context.annotation.Configuration;

     /**
      * MyBatis-Flex 配置（可选）
      * Spring Boot自动配置已足够，此类仅用于自定义配置
      */
     @Slf4j
     @Configuration
     public class MybatisFlexConfig implements MyBatisFlexCustomizer {

         @Override
         public void customize(FlexGlobalConfig globalConfig) {
             // 开启SQL审计（可选）
             AuditManager.setAuditEnable(true);

             // 设置SQL监听器（可选，用于慢SQL日志）
             AuditManager.setMessageCollector(auditMessage -> {
                 if (auditMessage.getElapsedTime() > 1000) {
                     log.warn("[慢SQL] {}ms - {}",
                         auditMessage.getElapsedTime(),
                         auditMessage.getFullSql());
                 }
             });

             log.info("[MyBatis-Flex] 配置完成");
         }
     }
     ```

  4. 更新`@MapperScan`注解位置:
     - 删除MybatisPlusConfig中的`@MapperScan`
     - 在Spring Boot主类中添加:
       ```java
       @SpringBootApplication
       @MapperScan("com.xiaoyao.logic.mapper")
       public class XiaoyaoApplication {
           public static void main(String[] args) {
               SpringApplication.run(XiaoyaoApplication.class, args);
           }
       }
       ```

- **预估工作量**: 2小时

#### 任务 3.6: Mapper接口改造 - 第一批 (核心模块)

- **目标**: 将核心Mapper接口从MyBatis-Plus迁移到MyBatis-Flex
- **输入**: 实体类改造完成、APT生成表定义类
- **输出**: 符合MyBatis-Flex规范的Mapper接口
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/PlayerMapper.java`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/PetMapper.java`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/BagItemMapper.java`

- **具体工作**:
  1. **PlayerMapper改造示例**:
     ```java
     package com.xiaoyao.logic.mapper;

     import com.mybatisflex.core.BaseMapper;  // 替换MyBatis-Plus的BaseMapper
     import com.xiaoyao.logic.entity.PlayerEntity;
     import org.apache.ibatis.annotations.Mapper;
     import org.apache.ibatis.annotations.Param;
     import org.apache.ibatis.annotations.Select;

     /**
      * 玩家 Mapper
      */
     @Mapper
     public interface PlayerMapper extends BaseMapper<PlayerEntity> {

         /**
          * 根据设备ID查询玩家
          * 注意: MyBatis-Flex的BaseMapper已自动处理逻辑删除，
          * 但@Select注解的方法需要手动添加is_deleted条件
          */
         @Select("SELECT * FROM t_player WHERE device_id = #{deviceId} AND is_deleted = 0 LIMIT 1")
         PlayerEntity selectByDeviceId(@Param("deviceId") String deviceId);

         // 也可以在Service中使用QueryWrapper实现:
         // QueryWrapper query = QueryWrapper.create()
         //     .where(PLAYER.DEVICE_ID.eq(deviceId))
         //     .limit(1);
         // return playerMapper.selectOneByQuery(query);
     }
     ```

  2. 注意事项:
     - `extends BaseMapper<PlayerEntity>` 保持不变，MyBatis-Flex完全兼容
     - `@Select`注解的方法需要手动添加`is_deleted = 0`条件
     - 推荐使用QueryWrapper替代@Select，获得类型安全和自动逻辑删除支持

  3. 同样改造PetMapper和BagItemMapper

- **预估工作量**: 2小时

#### 任务 3.7: Mapper接口改造 - 第二批和第三批 (其他模块)

- **目标**: 完成所有Mapper接口的迁移
- **输入**: 第一批Mapper改造经验
- **输出**: 完整的MyBatis-Flex Mapper层
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/mapper/*.java` (剩余13个Mapper)

- **具体工作**:
  1. 批量替换import语句:
     ```java
     // 旧
     import com.baomidou.mybatisplus.core.mapper.BaseMapper;

     // 新
     import com.mybatisflex.core.BaseMapper;
     ```
  2. 检查所有@Select注解的SQL，确保包含逻辑删除条件
  3. 验证编译通过

- **预估工作量**: 2小时

#### 任务 3.8: Service层代码适配 - 第一批 (核心模块)

- **目标**: 修改Service层代码，使用MyBatis-Flex API
- **输入**: Mapper改造完成
- **输出**: 适配后的Service层
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/service/PlayerService.java`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/service/PetService.java`
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/service/BagItemService.java`

- **具体工作**:
  1. **搜索LambdaQueryWrapper的使用位置**:
     ```bash
     grep -r "LambdaQueryWrapper" xiaoyao-logic/src/main/java/
     ```

  2. **PlayerService改造示例**:
     ```java
     package com.xiaoyao.logic.service;

     import com.mybatisflex.core.query.QueryWrapper;
     import com.mybatisflex.core.paginate.Page;
     import com.xiaoyao.logic.entity.PlayerEntity;
     import com.xiaoyao.logic.mapper.PlayerMapper;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.stereotype.Service;

     // 导入APT生成的表定义类
     import static com.xiaoyao.logic.entity.table.PlayerEntityTableDef.PLAYER;

     @Service
     public class PlayerService {

         @Autowired
         private PlayerMapper mapper;

         /**
          * 根据设备ID查询玩家
          */
         public PlayerEntity getByDeviceId(String deviceId) {
             // 方式1: 使用Mapper的@Select方法
             return mapper.selectByDeviceId(deviceId);

             // 方式2: 使用QueryWrapper (推荐，类型安全，自动逻辑删除)
             // QueryWrapper query = QueryWrapper.create()
             //     .where(PLAYER.DEVICE_ID.eq(deviceId))
             //     .limit(1);
             // return mapper.selectOneByQuery(query);
         }

         /**
          * 根据VIP等级查询玩家列表
          * MyBatis-Plus: wrapper.ge(PlayerEntity::getVipLevel, minLevel)
          * MyBatis-Flex: PLAYER.VIP_LEVEL.ge(minLevel)
          */
         public List<PlayerEntity> getByVipLevel(int minLevel) {
             QueryWrapper query = QueryWrapper.create()
                 .where(PLAYER.VIP_LEVEL.ge(minLevel))
                 .orderBy(PLAYER.CREATED_AT.desc());
             return mapper.selectListByQuery(query);
         }

         /**
          * 分页查询玩家
          * MyBatis-Plus: Page<T> page = new Page<>(pageNum, pageSize)
          * MyBatis-Flex: Page<T> page = Page.of(pageNum, pageSize)
          */
         public Page<PlayerEntity> pageQuery(int pageNum, int pageSize, String nickname) {
             Page<PlayerEntity> page = Page.of(pageNum, pageSize);
             QueryWrapper query = QueryWrapper.create()
                 .where(PLAYER.NICKNAME.like(nickname))  // 自动添加%符号
                 .orderBy(PLAYER.LAST_LOGIN_TIME.desc());
             return mapper.paginate(page, query);
             // 获取结果: page.getRecords(), page.getTotalRow()
         }

         /**
          * 插入玩家
          * 注意: createdBy和updatedBy需要手动设置
          */
         public void createPlayer(PlayerEntity player, Long operatorId) {
             player.setCreatedBy(operatorId);
             player.setUpdatedBy(operatorId);
             // createdAt和updatedAt会自动填充
             mapper.insert(player);
         }

         /**
          * 更新玩家
          */
         public boolean updatePlayer(PlayerEntity player, Long operatorId) {
             player.setUpdatedBy(operatorId);
             // updatedAt会自动填充
             // version乐观锁自动处理
             return mapper.update(player) > 0;
         }

         /**
          * 删除玩家（逻辑删除）
          * MyBatis-Plus: mapper.deleteById(id)
          * MyBatis-Flex: mapper.deleteById(id) (自动逻辑删除)
          */
         public boolean deletePlayer(Long id) {
             return mapper.deleteById(id) > 0;
         }
     }
     ```

  3. **API映射速查**:
     - `mapper.selectById(id)` → `mapper.selectOneById(id)`
     - `mapper.selectList(wrapper)` → `mapper.selectListByQuery(queryWrapper)`
     - `mapper.selectPage(page, wrapper)` → `mapper.paginate(page, queryWrapper)`
     - `mapper.insert(entity)` → `mapper.insert(entity)` (兼容)
     - `mapper.updateById(entity)` → `mapper.update(entity)`
     - `mapper.deleteById(id)` → `mapper.deleteById(id)` (兼容)
     - `new Page<>(pageNum, pageSize)` → `Page.of(pageNum, pageSize)`
     - `page.getTotal()` → `page.getTotalRow()`

  4. 处理`createdBy`和`updatedBy`的填充:
     - 在insert和update方法中手动设置
     - 从`FlowContext`或`ThreadLocal`获取当前玩家ID

  5. 同样改造PetService和BagItemService

- **预估工作量**: 6小时

#### 任务 3.9: Service层代码适配 - 第二批和第三批 (其他模块)

- **目标**: 完成所有Service层的API迁移
- **输入**: 第一批Service改造经验
- **输出**: 完整的Service层
- **涉及文件**:
  - `xiaoyao-logic/src/main/java/com/xiaoyao/logic/service/*.java` (剩余Service类)

- **具体工作**:
  1. 批量替换LambdaQueryWrapper为QueryWrapper
  2. 调整分页查询代码
  3. 处理复杂查询（如关联查询、子查询等）
  4. 添加操作人ID填充逻辑
  5. 编写单元测试验证功能

- **预估工作量**: 8小时

---

### 阶段 4: 测试与优化阶段

#### 任务 4.1: 单元测试编写与修复

- **目标**: 为Mapper和Service层编写单元测试，确保功能正确
- **输入**: 新的Mapper和Service层代码
- **输出**: 完整的单元测试套件
- **涉及文件**:
  - 新增: `xiaoyao-logic/src/test/java/com/xiaoyao/logic/mapper/**/*Test.java`
  - 新增: `xiaoyao-logic/src/test/java/com/xiaoyao/logic/service/**/*Test.java`

- **具体工作**:
  1. 配置测试数据源 (H2内存数据库或TestContainers MySQL)
  2. 为每个Mapper编写CRUD测试:
     - 测试基本CRUD方法
     - 测试QueryWrapper条件查询
     - 测试分页查询
     - 测试逻辑删除（验证is_deleted字段）
     - 测试乐观锁（验证version字段）
  3. 为Service层编写集成测试
  4. 验证自动填充字段（createdAt、updatedAt）
  5. 测试多表关联查询（如果有）

- **预估工作量**: 10小时

#### 任务 4.2: 集成测试与端到端测试

- **目标**: 验证整体业务流程正常运行
- **输入**: 完整的应用
- **输出**: 集成测试报告
- **涉及文件**:
  - 新增: `xiaoyao-logic/src/test/java/com/xiaoyao/logic/integration/**/*IT.java`

- **具体工作**:
  1. 启动完整应用 (包括ionet网络层)
  2. 测试玩家登录注册流程
  3. 测试背包物品增删改查
  4. 测试宠物系统
  5. 测试成就系统
  6. 测试邮件系统
  7. 验证数据持久化正确性

- **预估工作量**: 8小时

#### 任务 4.3: 性能测试与优化

- **目标**: 确保迁移后性能不降反升
- **输入**: 运行中的应用
- **输出**: 性能测试报告、优化建议
- **涉及文件**:
  - 可能优化: Service层代码 (批量操作)
  - 可能优化: Mapper接口 (SQL优化)

- **具体工作**:
  1. 对比MyBatis-Plus与MyBatis-Flex的查询性能
  2. 分析慢SQL (使用MyBatis-Flex的SQL审计功能)
  3. 优化N+1查询问题（利用多表关联查询）
  4. 优化批量插入/更新 (使用`insertBatch`、`updateBatch`)
  5. 评估HikariCP连接池性能
  6. 调优HikariCP参数

- **预估工作量**: 6小时

#### 任务 4.4: 代码Review与重构

- **目标**: 提升代码质量和可维护性
- **输入**: 迁移后的代码库
- **输出**: 优化后的代码、开发文档
- **涉及文件**:
  - 全部新迁移的Entity、Mapper、Service类

- **具体工作**:
  1. 统一QueryWrapper使用规范
  2. 提取公共查询方法到BaseService
  3. 优化APT生成的表定义类的使用方式
  4. 添加详细的注释和JavaDoc
  5. 编写MyBatis-Flex使用指南文档
  6. 记录迁移过程中的坑点和解决方案

- **预估工作量**: 4小时

#### 任务 4.5: 生产环境部署准备

- **目标**: 准备生产环境升级方案
- **输入**: 测试通过的版本
- **输出**: 部署方案、回滚预案、监控配置
- **涉及文件**:
  - 新增: `.claude/docs/deployment/springboot4-mybatisflex-migration-guide.md`

- **具体工作**:
  1. 编写详细的部署步骤
  2. 准备数据库备份方案
  3. 制定灰度发布计划
  4. 配置监控告警 (关注性能指标、SQL审计)
  5. 准备回滚方案 (降级到旧版本)
  6. 编写应急预案

- **预估工作量**: 3小时

---

## 代码对比示例

### 示例 1: 实体类对比

```java
// ==================== MyBatis-Plus ====================
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_player")
public class PlayerEntity extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("device_id")
    private String deviceId;

    private String nickname;
}

// BaseEntity (MyBatis-Plus)
@Data
public abstract class BaseEntity {
    @Version
    @TableField(value = "version")
    private Integer version;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}

// ==================== MyBatis-Flex ====================
@Data
@EqualsAndHashCode(callSuper = true)
@Table("t_player")
public class PlayerEntity extends BaseEntity {
    @Id(keyType = KeyType.Generator, value = "snowFlakeId")
    private Long id;

    @Column("device_id")
    private String deviceId;

    private String nickname;
}

// BaseEntity (MyBatis-Flex)
@Data
public abstract class BaseEntity {
    @Column(value = "version", version = true)
    private Integer version;

    @Column(value = "is_deleted", isLogicDelete = true)
    private Integer isDeleted;

    @Column(value = "created_at", onInsertValue = "now()")
    private LocalDateTime createdAt;

    @Column(value = "updated_at", onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updatedAt;
}
```

### 示例 2: Mapper接口对比

```java
// ==================== MyBatis-Plus ====================
@Mapper
public interface PlayerMapper extends BaseMapper<PlayerEntity> {
    @Select("SELECT * FROM t_player WHERE device_id = #{deviceId} AND is_deleted = 0 LIMIT 1")
    PlayerEntity selectByDeviceId(@Param("deviceId") String deviceId);
}

// ==================== MyBatis-Flex ====================
@Mapper
public interface PlayerMapper extends BaseMapper<PlayerEntity> {
    @Select("SELECT * FROM t_player WHERE device_id = #{deviceId} AND is_deleted = 0 LIMIT 1")
    PlayerEntity selectByDeviceId(@Param("deviceId") String deviceId);
}
// 完全兼容！
```

### 示例 3: Service层查询对比

```java
// ==================== MyBatis-Plus ====================
@Service
public class PlayerService {
    public List<PlayerEntity> getByVipLevel(int minLevel) {
        LambdaQueryWrapper<PlayerEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(PlayerEntity::getVipLevel, minLevel)
               .orderByDesc(PlayerEntity::getCreatedAt);
        return playerMapper.selectList(wrapper);
    }
}

// ==================== MyBatis-Flex ====================
import static com.xiaoyao.logic.entity.table.PlayerEntityTableDef.PLAYER;

@Service
public class PlayerService {
    public List<PlayerEntity> getByVipLevel(int minLevel) {
        QueryWrapper query = QueryWrapper.create()
            .where(PLAYER.VIP_LEVEL.ge(minLevel))
            .orderBy(PLAYER.CREATED_AT.desc());
        return playerMapper.selectListByQuery(query);
    }
}
// 类型安全：PLAYER.VIP_LEVEL会在IDE中自动提示，支持重构
```

### 示例 4: 分页查询对比

```java
// ==================== MyBatis-Plus ====================
public Page<PlayerEntity> pageQuery(int pageNum, int pageSize) {
    Page<PlayerEntity> page = new Page<>(pageNum, pageSize);
    LambdaQueryWrapper<PlayerEntity> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(PlayerEntity::getCreatedAt);
    Page<PlayerEntity> result = playerMapper.selectPage(page, wrapper);

    long total = result.getTotal();
    List<PlayerEntity> records = result.getRecords();
    return result;
}

// ==================== MyBatis-Flex ====================
public Page<PlayerEntity> pageQuery(int pageNum, int pageSize) {
    Page<PlayerEntity> page = Page.of(pageNum, pageSize);
    QueryWrapper query = QueryWrapper.create()
        .orderBy(PLAYER.CREATED_AT.desc());
    Page<PlayerEntity> result = playerMapper.paginate(page, query);

    long total = result.getTotalRow();  // 方法名不同
    List<PlayerEntity> records = result.getRecords();
    return result;
}
```

### 示例 5: 多表关联查询 (MyBatis-Flex新特性)

```java
// MyBatis-Plus: 不支持原生JOIN，需要手写@Select SQL或分次查询
@Select("SELECT p.*, pet.pet_name FROM t_player p " +
        "LEFT JOIN t_pet pet ON p.id = pet.player_id " +
        "WHERE p.id = #{playerId}")
PlayerWithPetVO selectPlayerWithPet(@Param("playerId") Long playerId);

// MyBatis-Flex: 原生支持JOIN查询
import static com.xiaoyao.logic.entity.table.PetEntityTableDef.PET;
import static com.xiaoyao.logic.entity.table.PlayerEntityTableDef.PLAYER;

public PlayerWithPetVO getPlayerWithPet(Long playerId) {
    QueryWrapper query = QueryWrapper.create()
        .select(PLAYER.ALL_COLUMNS, PET.PET_NAME)
        .from(PLAYER)
        .leftJoin(PET).on(PLAYER.ID.eq(PET.PLAYER_ID))
        .where(PLAYER.ID.eq(playerId));
    return playerMapper.selectOneByQueryAs(query, PlayerWithPetVO.class);
}
// 类型安全，支持IDE自动补全和重构
```

---

## 配置文件对比

### MyBatis-Plus 配置 (旧)

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/xiaoyao_game
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    # Druid连接池配置（需要手动配置Bean）

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
  global-config:
    db-config:
      logic-delete-field: isDeleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

```java
// MybatisPlusConfig.java (需要手动配置)
@Configuration
@MapperScan("com.xiaoyao.logic.mapper")
public class MybatisPlusConfig {
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        // 手动配置Druid...
        return dataSource;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(...) {
        // 手动配置SqlSessionFactory...
    }
}

// MetaObjectHandlerConfig.java (需要手动实现)
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}
```

### MyBatis-Flex 配置 (新)

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/xiaoyao_game
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:  # Spring Boot 4自动配置HikariCP
      maximum-pool-size: 20
      minimum-idle: 5

mybatis-flex:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    logic-delete-column: is_deleted
    logic-delete-value: 1
    logic-not-delete-value: 0
    banner: true
```

```java
// MybatisFlexConfig.java (可选，仅用于自定义配置)
@Configuration
public class MybatisFlexConfig implements MyBatisFlexCustomizer {
    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        // 可选: 开启SQL审计
        AuditManager.setAuditEnable(true);
    }
}

// 主类添加@MapperScan
@SpringBootApplication
@MapperScan("com.xiaoyao.logic.mapper")
public class XiaoyaoApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaoyaoApplication.class, args);
    }
}

// 不需要MetaObjectHandler，使用注解配置:
// @Column(onInsertValue = "now()", onUpdateValue = "now()")
```

**配置简化对比:**
- MyBatis-Plus: 需要3个配置类（MybatisPlusConfig、MetaObjectHandlerConfig、数据源配置）
- MyBatis-Flex: 仅需application.yml，配置类可选（Spring Boot自动配置）

---

## 依赖清单

### 当前依赖 (需要移除)

| 依赖 | 当前版本 | 操作 |
|------|----------|------|
| mybatis-plus-spring-boot3-starter | 3.5.9 | 移除 |
| mybatis-plus-jsqlparser | 3.5.9 | 移除 |
| druid | 1.2.23 | 移除 (切换到HikariCP) |
| redisson | 3.34.1 | 移除 (未实际使用) |

### 新增依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| mybatis-flex-spring-boot4-starter | 1.11.5 | MyBatis-Flex核心 (Spring Boot 4专用) |
| mybatis-flex-processor | 1.11.5 | APT注解处理器 (编译时生成表定义类) |

### 保留依赖

| 依赖 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 4.0.0 | 升级 |
| MySQL Connector | 8.0.33+ | 保留或升级 |
| HikariCP | (Spring Boot 4内置) | 自动配置 |
| ionet | 25.2 | 保留 |
| Hutool | 5.8.31 | 保留 |
| Guava | 33.3.0-jre | 保留 |
| Fastjson2 | 2.0.52 | 保留 |
| Lombok | 1.18.42 | 保留 |
| JUnit Jupiter | 5.11.0 | 保留 |

---

## 迁移时间估算

| 阶段 | 预估时间 | 备注 |
|------|----------|------|
| 阶段 1: 依赖分析与准备 | 18小时 | 约2个工作日 (学习MyBatis-Flex + 方案设计) |
| 阶段 2: Spring Boot 4.0 升级 | 13小时 | 约1.5个工作日 (升级框架 + 移除Redisson/Druid) |
| 阶段 3: MyBatis-Flex 迁移 | 29小时 | 约3.5个工作日 (实体/Mapper/Service迁移) |
| 阶段 4: 测试与优化 | 31小时 | 约4个工作日 |
| **总计** | **91小时** | **约11个工作日 (2.2周)** |

**对比Spring Data JDBC方案:**
- Spring Data JDBC方案: 140小时 (17个工作日，3.5周)
- MyBatis-Flex方案: 91小时 (11个工作日，2.2周)
- **节省时间: 49小时 (35%)**

**节省时间的原因:**
1. **API高度相似**: MyBatis-Flex与MyBatis-Plus API兼容度高，减少学习成本
2. **无需编写SQL**: 不需要创建16个XML Mapper文件和手动编写CRUD SQL
3. **配置简化**: 无需手动配置拦截器、分页插件、字段填充等
4. **自动化程度高**: APT自动生成表定义类，QueryWrapper提供类型安全
5. **迁移风险低**: BaseMapper接口完全兼容，@Select注解可保留

---

## 风险识别与应对

### 风险 1: APT注解处理器配置问题

**风险描述**: APT (Annotation Processing Tool) 需要正确配置，否则无法生成表定义类（如PLAYER、PET等）。

**影响范围**: QueryWrapper无法使用类型安全的字段引用

**应对措施**:
1. 在maven-compiler-plugin中正确配置mybatis-flex-processor
2. 执行`mvn clean compile`后检查`target/generated-sources/annotations`目录
3. 验证IDEA能正确识别生成的类（可能需要重新导入Maven项目）
4. 如果APT失败，可降级为字符串查询: `.where("device_id = ?", deviceId)`

**缓解方案**: 提供完整的APT配置示例和故障排查指南

---

### 风险 2: 逻辑删除处理不一致

**风险描述**: MyBatis-Flex的逻辑删除机制与MyBatis-Plus略有不同，可能导致某些查询返回已删除数据。

**影响范围**: 所有使用@Select注解的自定义SQL查询

**应对措施**:
1. 审查所有@Select注解的SQL，确保包含`is_deleted = 0`条件
2. 推荐迁移到QueryWrapper，自动处理逻辑删除
3. 编写单元测试验证逻辑删除功能
4. 在代码Review中重点检查自定义SQL

**缓解方案**: 提供@Select到QueryWrapper的迁移清单

---

### 风险 3: HikariCP连接池参数调优

**风险描述**: 从Druid切换到HikariCP后，连接池参数需要重新调优，可能影响性能。

**影响范围**: 数据库连接性能

**应对措施**:
1. 参考Druid原有参数，配置HikariCP等价参数
2. 进行压力测试，调整连接池大小
3. 监控连接池状态（通过Actuator或JMX）
4. 准备回滚方案（保留Druid配置备份）

**HikariCP参数映射**:
| Druid | HikariCP | 说明 |
|-------|----------|------|
| maxActive: 20 | maximum-pool-size: 20 | 最大连接数 |
| minIdle: 5 | minimum-idle: 5 | 最小空闲连接 |
| maxWait: 60000 | connection-timeout: 30000 | 连接超时 |
| testWhileIdle | - | HikariCP自动检测 |

**缓解方案**: 提供详细的HikariCP配置指南

---

### 风险 4: Service层API调用错误

**风险描述**: MyBatis-Flex的Mapper方法名与MyBatis-Plus略有不同，可能导致编译错误或运行时异常。

**影响范围**: 所有Service层代码

**应对措施**:
1. 使用IDE的全局替换功能批量修改:
   - `selectById` → `selectOneById`
   - `selectList` → `selectListByQuery`
   - `selectPage` → `paginate`
   - `updateById` → `update`
2. 编写单元测试覆盖所有Service方法
3. 逐模块迁移，每个模块完成后立即测试

**API映射清单**:
```java
// MyBatis-Plus → MyBatis-Flex
mapper.selectById(id)              → mapper.selectOneById(id)
mapper.selectBatchIds(ids)         → mapper.selectListByIds(ids)
mapper.selectList(wrapper)         → mapper.selectListByQuery(queryWrapper)
mapper.selectPage(page, wrapper)   → mapper.paginate(page, queryWrapper)
mapper.insert(entity)              → mapper.insert(entity)  // 兼容
mapper.updateById(entity)          → mapper.update(entity)
mapper.deleteById(id)              → mapper.deleteById(id)  // 兼容
```

**缓解方案**: 提供完整的API映射表和代码示例

---

### 风险 5: 字段自动填充逻辑变更

**风险描述**: MyBatis-Flex使用注解配置字段填充，而MyBatis-Plus使用MetaObjectHandler，createdBy和updatedBy需要手动设置。

**影响范围**: 审计字段可能为空

**应对措施**:
1. 在BaseEntity中保留createdBy和updatedBy字段（不使用自动填充）
2. 在Service层的insert和update方法中手动设置操作人ID
3. 从FlowContext获取当前玩家ID:
   ```java
   Long currentUserId = getCurrentUserId();  // 从FlowContext获取
   player.setCreatedBy(currentUserId);
   player.setUpdatedBy(currentUserId);
   ```
4. 编写工具类统一处理审计字段填充

**缓解方案**: 提供AuditHelper工具类封装填充逻辑

---

## 需要进一步明确的问题

### 问题 1: QueryWrapper迁移策略

保留@Select注解的SQL还是全部迁移到QueryWrapper？

**推荐方案:**

**方案 A: 保留@Select注解，渐进式迁移**
- 优点: 迁移成本最低，现有@Select方法无需修改
- 缺点: 失去类型安全和自动逻辑删除的优势
- 适用场景: 快速上线，后续逐步优化

**方案 B: 全部迁移到QueryWrapper (推荐)**
- 优点: 类型安全，支持IDE重构，自动逻辑删除，代码统一
- 缺点: 需要重写查询逻辑，增加工作量约3-5小时
- 适用场景: 追求代码质量和长期可维护性

**等待用户选择:**

```
请选择QueryWrapper迁移策略:
[ ] 方案 A - 保留@Select注解，渐进式迁移
[✅] 方案 B - 全部迁移到QueryWrapper (推荐)
[ ] 其他方案: _______________
```

---

### 问题 2: 是否启用MyBatis-Flex高级特性

MyBatis-Flex提供了SQL审计、实体监听器、字段加密等企业级特性。

**可选特性:**

**SQL审计 (Audit)**
- 功能: 自动记录所有SQL执行情况（耗时、参数、结果）
- 用途: 慢SQL分析、SQL调优、问题排查
- 配置:
  ```java
  AuditManager.setAuditEnable(true);
  AuditManager.setMessageCollector(auditMessage -> {
      if (auditMessage.getElapsedTime() > 1000) {
          log.warn("[慢SQL] {}ms - {}", auditMessage.getElapsedTime(), auditMessage.getFullSql());
      }
  });
  ```

**实体监听器 (Entity Listener)**
- 功能: 在insert、update、delete前后执行自定义逻辑
- 用途: 数据校验、缓存刷新、领域事件发布
- 示例:
  ```java
  @Table(value = "t_player", onInsert = PlayerInsertListener.class)
  public class PlayerEntity extends BaseEntity { ... }
  ```

**字段加密 (Encryption)**
- 功能: 自动加密/解密敏感字段
- 用途: 保护隐私数据（如身份证号、手机号）
- 示例:
  ```java
  @Column(value = "phone", typeHandler = EncryptTypeHandler.class)
  private String phone;
  ```

**等待用户选择:**

```
请选择是否启用以下特性:
[✅] SQL审计 (推荐开启，用于慢SQL分析)
[ ] 实体监听器 (如需发布领域事件，可开启)
[ ] 字段加密 (如有敏感数据，可开启)
[ ] 暂不启用，保持简单 (快速上线后再优化)
```

---

### 问题 3: 是否需要数据库Schema调整

确认现有数据库表结构是否需要调整。

**需要确认的问题:**
1. 表名是否符合命名约定 (已使用`t_`前缀，符合要求)
2. 字段名是否遵循下划线命名 (已使用，如`device_id`，符合要求)
3. `version`字段是否为`INT`类型 (用于乐观锁，需确认)
4. `is_deleted`字段是否为`INT`类型，默认值为0 (需确认)
5. 是否需要添加索引以支持新查询模式

**等待用户选择:**

```
请确认数据库Schema是否需要调整:
[✅] 不需要调整,现有表结构兼容MyBatis-Flex
[ ] 需要调整,具体需求: _______________
```

---

## 用户反馈区域

请在此区域补充您对整体规划的意见和建议:

```
用户补充内容:

1. 关于QueryWrapper迁移策略,我倾向于选择: _______________

2. 关于MyBatis-Flex高级特性,我倾向于选择: _______________

3. 数据库Schema是否需要调整: _______________

4. 其他补充意见:
   _______________
   _______________
   _______________

```

---

## 下一步行动

请在确认上述规划后，逐一回答"需要进一步明确的问题"部分的3个问题，我将根据您的选择生成更详细的实施代码和配置示例。

**待确认的决策点:**
1. ✅ QueryWrapper迁移策略
2. ✅ MyBatis-Flex高级特性启用
3. ✅ 数据库Schema调整需求

确认后，我们可以开始执行"阶段1: 依赖分析与准备阶段"。

---

## 附录: MyBatis-Flex资源链接

- **官方文档**: https://mybatis-flex.com
- **GitHub仓库**: https://github.com/mybatis-flex/mybatis-flex
- **示例代码**: https://github.com/mybatis-flex/mybatis-flex-samples
- **JavaDoc API**: https://mybatis-flex.com/javadoc/
- **对比MyBatis-Plus**: https://mybatis-flex.com/zh/intro/comparison.html
- **快速开始**: https://mybatis-flex.com/zh/intro/getting-started.html
- **APT配置指南**: https://mybatis-flex.com/zh/others/apt.html

---

*本文档基于 MyBatis-Flex v1.11.5 (2024-12-24发布) 和 Spring Boot 4.0 编写。*
