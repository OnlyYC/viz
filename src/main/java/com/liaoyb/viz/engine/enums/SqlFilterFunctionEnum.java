package com.liaoyb.viz.engine.enums;

import com.google.common.collect.Lists;

import com.liaoyb.viz.engine.config.Consts;
import com.liaoyb.viz.engine.errors.NotImplementedException;
import com.liaoyb.viz.engine.util.SqlParseUtils;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * 过滤函数枚举
 *
 * @author liaoyanbo
 */
@Slf4j
public enum SqlFilterFunctionEnum {
    /**
     * 等于(相当于in)
     */
    EQUAL("equal", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return inOperationSql("in", name, values);
        }
    },
    /**
     * 不等于
     */
    NOT_EQUAL("not_equal", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return notInOperationSql("not in", name, values);
        }
    },
    /**
     * 包含
     */
    CONTAINS("contains", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (CollectionUtils.isEmpty(values)) {
                log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return name + " like " + SqlParseUtils.formatterSingleQuotes("%" + values.get(0) + "%");
        }
    },
    /**
     * 不包含
     */
    NOT_CONTAINS("not_contains", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (CollectionUtils.isEmpty(values)) {
                log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return name + " not like" + Consts.SPACE + SqlParseUtils.formatterSingleQuotes("%" + values.get(0) + "%");
        }
    },
    /**
     * 为空
     */
    EMPTY("empty", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return name + " is null or " + name + "=''";
        }
    },
    /**
     * 不为空
     */
    NOT_EMPTY("not_empty", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return name + " is not null and " + name + "!=''";
        }
    },
    /**
     * MYSQL、MaxCompute 正则匹配
     */
    REGEX("regex", Lists.newArrayList(DatabaseTypeEnum.MYSQL, DatabaseTypeEnum.MaxCompute), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (CollectionUtils.isEmpty(values)) {
                log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return name + " regexp " + SqlParseUtils.formatterSingleQuotes(values.get(0));
        }
    },
    /**
     * MYSQL、MaxCompute 正则不匹配
     */
    NOT_REGEX("not_regex", Lists.newArrayList(DatabaseTypeEnum.MYSQL, DatabaseTypeEnum.MaxCompute), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (CollectionUtils.isEmpty(values)) {
                log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return " not " + name + " regexp " + SqlParseUtils.formatterSingleQuotes(values.get(0));
        }
    },
    /**
     * LIGHTNING 正则匹配
     */
    REGEX_LIGHTNING("regex", Lists.newArrayList(DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (CollectionUtils.isEmpty(values)) {
                log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return name + " ~* " + SqlParseUtils.formatterSingleQuotes(values.get(0));
        }
    },
    /**
     * LIGHTNING 正则不匹配
     */
    NOT_REGEX_LIGHTNING("not_regex", Lists.newArrayList(DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (CollectionUtils.isEmpty(values)) {
                log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return " not " + name + " ~* " + SqlParseUtils.formatterSingleQuotes(values.get(0));
        }
    },
    /**
     * 有值
     */
    HAS_VALUE("has_value", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return name + " is not null";
        }
    },
    /**
     * 没值
     */
    NO_VALUE("no_value", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return name + " is null";
        }
    },
    /**
     * 区间
     */
    BETWEEN("between", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (values == null) {
                log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, this.getOperator());
                return null;
            }
            Assert.isTrue(values.size() == 2, "between操作类型需要2个参数值，当前为" + values.size());
            return name + " between " + SqlParseUtils.formatterSingleQuotes(values.get(0)) + Consts.SPACE + "AND" + Consts.SPACE + SqlParseUtils.formatterSingleQuotes(values.get(1));
        }
    },
    /**
     * 大于
     */
    GREATER_THAN("greater_than", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (values == null) {
                log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return singleParamValueSql(">", name, values);
        }
    },
    /**
     * 大于等于
     */
    greater_than_or_equal("greater_than_or_equal", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (values == null) {
                log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return singleParamValueSql(">=", name, values);
        }
    },
    /**
     * 小于
     */
    less_than("less_than", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (values == null) {
                log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return singleParamValueSql("<", name, values);
        }
    },
    /**
     * 小于等于
     */
    less_than_or_equal("less_than_or_equal", Lists.newArrayList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            if (values == null) {
                log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, this.getOperator());
                return null;
            }
            return singleParamValueSql("<=", name, values);
        }
    },
    /**
     * 为真
     */
    TRUE("true", Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.BOOLEAN)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return name + "=true";
        }
    },
    /**
     * 为假
     */
    FALSE("false", Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.BOOLEAN)) {
        @Override
        public String convert2SqlCondition(String name, List<String> values) {
            return name + "=false";
        }
    };

    /**
     * 操作符
     */
    private String operator;
    /**
     * 数据库类型
     */
    private List<DatabaseTypeEnum> databaseTypeEnums;
    /**
     * 支持的数据类型
     */
    private List<MetadataColumnTypeEnum> columnTypeEnums;

    public String convert2SqlCondition(String name, List<String> values) {
        throw new NotImplementedException(this.name() + "类型的过滤操作未实现");
    }

    private static String singleParamValueSql(String operator, String name, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            log.debug("过滤参数:({}:{})，参数值长度为0，跳过此操作类型", name, operator);
            return null;
        }
        return name + " " + operator + " " + SqlParseUtils.formatterSingleQuotes(values.get(0));
    }

    /**
     * in 操作类型的sql转换
     */
    private static String inOperationSql(String operator, String name, List<String> values) {
        if (values == null) {
            log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, operator);
            return null;
        }
        if (values.size() == 0) {
            //todo in的values为空，表示没有权限
            return "1!=1";
        }
        StringBuilder expBuilder = new StringBuilder();
        expBuilder
            .append(name).append(Consts.SPACE)
            .append(operator).append(Consts.SPACE)
            .append(values.stream().map(str -> String.format("'%s'", str)).collect(Collectors.joining(",", "(", ")")));
        return expBuilder.toString();
    }

    /**
     * not_in 操作类型的sql转换
     */
    private static String notInOperationSql(String operator, String name, List<String> values) {
        if (values == null) {
            log.debug("过滤参数:({}:{})，参数值为null，跳过此操作类型", name, operator);
            return null;
        }
        if (values.size() == 0) {
            //not in的values为空，表示可以访问
            return null;
        }
        StringBuilder expBuilder = new StringBuilder();
        expBuilder
            .append(name).append(Consts.SPACE)
            .append(operator).append(Consts.SPACE)
            .append(values.stream().map(str -> String.format("'%s'", str)).collect(Collectors.joining(",", "(", ")")));
        return expBuilder.toString();
    }

    SqlFilterFunctionEnum(String operator, List<DatabaseTypeEnum> databaseTypeEnums, List<MetadataColumnTypeEnum> columnTypeEnums) {
        this.operator = operator;
        this.databaseTypeEnums = databaseTypeEnums;
        this.columnTypeEnums = columnTypeEnums;
    }

    public String getOperator() {
        return operator;
    }

    public List<DatabaseTypeEnum> getDatabaseTypeEnums() {
        return databaseTypeEnums;
    }

    public List<MetadataColumnTypeEnum> getColumnTypeEnums() {
        return columnTypeEnums;
    }

    public static SqlFilterFunctionEnum getOperation(String operator, DatabaseTypeEnum databaseType, MetadataColumnTypeEnum columnTypeEnum) {
        for (SqlFilterFunctionEnum operatorEnum : SqlFilterFunctionEnum.values()) {
            if (Objects.equals(operatorEnum.getOperator(), operator)
                && operatorEnum.getDatabaseTypeEnums().contains(databaseType)
                && operatorEnum.getColumnTypeEnums().contains(columnTypeEnum)) {
                return operatorEnum;
            }
        }
        return null;
    }
}
