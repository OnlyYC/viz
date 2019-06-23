package com.liaoyb.viz.engine.util;

import com.google.common.collect.Lists;
import com.liaoyb.viz.engine.config.Consts;
import com.liaoyb.viz.engine.domain.Aggregator;
import com.liaoyb.viz.engine.domain.Column;
import com.liaoyb.viz.engine.domain.FilterGroup;
import com.liaoyb.viz.engine.domain.FilterParam;
import com.liaoyb.viz.engine.domain.FilterParamGroup;
import com.liaoyb.viz.engine.domain.Group;
import com.liaoyb.viz.engine.domain.Order;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.enums.AggregationFunctionEnum;
import com.liaoyb.viz.engine.enums.ConditionConversionEnum;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.enums.DisplayConversionEnum;
import com.liaoyb.viz.engine.enums.GroupConversionEnum;
import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;
import com.liaoyb.viz.engine.enums.QueryParamRelationTypeEnum;
import com.liaoyb.viz.engine.enums.SqlFilterFunctionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql处理扩展
 *
 * @author liaoyanbo
 */
@Slf4j
public class SqlProcessorWrapper {
    private static final String AGGREGATE_REGEX = "sum\\(.*\\)|avg\\(.*\\)|count\\(.*\\)|COUNTDISTINCT\\(.*\\)|max\\(.*\\)|min\\(.*\\)";
    private static final String AND_REGEX = "(.*)(\\s*and\\s*)(\\s*\\)?)\\s*";
    private PerformParam performParam;
    /**
     * sql变量分隔符，如$param1$
     */
    private static final String SQL_TEMP_DELIMITER = "$";

    public SqlProcessorWrapper(PerformParam performParam) {
        this.performParam = performParam;
    }

    public List<Order> getOrders(DatabaseTypeEnum databaseTypeEnum) {
        List<Order> list = null;
        List<Order> orders = this.performParam.getOrders();
        if (null != orders && orders.size() > 0) {
            list = new ArrayList<>();
            Iterator<Order> iterator = orders.iterator();
            String regex = "sum\\(.*\\)|avg\\(.*\\)|count\\(.*\\)|COUNTDISTINCT\\(.*\\)|max\\(.*\\)|min\\(.*\\)";
            Pattern pattern = Pattern.compile(regex);
            while (iterator.hasNext()) {
                Order order = iterator.next();
                String column = order.getColumn().trim();
                Matcher matcher = pattern.matcher(order.getColumn().trim());
                if (!matcher.find()) {
                    column = databaseTypeEnum.getKeywordPrefix() + column + databaseTypeEnum.getKeywordSuffix();
                    order.setColumn(column);
                }
                list.add(order);
            }
        }
        return list;
    }

    /**
     * 获取聚合
     *
     * @param databaseTypeEnum 数据库类型
     * @return 聚合
     */
    public List<Aggregator> getAggregators(DatabaseTypeEnum databaseTypeEnum) {
        if (CollectionUtils.isEmpty(performParam.getAggregators())) {
            performParam.setAggregators(null);
        }
        if (performParam.getAggregators() == null) {
            return null;
        }
        List<Aggregator> result = performParam.getAggregators();
        applyAggregation(result, databaseTypeEnum);
        return result;
    }

    /**
     * 应用聚合
     *
     * @param result           查询列
     * @param databaseTypeEnum 数据库类型
     */
    private void applyAggregation(List<Aggregator> result, DatabaseTypeEnum databaseTypeEnum) {
        result.forEach(aggregator -> {
            if (StringUtils.isEmpty(aggregator.getFunc())) {
                return;
            }
            //todo 支持转换后再进行聚合

            //聚合函数枚举
            AggregationFunctionEnum aggregationFunctionEnum = AggregationFunctionEnum.getFunction(aggregator.getFunc(), databaseTypeEnum, aggregator.getColumnTypeEnum());

            if (aggregationFunctionEnum != null) {
                aggregator.setColumn(aggregationFunctionEnum.applyFunction(aggregator.getColumn()));
                if (StringUtils.isEmpty(aggregator.getAlias())) {
                    aggregator.setAlias(aggregator.getAlias() + "_" + aggregationFunctionEnum.getRemark());
                }
            } else {
                //没有匹配的聚合函数枚举
                log.error(String.format("列:%s[%s]未找到匹配的聚合函数:%s", aggregator.getColumn(), aggregator.getColumnTypeEnum().getValue(), aggregator.getFunc()));
                throw new IllegalArgumentException(String.format("列:%s[%s]不支持的聚合函数:%s", aggregator.getColumn(), aggregator.getColumnTypeEnum().getValue(), aggregator.getFunc()));
            }
        });
    }

