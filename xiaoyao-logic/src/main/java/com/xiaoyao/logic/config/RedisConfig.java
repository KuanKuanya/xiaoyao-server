package com.xiaoyao.logic.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置类
 *
 * @author xiaoyao
 */
@Slf4j
@Configuration
public class RedisConfig {

    @Value("${data.redis.host:127.0.0.1}")
    private String host;

    @Value("${data.redis.port:6379}")
    private int port;

    @Value("${data.redis.password:}")
    private String password;

    @Value("${data.redis.database:0}")
    private int database;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 单节点配置
        String address = "redis://" + host + ":" + port;
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password.isEmpty() ? null : password)
                .setDatabase(database)
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(3000)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1500);

        log.info("[Redis配置] 初始化 Redisson 客户端 address={}", address);

        return Redisson.create(config);
    }
}
