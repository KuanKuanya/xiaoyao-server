# 水墨修仙：长生路 - 游戏服务端

基于 [ioGame](https://github.com/iohao/ioGame) 框架开发的修仙游戏服务端。

## 项目结构

```
xiuxian-server/
├── pom.xml                    # 父 POM
├── xiuxian-common/            # 公共模块
│   ├── cmd/                   # 路由命令定义
│   ├── proto/                 # 协议消息 (Protobuf)
│   ├── error/                 # 错误码定义
│   └── util/                  # 工具类
│
├── xiuxian-logic/             # 逻辑服模块
│   ├── player/                # 玩家模块
│   ├── realm/                 # 境界模块
│   ├── combat/                # 战斗模块
│   ├── inventory/             # 背包模块
│   └── ...                    # 其他业务模块
│
├── xiuxian-external/          # 对外服模块
│   └── server/                # WebSocket 网关
│
└── xiuxian-starter/           # 启动模块
    └── XiuxianApplication     # 主启动类
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
cd xiuxian-server
mvn clean install -DskipTests
```

### 运行服务器

```bash
cd xiuxian-starter
mvn exec:java -Dexec.mainClass="com.xiuxian.starter.XiuxianApplication"
```

或者使用打包后的 JAR：

```bash
java -jar xiuxian-starter/target/xiuxian-starter-1.0.0-SNAPSHOT.jar
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

## 协议格式

请求/响应使用 Protobuf 二进制格式，通过 WebSocket 传输。

请求示例 (JSON 格式展示)：

```json
{
  "cmd": 2,
  "subCmd": 1,
  "data": {
    "platform": 0,
    "deviceId": "test-device-001",
    "clientVersion": "1.0.0"
  }
}
```

## 开发说明

### 新增模块

1. 在 `xiuxian-common/cmd/` 创建命令接口
2. 在 `xiuxian-common/proto/` 创建协议消息
3. 在 `xiuxian-logic/` 创建 Action 和 Service
4. 在 `LogicServer.java` 中添加 Action 扫描

### 错误处理

使用统一错误码，在 `xiuxian-common/error/` 定义：

```java
throw new MsgException(PlayerError.PLAYER_NOT_FOUND);
```

## 待开发功能

- [ ] 战斗系统
- [ ] 背包系统
- [ ] 装备系统
- [ ] 技能系统
- [ ] 宗门系统
- [ ] 战宠系统
- [ ] 任务系统
- [ ] 排行榜

## License

MIT
