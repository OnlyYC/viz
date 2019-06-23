package com.liaoyb.viz.engine.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库类型枚举
 *
 * @author liaoyanbo
 */
@Slf4j
public enum DatabaseTypeEnum {
    /**
     * MYSQL
     */
    MYSQL("mysql", "mysql", "com.mysql.jdbc.Driver", "SELECT 1", "", "", "'", "'"),
    /**
     * Lightning
     */
    LIGHTNING("lightning", "lightning", "org.postgresql.Driver", "SELECT 1", "", "", "\"", "\""),
    /**
     * MaxCompute
     */
    MaxCompute("mysql", "mysql", "com.mysql.jdbc.Driver", "SELECT 1", "", "", "`", "`"),
    /**
     * ORACLE
     */
    ORACLE("oracle", "oracle", "oracle.jdbc.driver.OracleDriver", "SELECT 1", "\"", "\"", "\"", "\"");

    /**
     * feature
     */
    private String feature;
    /**
     * 描述
     */
    private String desc;
    /**
     * 驱动
     */
    private String driver;
    /**
     * 测试连接sql
     */
    private String connectionTestQuery;
    /**
     * 关键词前缀
     */
    private String keywordPrefix;
    /**
     * 关键词后缀
     */
    private String keywordSuffix;
    /**
     * 别名前缀
     */
    private String aliasPrefix;
    /**
     * 别名后缀
     */
    private String aliasSuffix;

    private static final String JDBC_URL_PREFIX = "jdbc:";

    DatabaseTypeEnum(String feature, String desc, String driver, String connectionTestQuery, String keywordPrefix, String keywordSuffix, String aliasPrefix, String aliasSuffix) {
        this.feature = feature;
        this.desc = desc;
        this.driver = driver;
        this.connectionTestQuery = connectionTestQuery;
        this.keywordPrefix = keywordPrefix;
        this.keywordSuffix = keywordSuffix;
        this.aliasPrefix = aliasPrefix;
        this.aliasSuffix = aliasSuffix;
    }

    public static DatabaseTypeEnum urlOf(String jdbcUrl) {
        String url = jdbcUrl.toLowerCase();
        for (DatabaseTypeEnum databaseTypeEnum : values()) {
            if (url.startsWith(JDBC_URL_PREFIX + databaseTypeEnum.feature)) {
                try {
                    Class<?> aClass = Class.forName(databaseTypeEnum.getDriver());
                    if (null == aClass) {
                        throw new IllegalArgumentException("Unable to get driver instance for jdbcUrl: " + jdbcUrl);
                    }
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Unable to get driver instance: " + jdbcUrl);
                }
                return databaseTypeEnum;
            }
        }
        return null;
    }

    public String getFeature() {
        return feature;
    }

    public String getDesc() {
        return desc;
    }

    public String getDriver() {
        return driver;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public String getKeywordPrefix() {
        return keywordPrefix;
    }

    public String getKeywordSuffix() {
        return keywordSuffix;
    }

    public String getAliasPrefix() {
        return aliasPrefix;
    }

    public String getAliasSuffix() {
        return aliasSuffix;
    }
}
