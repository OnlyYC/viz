package com.liaoyb.viz.engine.source;

import com.liaoyb.viz.engine.domain.JdbcConfig;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import lombok.Data;

/**
 * jdbc类型数据源
 *
 * @author liaoyb
 */
@Data
public abstract class AbstractJdbcSource implements Source{
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
}
