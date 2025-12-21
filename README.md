# 逍遥游：自动悟道 - 游戏服务端

基于 [ioGame](https://github.com/iohao/ioGame) 框架开发的修仙放置类游戏服务端。

## 项目结构

```
xiaoyao-server/
├── pom.xml                    # 父 POM
├── xiaoyao-common/            # 公共模块
│   ├── cmd/                   # 路由命令定义
│   ├── proto/                 # 协议消息 (Protobuf)
│   ├── error/                 # 错误码定义
│   └── util/                  # 工具类
│
├── xiaoyao-logic/             # 逻辑服模块
│   ├── player/                # 玩家模块
│   ├── realm/                 # 境界模块
│   ├── combat/                # 战斗模块
│   ├── inventory/             # 背包模块
│   └── ...                    # 其他业务模块
│
├── xiaoyao-external/          # 对外服模块
│   └── server/                # WebSocket 网关
│
└── xiaoyao-starter/           # 启动模块
    └── XiaoyaoApplication     # 主启动类
```

## 技术栈

- **框架**: ioGame 21.19
- **Java**: JDK 21+
- **网络**: Netty + WebSocket
- **序列化**: Protobuf (JProtobuf)
- **数据库**: MySQL + MyBatis-Plus
- **缓存**: Redis (Redisson)
- **日志**: SLF4J + Logback

## 快速开始

### 环境要求

- JDK 21 或更高版本
- Maven 3.8+
- MySQL 8.0+ (可选，目前使用内存存储)
- Redis 6.0+ (可选)

### 编译项目

```bash
cd xiaoyao-server
mvn clean install -DskipTests
```

### 运行服务器

```bash
cd xiaoyao-starter
mvn exec:java -Dexec.mainClass="com.xiaoyao.starter.XiaoyaoApplication"
```

或者使用打包后的 JAR：

```bash
java -jar xiaoyao-starter/target/xiaoyao-starter-1.0.0-SNAPSHOT.jar
```

### 客户端连接

服务器启动后，WebSocket 连接地址：

```
ws://127.0.0.1:10100/websocket
```

## 已实现的功能

### 玩家模块 (cmd=2)
| 子命令 | 功能 | 说明 |
|--------|------|------|
| 1 | 登录 | 使用设备ID登录，自动创建新玩家 |
| 3 | 获取信息 | 获取玩家基础数据 |
| 5 | 心跳 | 保持连接活跃 |

### 境界模块 (cmd=11)
| 子命令 | 功能 | 说明 |
|--------|------|------|
| 1 | 修炼 | 领取挂机经验 |
| 2 | 突破 | 境界突破 |

## 开发说明

### 新增模块

1. 在 `xiaoyao-common/cmd/` 创建命令接口
2. 在 `xiaoyao-common/proto/` 创建协议消息
3. 在 `xiaoyao-logic/` 创建 Action 和 Service
4. 在 `LogicServer.java` 中添加 Action 扫描

### 错误处理

使用统一错误码，在 `xiaoyao-common/error/` 定义：

```java
throw new MsgException(PlayerError.PLAYER_NOT_FOUND);
```

## License

MIT
