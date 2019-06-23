package com.liaoyb.viz.engine.core;

import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.source.AbstractJdbcSource;
import com.liaoyb.viz.engine.sql.jdbc.SqlJdbcExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 抽象jdbc查询引擎
 *
 * @author liaoyb
 */
public abstract class AbstractJdbcQueryEngine extends AbstractSqlQueryEngine {
    @Autowired
    private SqlJdbcExecutor sqlJdbcExecutor;


    /**
     * 查询数据
     *
     * @param source       jdbc视图
     * @param performParam 执行参数
     */
    public List<Map<String, Object>> getData(AbstractJdbcSource source, PerformParam performParam) {
        //组装sql
        String targetSql = null;
        if (source.getTableName() != null) {
            targetSql = buildSqlWithTableName(source.getDatabaseTypeEnum(), source.getTableName(), performParam);
        } else {
            targetSql = buildSql(source.getDatabaseTypeEnum(), source.getSql(), performParam);
        }

        //运行sql(返回最后一个sql执行的值)
        return execute(source, targetSql);
    }

    /**
     * 执行sql返回结果
     *
     * @param source    jdbc数据源
     * @param targetSql 目标sql
     * @return 查询结果
     */
    private List<Map<String, Object>> execute(AbstractJdbcSource source, String targetSql) {
        return sqlJdbcExecutor.syncQuery4List(source, targetSql);
    }
}