    private static String getField(String field, DatabaseTypeEnum databaseTypeEnum) {
        String keywordPrefix = databaseTypeEnum.getKeywordPrefix();
        String keywordSuffix = databaseTypeEnum.getKeywordSuffix();
        if (!StringUtils.isEmpty(keywordPrefix) && !StringUtils.isEmpty(keywordSuffix)) {
            return keywordPrefix + field + keywordSuffix;
        }
        return field;
    }

    /**
     * 获取where条件
     */
    public String getWheres(DatabaseTypeEnum databaseTypeEnum) {
        List<String> conditionGroupList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(this.performParam.getFilterGroups())) {
            for (FilterGroup filterGroup : this.performParam.getFilterGroups()) {
                List<String> conditionParamGroupList = Lists.newArrayList();
                //参数模式
                if (!CollectionUtils.isEmpty(filterGroup.getFilterParamGroups())) {
                    for (FilterParamGroup filterParamGroup : filterGroup.getFilterParamGroups()) {
                        List<String> conditionList = Lists.newArrayList();
                        if (!CollectionUtils.isEmpty(filterParamGroup.getFilterParams())) {
                            //参数值模式
                            for (FilterParam filterParam : filterParamGroup.getFilterParams()) {
                                //原始列
                                String originalColumn = filterParam.getParam();
                                MetadataColumnTypeEnum columnTypeEnum = filterParam.getColumnTypeEnum();
                                if (!StringUtils.isEmpty(filterParam.getConditionConversion())) {
                                    //转换列
                                    ConditionConversionEnum conversionEnum = ConditionConversionEnum.getConditionConversion(filterParam.getConditionConversion(), databaseTypeEnum, columnTypeEnum);
                                    if (conversionEnum != null) {
                                        filterParam.setParam(conversionEnum.convertColumn(filterParam.getParam()));
                                        columnTypeEnum = conversionEnum.getResultTypeEnum();
                                    } else {
                                        //没有匹配的显示转换函数枚举
                                        log.error(String.format("列:%s[%s]未找到匹配的条件转换函数:%s", originalColumn, columnTypeEnum.getValue(), filterParam.getConditionConversion()));
                                        throw new IllegalArgumentException(String.format("列:%s[%s]不支持的条件转换函数:%s", originalColumn, columnTypeEnum.getValue(), filterParam.getConditionConversion()));
                                    }
                                }

                                //转换为条件where表达式
                                SqlFilterFunctionEnum sqlFilterFunctionEnum = SqlFilterFunctionEnum.getOperation(filterParam.getOperation(), databaseTypeEnum, columnTypeEnum);
                                if (sqlFilterFunctionEnum == null) {
                                    //没有匹配的筛选条件类型
                                    log.error(String.format("列:%s[%s]未找到匹配的筛选条件类型:%s", originalColumn, columnTypeEnum.getValue(), filterParam.getOperation()));
                                    //没有匹配的
                                    throw new IllegalArgumentException(String.format("列:%s[%s]不支持的筛选条件类型:%s", originalColumn, columnTypeEnum.getValue(), filterParam.getOperation()));
                                } else {
                                    //转换为sql
                                    String condition = sqlFilterFunctionEnum.convert2SqlCondition(filterParam.getParam(), filterParam.getValues());
                                    if (!StringUtils.isEmpty(condition)) {
                                        conditionList.add(condition);
                                    }
                                }
                            }
                        } else {
                            //表达式模式
                            if (!StringUtils.isEmpty(filterParamGroup.getSqlExpression())) {
                                conditionList.add(filterParamGroup.getSqlExpression());
                            }
                        }

                        if (!CollectionUtils.isEmpty(conditionList)) {
                            String conditionItem = String.join(Consts.SPACE + filterParamGroup.getRelation() + Consts.SPACE, conditionList);
                            conditionParamGroupList.add(Consts.PARENTHESES_START + conditionItem + Consts.PARENTHESES_END);
                        }
                    }
                }

                //总条件
                if (!CollectionUtils.isEmpty(conditionParamGroupList)) {
                    String conditionItem = String.join(Consts.SPACE + filterGroup.getRelation() + Consts.SPACE, conditionParamGroupList);
                    conditionGroupList.add(Consts.PARENTHESES_START + conditionItem + Consts.PARENTHESES_END);
                }
            }
        }

