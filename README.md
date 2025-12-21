# 逍遥游：自动悟道 - 游戏服务端

基于 [ionet](https://github.com/iohao/ionet) 框架开发的修仙放置类游戏服务端。

ionet 是一个轻量级无锁异步化、事件驱动的 Java 分布式网络编程框架，支持**纳秒级端到端延迟**。

## 项目结构

```
xiaoyao-server/
├── pom.xml                    # 父 POM
├── xiaoyao-common/            # 公共模块
│   ├── cmd/                   # 路由命令定义
│   ├── proto/                 # 协议消息 (Protobuf)
│   └── error/                 # 错误码定义
│
├── xiaoyao-logic/             # 逻辑服模块
│   ├── player/                # 玩家模块 (Action + Service)
│   ├── realm/                 # 境界模块 (修炼/突破)
│   └── HallLogicServer.java   # 逻辑服配置
│
├── xiaoyao-external/          # 对外服模块 (WebSocket 网关)
│
└── xiaoyao-starter/           # 启动模块
    └── XiaoyaoApplication     # 主启动类
```

## 技术栈

- **框架**: ionet 25.2
- **Java**: JDK 25
- **网络**: Aeron + Netty WebSocket
- **序列化**: Protobuf (JProtobuf)
- **数据库**: MySQL + MyBatis-Plus (待接入)
- **缓存**: Redis (Redisson) (待接入)
- **日志**: SLF4J + Logback

## 快速开始

### 环境要求

- **JDK 25** (ionet 要求)
- Maven 3.9+

### 编译项目

```bash
cd xiaoyao-server
mvn clean compile
```

### 运行服务器

需要添加 VM options 启动参数：

```bash
java --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
     --enable-native-access=ALL-UNNAMED \
     -cp "xiaoyao-starter/target/classes:xiaoyao-starter/target/lib/*" \
     com.xiaoyao.starter.XiaoyaoApplication
```

或者在 IntelliJ IDEA 中配置 VM options：

```
--add-opens java.base/jdk.internal.misc=ALL-UNNAMED
--enable-native-access=ALL-UNNAMED
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
| 2 | 突破 | 境界突破 (108 个境界) |

## 开发说明

### 新增 Action

1. 在 `xiaoyao-common/cmd/` 创建命令接口
2. 在 `xiaoyao-common/proto/` 创建协议消息
3. 在 `xiaoyao-logic/` 创建 Action 类：

```java
@ActionController(YourCmd.cmd)
public class YourAction {
    @ActionMethod(YourCmd.method)
    public YourResp yourMethod(YourReq req, FlowContext ctx) {
        // 业务逻辑
        return resp;
    }
}
```

4. 在 `HallLogicServer.java` 中添加 Action 扫描

### 错误处理

使用 ionet 的断言机制：

```java
// 断言方式
PlayerError.PLAYER_NOT_FOUND.assertNonNull(player);

// 或抛出异常
throw new MessageException(PlayerError.PLAYER_NOT_FOUND);
```

## 相关链接

- [ionet 官方文档](https://iohao.github.io/ionet)
- [ionet GitHub](https://github.com/iohao/ionet)

## License

AGPL-3.0 (遵循 ionet 框架许可)
