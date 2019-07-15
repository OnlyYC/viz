package com.liaoyb.viz.engine.sql.jdbc;

import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.errors.SourceException;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * jdbc数据源
 *
 * @author liaoyanbo
 */
@Slf4j
@Component
public class JdbcDataSource {
    /**
     * key连接符
     */
    private static String DATASOURCE_KEY_JOINER = "@";

    @Value("${source.dataSource.connectionTimeout:9000}")
    private int connectionTimeout;

    @Value("${source.dataSource.idleTimeout:45000}")
    private int idleTimeout;

    @Value("${source.dataSource.maxLifetime:50000}")
    private int maxLifetime;

    @Value("${source.dataSource.maximumPoolSize:10}")
    private int maximumPoolSize;

    @Value("${source.dataSource.minimumIdle:5}")
    private int minimumIdle;

    @Value("${source.initial-size:1}")
    private int initialSize;

    @Value("${source.min-idle:3}")
    private int minIdle;

    private static volatile Map<String, HikariDataSource> map = new ConcurrentHashMap<>();

    /**
     * 获取数据库连接池
     *
     * @return 连接池
     */
    public synchronized HikariDataSource getDataSource(DatabaseTypeEnum databaseTypeEnum, String jdbcUrl, String username, String password) throws SourceException {
        if (!map.containsKey(username + DATASOURCE_KEY_JOINER + jdbcUrl.trim()) || null == map.get(username + DATASOURCE_KEY_JOINER + jdbcUrl.trim())) {
            HikariDataSource instance = new HikariDataSource();
            try {
                String className = DriverManager.getDriver(jdbcUrl.trim()).getClass().getName();
                instance.setDriverClassName(className);
            } catch (SQLException e) {
                throw new SourceException("加载数据库驱动异常", e);
            }

            instance.setJdbcUrl(jdbcUrl.trim());
            instance.setUsername(username);
            instance.setPassword(password);
            instance.setConnectionTestQuery(databaseTypeEnum.getConnectionTestQuery());
            instance.setConnectionTimeout(connectionTimeout);
            instance.setIdleTimeout(idleTimeout);
            instance.setMaxLifetime(maxLifetime);
            instance.setMaximumPoolSize(maximumPoolSize);
            instance.setMinimumIdle(minimumIdle);

            map.put(username + DATASOURCE_KEY_JOINER + jdbcUrl.trim(), instance);
        }

        return map.get(username + DATASOURCE_KEY_JOINER + jdbcUrl.trim());
    }
}
