package com.xiaoyao.logic;

import com.iohao.net.extension.codegen.TypeScriptDocumentGenerate;
import com.iohao.net.extension.protobuf.ProtoGenerateFile;
import com.iohao.net.framework.CoreGlobalConfig;
import com.iohao.net.framework.core.BarSkeleton;
import com.iohao.net.framework.core.doc.DocumentHelper;
import com.iohao.net.server.LogicServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;

/**
 * TypeScript SDK 代码生成测试类
 * <p>
 * 运行此类的 main 方法，即可为前端生成 TypeScript SDK 代码
 * 生成的代码包括：
 * - Action 调用接口
 * - 广播监听代码
 * - 错误码
 * - .proto 协议文件
 * <p>
 * 参考：ionet-examples/ionet-sdk-example/src/main/java/com/iohao/example/sdk/GenerateTest.java
 *
 * @author xiaoyao
 */
@Slf4j
public final class GenerateTest {

    /** 前端项目根路径 */
    static final String FRONTEND_ROOT = "/Users/kuankuan/games/game-xiuxian";
    /** 前端项目代码生成路径 */
    static final String CODE_PATH = FRONTEND_ROOT + "/src/gen/code";
    /** 前端项目 proto 生成路径 */
    static final String PROTO_PATH = FRONTEND_ROOT + "/proto";

    public static void main(String[] args) {
        // 设置语言
        Locale.setDefault(Locale.US);

        // 开启文档解析（必须在 BarSkeleton.build() 之前设置）
        CoreGlobalConfig.setting.parseDoc = true;

        // 加载游戏逻辑服的业务框架
        listLogic().forEach(logicServer -> {
            var builder = BarSkeleton.builder();
            logicServer.settingBarSkeletonBuilder(builder);
            builder.build();
        });

        // 配置 TypeScript 代码生成器
        generateCodeTypeScript();

        // 生成文档和代码
        DocumentHelper.generateDocument();

        // 生成 .proto 协议文件
        // generateProtoFile();

        log.info("========================================");
        log.info("代码生成完成！");
        log.info("TypeScript SDK: {}", CODE_PATH);
        log.info("Proto 文件: {}", PROTO_PATH);
        log.info("========================================");
        log.info("下一步：在前端目录运行 'npx buf generate' 编译 proto 文件");
    }

    /**
     * 获取逻辑服列表
     */
    static List<LogicServer> listLogic() {
        return List.of(new HallLogicServer());
    }

    /**
     * 生成 TypeScript SDK 代码
     */
    private static void generateCodeTypeScript() {
        var documentGenerate = new TypeScriptDocumentGenerate();
        documentGenerate.setPath(CODE_PATH);
        DocumentHelper.addDocumentGenerate(documentGenerate);
        log.info("[GenerateTest] 将生成 TypeScript SDK 到: {}", CODE_PATH);
    }

    /**
     * 生成 .proto 协议文件
     */
    private static void generateProtoFile() {
        // proto 类所在的源码路径 (xiaoyao-common 模块)
        String protoSourcePath = "/Users/kuankuan/games/xiaoyao-server/xiaoyao-common/src/main/java";

        var protoGenerateFile = new ProtoGenerateFile()
                .setGenerateFolder(PROTO_PATH)
                .setProtoSourcePath(protoSourcePath)
                .addProtoPackage("com.xiaoyao.common.proto");

        protoGenerateFile.generate();
        log.info("[GenerateTest] 将生成 .proto 文件到: {}", PROTO_PATH);
    }

    /**
     * JUnit 测试入口
     */
    @org.junit.jupiter.api.Test
    public void testGenerate() {
        main(null);
    }
}