        //todo 权限表达式解析
        if (!CollectionUtils.isEmpty(conditionGroupList)) {
            return String.join(Consts.SPACE + QueryParamRelationTypeEnum.AND.getValue() + Consts.SPACE, conditionGroupList);
        } else {
            return null;
        }
    }

    /**
     * 移除多余的and
     */
    private static String removeLastAdd(String wheres) {
        if (wheres == null) {
            return null;
        }
        wheres = wheres.trim();
        Pattern pattern = Pattern.compile(AND_REGEX);
        Matcher matcher = pattern.matcher(wheres);
        if (matcher.find()) {
            int andStartIndex = matcher.start(2);
            int andEndIndex = matcher.end(2);
            return wheres.substring(0, andStartIndex) + wheres.substring(andEndIndex);
        }

        return wheres;
    }

    public static void main(String[] args) {
        System.out.println(removeLastAdd("kleklakealk and"));
        System.out.println(removeLastAdd("kleklakealk and   )   "));
    }


    /**
     * 获取分组
     *
     * @return 列
     */
    public List<Group> getGroups(DatabaseTypeEnum databaseTypeEnum) {
        if (CollectionUtils.isEmpty(performParam.getGroups())) {
            performParam.setGroups(null);
        }
        if (performParam.getGroups() == null) {
            return null;
        }

        List<Group> result = performParam.getGroups();
        //分组转换
        columnGroupConversion(result, databaseTypeEnum);
        return result;
    }

    /**
     * 获取列
     *
     * @return 列
     */
    public List<Column> getColumns(DatabaseTypeEnum databaseTypeEnum) {
        if (CollectionUtils.isEmpty(performParam.getColumns())) {
            performParam.setColumns(null);
        }
        if (performParam.getColumns() == null) {
            return null;
        }

        List<Column> result = performParam.getColumns();
        //显示转换
        displayConversion(result, databaseTypeEnum);
        return result;
    }

    /**
     * 显示转换
     *
     * @param result           查询列
     * @param databaseTypeEnum 数据库类型
     */
    private void displayConversion(List<Column> result, DatabaseTypeEnum databaseTypeEnum) {
        result.forEach(column -> {
            if (StringUtils.isEmpty(column.getDisplayConversion())) {
                return;
            }
            //todo 转换列支持多种转换
            DisplayConversionEnum conversionEnum = DisplayConversionEnum.getDisplayConversion(column.getDisplayConversion(), databaseTypeEnum, column.getColumnTypeEnum());
            if (conversionEnum != null) {
                column.setColumn(conversionEnum.convertColumn(column.getColumn()));
            } else {
                //没有匹配的显示转换函数枚举
                log.error(String.format("列:%s[%s]未找到匹配的显示转换函数:%s", column.getColumn(), column.getColumnTypeEnum().getValue(), column.getDisplayConversion()));
                throw new IllegalArgumentException(String.format("列:%s[%s]不支持的显示转换函数:%s", column.getColumn(), column.getColumnTypeEnum().getValue(), column.getDisplayConversion()));
            }
        });
    }

    /**
     * 分组转换
     *
     * @param result           查询列
     * @param databaseTypeEnum 数据库类型
     */
    private void columnGroupConversion(List<Group> result, DatabaseTypeEnum databaseTypeEnum) {
        result.forEach(column -> {
            if (StringUtils.isEmpty(column.getGroupConversion())) {
                return;
            }
            //分组转换
            GroupConversionEnum conversionEnum = GroupConversionEnum.getGroupConversion(column.getGroupConversion(), databaseTypeEnum, column.getColumnTypeEnum());
            if (conversionEnum != null) {
                column.setColumn(conversionEnum.convertColumn(column.getColumn()));
                if (StringUtils.isEmpty(column.getAlias())) {
                    column.setAlias(column.getAlias() + "_" + conversionEnum.getRemark());
                }
            } else {
                //没有匹配的分组转换函数枚举
                log.error(String.format("列:%s[%s]未找到匹配的分组转换函数:%s", column.getColumn(), column.getColumnTypeEnum().getValue(), column.getGroupConversion()));
                throw new IllegalArgumentException(String.format("列:%s[%s]不支持的分组转换函数:%s", column.getColumn(), column.getColumnTypeEnum().getValue(), column.getGroupConversion()));
            }
        });
    }
}
