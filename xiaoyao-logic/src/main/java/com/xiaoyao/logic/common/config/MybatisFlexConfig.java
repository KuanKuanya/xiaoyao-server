package com.xiaoyao.logic.common.config;

import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.spring.FlexSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * MyBatis-Flex 配置类
 * 手动配置 SqlSessionFactory 以解决 Spring Boot 4.0 兼容性问题
 * 
 * 注意: @MapperScan 已移至 XiaoyaoApplication 启动类
 *
 * @author xiaoyao
 */
@Slf4j
@Configuration
public class MybatisFlexConfig {

    /**
     * 配置 SqlSessionFactory
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        FlexSqlSessionFactoryBean factoryBean = new FlexSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 初始化
        factoryBean.afterPropertiesSet();
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();

        // 配置驼峰映射
        if (sqlSessionFactory != null) {
            sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        }

        // 开启 SQL 审计
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(auditMessage -> {
            if (auditMessage.getElapsedTime() > 1000) {
                log.warn("[慢SQL警告] 执行时间: {}ms, SQL: {}",
                        auditMessage.getElapsedTime(),
                        auditMessage.getFullSql());
            }
        });

        log.info("[MyBatis-Flex] SqlSessionFactory 配置完成");
        return sqlSessionFactory;
    }

    /**
     * 配置 SqlSessionTemplate
     */
    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
