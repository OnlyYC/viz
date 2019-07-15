package com.liaoyb.viz.engine.source;

import com.liaoyb.viz.engine.domain.JdbcConfig;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.enums.SourceTypeEnum;
import com.liaoyb.viz.engine.errors.NotSupportException;

import lombok.Data;

/**
 * jdbc类型数据源
 *
 * @author liaoyanbo
 */
@Data
public abstract class AbstractJdbcDataSource implements DataSource {
    /**
     * jdbc配置
     */
    protected JdbcConfig jdbcConfig;
    /**
     * 数据库类型
     */
    protected DatabaseTypeEnum databaseTypeEnum;
    /**
     * 表名
     */
    protected String tableName;
    /**
     * sql
     */
    protected String sql;

    /**
     * 以表名创建mysql数据源实例
     *
     * @param jdbcConfig     jdbc配置
     * @param sourceTypeEnum 数据源类型
     * @param tableName      表名
     * @return mysql数据源实例
     */
    public static AbstractJdbcDataSource newInstanceWithTableName(JdbcConfig jdbcConfig, SourceTypeEnum sourceTypeEnum, String tableName) {
        AbstractJdbcDataSource abstractJdbcDataSource = null;

        //根据数据源类型获取匹配的数据库类型
        DatabaseTypeEnum databaseTypeEnum = DatabaseTypeEnum.getMatchDatabaseType(sourceTypeEnum);
        if (databaseTypeEnum == null) {
            throw new IllegalStateException("数据源未找到匹配的数据库类型:" + sourceTypeEnum);
        }

        //todo 新增DataSource实例，需修改这里
        if (sourceTypeEnum == SourceTypeEnum.MYSQL) {
            abstractJdbcDataSource = new MySqlDataSource();
        } else {
            //不支持的数据源类型
            throw new NotSupportException("不支持的数据源类型:" + sourceTypeEnum);
        }

        abstractJdbcDataSource.setJdbcConfig(jdbcConfig);
        abstractJdbcDataSource.setTableName(tableName);
        return abstractJdbcDataSource;
    }


    public static AbstractJdbcDataSource newInstanceWithSql(JdbcConfig jdbcConfig, SourceTypeEnum sourceTypeEnum, String sql) {
        AbstractJdbcDataSource abstractJdbcDataSource = null;


        //根据数据源类型获取匹配的数据库类型
        DatabaseTypeEnum databaseTypeEnum = DatabaseTypeEnum.getMatchDatabaseType(sourceTypeEnum);
        if (databaseTypeEnum == null) {
            throw new IllegalStateException("数据源未找到匹配的数据库类型:" + sourceTypeEnum);
        }

        //todo 新增DataSource实例，需修改这里
        if (sourceTypeEnum == SourceTypeEnum.MYSQL) {
            abstractJdbcDataSource = new MySqlDataSource();
        } else {
            //不支持的数据源类型
            throw new NotSupportException("不支持的数据源类型:" + sourceTypeEnum);
        }

        abstractJdbcDataSource.setJdbcConfig(jdbcConfig);
        abstractJdbcDataSource.setSql(sql);
        return abstractJdbcDataSource;
    }


}
