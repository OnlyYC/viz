package com.liaoyb.viz.engine.core;

import com.google.common.collect.Lists;
import com.liaoyb.viz.engine.config.Consts;
import com.liaoyb.viz.engine.domain.Aggregator;
import com.liaoyb.viz.engine.domain.FilterGroup;
import com.liaoyb.viz.engine.domain.Group;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.util.SqlProcessorWrapper;
import com.liaoyb.viz.engine.util.SqlUtils;
import org.springframework.util.CollectionUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

/**
 * sql查询引擎
 *
 * @author liaoyb
 */
public abstract class AbstractSqlQueryEngine implements QueryFunction {
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
