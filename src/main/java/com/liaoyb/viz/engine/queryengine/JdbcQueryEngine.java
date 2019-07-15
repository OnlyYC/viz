package com.liaoyb.viz.engine.queryengine;

import com.liaoyb.viz.engine.domain.QueryResult;
import com.liaoyb.viz.engine.source.AbstractJdbcDataSource;
import com.liaoyb.viz.engine.sql.jdbc.SqlJdbcExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * jdbc查询引擎
 *
 * @author liaoyanbo
 */
@Component
public class JdbcQueryEngine {
    @Autowired
    private SqlJdbcExecutor sqlJdbcExecutor;


    /**
     * 执行sql返回结果
     *
     * @param source    jdbc数据源
     * @param targetSql 目标sql
     * @return 查询结果
     */
    public List<Map<String, Object>> execute(AbstractJdbcDataSource source, String targetSql) {
        return sqlJdbcExecutor.syncQuery4List(source, targetSql);
    }


    public QueryResult<Map<String, Object>> execute4Result(AbstractJdbcDataSource source, String targetSql) {
        return sqlJdbcExecutor.query4Result(source, targetSql);
    }

    /**
     * 执行sql返回总数
     */
    public long execute4Count(AbstractJdbcDataSource source, String countSql) {
        return sqlJdbcExecutor.queryForObject(source, countSql, Long.class);
    }
}
