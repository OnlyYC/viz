package com.liaoyb.viz.engine.source;

import com.liaoyb.viz.engine.domain.JdbcConfig;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;

/**
 * mysql数据源
 *
 * @author liaoyb
 */
public class MySqlSource extends AbstractJdbcSource {
    /**
     * 以表名创建mysql数据源实例
     *
     * @param jdbcConfig       jdbc配置
     * @param databaseTypeEnum 数据库类型
     * @param tableName        表名
     * @return mysql数据源实例
     */
    public static MySqlSource newInstanceWithTableName(JdbcConfig jdbcConfig, DatabaseTypeEnum databaseTypeEnum, String tableName) {
        MySqlSource mySqlSource = new MySqlSource();
        mySqlSource.setJdbcConfig(jdbcConfig);
        mySqlSource.setDatabaseTypeEnum(databaseTypeEnum);
        mySqlSource.setTableName(tableName);
        return mySqlSource;
    }


    public static MySqlSource newInstanceWithSql(JdbcConfig jdbcConfig, DatabaseTypeEnum databaseTypeEnum, String sql) {
        MySqlSource mySqlSource = new MySqlSource();
        mySqlSource.setJdbcConfig(jdbcConfig);
        mySqlSource.setDatabaseTypeEnum(databaseTypeEnum);
        mySqlSource.setSql(sql);
        return mySqlSource;
    }
}
