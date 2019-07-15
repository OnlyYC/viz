package com.liaoyb.viz.engine.queryadapter;

import com.google.common.collect.Lists;

import com.liaoyb.viz.engine.config.Consts;
import com.liaoyb.viz.engine.domain.Aggregator;
import com.liaoyb.viz.engine.domain.ExportParam;
import com.liaoyb.viz.engine.domain.FilterGroup;
import com.liaoyb.viz.engine.domain.Group;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.domain.PerformQueryResult;
import com.liaoyb.viz.engine.domain.QueryResult;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.queryengine.JdbcQueryEngine;
import com.liaoyb.viz.engine.source.AbstractJdbcDataSource;
import com.liaoyb.viz.engine.source.DataSource;
import com.liaoyb.viz.engine.util.SqlPageUtils;
import com.liaoyb.viz.engine.util.SqlProcessorWrapper;
import com.liaoyb.viz.engine.util.SqlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liaoyanbo
 * @date 2019-07-04 18:58
 */
@Component
public class PerformQueryAdapter implements QueryAdapter<PerformParam, PerformQueryResult> {
    @Autowired
    private JdbcQueryEngine jdbcQueryEngine;

    @Qualifier("catchableThreadPoolExecutor")
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public boolean support(DataSource source, PerformParam param) {
        return source instanceof AbstractJdbcDataSource && param instanceof PerformParam;
    }

    @Override
    public PerformQueryResult getQueryData(DataSource source, PerformParam param) {
        AbstractJdbcDataSource mySqlSource = (AbstractJdbcDataSource) source;

        QueryResult<Map<String, Object>> queryResult = getData(mySqlSource, param);

        //查询结果
        PerformQueryResult performQueryResult = new PerformQueryResult();
        performQueryResult.setColumns(queryResult.getColumns());
        performQueryResult.setData(queryResult.getData());
        performQueryResult.setRowCount(queryResult.getRowCount());
        performQueryResult.setTotalRowCount(queryResult.getTotalRowCount());
        return performQueryResult;
    }

    @Override
    public void exportQueryData(DataSource source, PerformParam queryParam, ExportParam exportParam, ExportCallback exportCallback) {
        threadPoolExecutor.submit(() -> {
            try {
                //查询数据

                //写入文件
                Thread.sleep(5000);

                File file = new File("geage");
                exportCallback.doComplete(file);
            } catch (Throwable e) {
                exportCallback.uncaughtException(e);
            }
        });
    }

    /**
     * 查询数据
     *
     * @param source       jdbc视图
     * @param performParam 执行参数
     */
    public QueryResult<Map<String, Object>> getData(AbstractJdbcDataSource source, PerformParam performParam) {
        //组装sql
        String targetSql = null;
        if (source.getTableName() != null) {
            targetSql = buildSqlWithTableName(source.getDatabaseTypeEnum(), source.getTableName(), performParam);
        } else {
            targetSql = buildSql(source.getDatabaseTypeEnum(), source.getSql(), performParam);
        }
        SqlPageUtils sqlPageUtil = SqlPageUtils.getPageUtil(source.getDatabaseTypeEnum());

        Long total = null;
        if (performParam.getLimit() != null && performParam.getLimit() != -1) {
            String countSql = sqlPageUtil.getCountSql(targetSql);
            targetSql = sqlPageUtil.getPageSql(targetSql, performParam.getLimit(), 1);

            //执行计算总数sql
            total = jdbcQueryEngine.execute4Count(source, countSql);
        }

        //执行sql
        QueryResult<Map<String, Object>> queryResult = jdbcQueryEngine.execute4Result(source, targetSql);
        long rowCount = Optional.ofNullable(queryResult.getData())
            .map(List::size)
            .map(Long::valueOf)
            .orElse(0L);
        if (total == null) {
            total = rowCount;
        }

        queryResult.setTotalRowCount(total);
        queryResult.setRowCount(rowCount);

        return queryResult;
    }

    /**
     * 处理sql语句
     *
     * @param sql          sql
     * @param performParam 执行参数
     * @return 处理后的sql
     */
    public String buildSql(DatabaseTypeEnum databaseTypeEnum, String sql, PerformParam performParam) {
        String srcSql = sql;
        String innerSql = String.format(Consts.INNER_SQL_QUERY_FORMAT, srcSql);

        //处理权限参数
        List<FilterGroup> filterGroups = Lists.newArrayList();
        if (performParam.getFilterGroups() != null) {
            filterGroups.addAll(performParam.getFilterGroups());
        }

        //行权限
        if (performParam.getRowAuths() != null) {
            filterGroups.addAll(performParam.getRowAuths());
        }
        performParam.setFilterGroups(filterGroups);

        //使用模板处理
        String targetSql = processTemplate(databaseTypeEnum, innerSql, performParam);
        //格式化
        return SqlUtils.formatSql(targetSql);
    }

    /**
     * 处理sql语句
     *
     * @param tableName    表名
     * @param performParam 执行参数
     * @return 处理后的sql
     */
    public String buildSqlWithTableName(DatabaseTypeEnum databaseTypeEnum, String tableName, PerformParam performParam) {
        //处理权限参数
        List<FilterGroup> filterGroups = Lists.newArrayList();
        if (performParam.getFilterGroups() != null) {
            filterGroups.addAll(performParam.getFilterGroups());
        }

        //行权限
        if (performParam.getRowAuths() != null) {
            filterGroups.addAll(performParam.getRowAuths());
        }
        performParam.setFilterGroups(filterGroups);

        //使用模板处理
        String targetSql = processTemplate(databaseTypeEnum, tableName, performParam);
        //格式化
        return SqlUtils.formatSql(targetSql);
    }

    /**
     * 处理sql模板
     *
     * @param srcSql       原始sql
     * @param performParam 执行参数
     */
    private String processTemplate(DatabaseTypeEnum databaseTypeEnum, String srcSql, PerformParam performParam) {
        //构造参数， 原有的被传入的替换
        if (null == performParam.getGroups() || performParam.getGroups().size() < 1) {
            performParam.setGroups(null);
        }

        SqlProcessorWrapper sqlWrapperProcessor = new SqlProcessorWrapper(performParam);

        List<Group> groups = sqlWrapperProcessor.getGroups(databaseTypeEnum);
        List<Aggregator> aggregators = sqlWrapperProcessor.getAggregators(databaseTypeEnum);

        //分组
        String sqlTemplateName = "querySqlSimple";
        if (!CollectionUtils.isEmpty(groups)) {
            sqlTemplateName = "querySqlByGroup";
        }

        STGroup stg = new STGroupFile(Consts.SQL_TEMPLATE);
        ST st = stg.getInstanceOf(sqlTemplateName);
        st.add("groups", groups);
        st.add("aggregators", aggregators);


        st.add("orders", sqlWrapperProcessor.getOrders(databaseTypeEnum));
        //wheres
        st.add("wheres", sqlWrapperProcessor.getWheres(databaseTypeEnum));
        //todo having
        //columns
        st.add("columns", sqlWrapperProcessor.getColumns(databaseTypeEnum));

        st.add("keywordPrefix", databaseTypeEnum.getKeywordPrefix());
        st.add("keywordSuffix", databaseTypeEnum.getKeywordSuffix());

        st.add("aliasPrefix", databaseTypeEnum.getAliasPrefix());
        st.add("aliasSuffix", databaseTypeEnum.getAliasSuffix());
        st.add("sql", srcSql);

        return st.render();
    }
}
