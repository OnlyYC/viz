package com.liaoyb.viz.engine.enums;

import com.google.common.collect.Lists;

import com.liaoyb.viz.engine.errors.NotImplementedException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * todo 聚合函数枚举
 *
 * @author liaoyanbo
 */
public enum AggregationFunctionEnum {
    /**
     * 总和
     */
    SUM("sum", "总和", Arrays.asList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.NUMBER)) {
        @Override
        public String applyFunction(String column) {
            return this.getFun() + "(" + column + ")";
        }
    },
    /**
     * 平均值
     */
    AVG("avg", "平均值", Arrays.asList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.NUMBER)) {
        @Override
        public String applyFunction(String column) {
            return this.getFun() + "(" + column + ")";
        }
    },
    /**
     * 计数
     */
    COUNT("count", "计数", Arrays.asList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String applyFunction(String column) {
            return this.getFun() + "(" + column + ")";
        }
    },
    /**
     * 去重数
     */
    COUNT_DISTINCT("count_distinct", "去重数", Arrays.asList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String applyFunction(String column) {
            return "count(distinct " + column + ")";
        }
    },
    /**
     * 最大值
     */
    MAX("max", "去重数", Arrays.asList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String applyFunction(String column) {
            return this.getFun() + "(" + column + ")";
        }
    },
    /**
     * 最小值
     */
    MIN("min", "最小值", Arrays.asList(DatabaseTypeEnum.values()), Lists.newArrayList(MetadataColumnTypeEnum.values())) {
        @Override
        public String applyFunction(String column) {
            return this.getFun() + "(" + column + ")";
        }
    },
    /**
     * 按年计数
     */
    COUNT_BY_YEAR_MYSQL("count_by_year", "按年计数", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String applyFunction(String column) {
            return "count(date_format(" + column + ", '%Y'))";
        }
    },
    /**
     * 按年去重数
     */
    COUNT_DISTINCT_BY_YEAR_MYSQL("count_distinct_by_year", "去重数", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String applyFunction(String column) {
            return "count(distinct date_format(" + column + ", '%Y'))";
        }
    },
    /**
     * 按年计数
     */
    COUNT_BY_YEAR_MaxCompute("count_by_year", "按年计数", Lists.newArrayList(DatabaseTypeEnum.MaxCompute), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String applyFunction(String column) {
            return "count(TO_CHAR(" + column + ", 'yyyy'))";
        }
    },
    /**
     * 按年去重数
     */
    COUNT_DISTINCT_BY_YEAR_MaxCompute("count_distinct_by_year", "去重数", Lists.newArrayList(DatabaseTypeEnum.MaxCompute), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String applyFunction(String column) {
            return "count(distinct TO_CHAR(" + column + ", 'yyyy'))";
        }
    },
    /**
     * 按年计数
     */
    COUNT_BY_YEAR_LIGHTNING("count_by_year", "按年计数", Lists.newArrayList(DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String applyFunction(String column) {
            return "count(TO_CHAR(" + column + ", 'yyyy'))";
        }
    },
    /**
     * 按年去重数
     */
    COUNT_DISTINCT_BY_YEAR_LIGHTNING("count_distinct_by_year", "去重数", Lists.newArrayList(DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String applyFunction(String column) {
            return "count(distinct TO_CHAR(" + column + ", 'yyyy'))";
        }
    };
    //todo 补充 按月、按日

    /**
     * 函数
     */
    private String fun;
    /**
     * 备注
     */
    private String remark;
    /**
     * 数据库类型
     */
    private List<DatabaseTypeEnum> databaseTypeEnums;
    /**
     * 支持的数据类型
     */
    private List<MetadataColumnTypeEnum> columnTypeEnums;


    /**
     * 应用聚合函数
     */
    public String applyFunction(String column) {
        throw new NotImplementedException(this.name() + "聚合函数未实现");
    }

    public String getRemark() {
        return remark;
    }

    public String getFun() {
        return fun;
    }

    public List<DatabaseTypeEnum> getDatabaseTypeEnums() {
        return databaseTypeEnums;
    }

    public List<MetadataColumnTypeEnum> getColumnTypeEnums() {
        return columnTypeEnums;
    }

    /**
     * 根据函数、数据库类型、数据类型获取聚合函数枚举
     *
     * @param fun            函数
     * @param databaseType   数据库类型
     * @param columnTypeEnum 数据类型
     * @return 聚合函数枚举
     */
    public static AggregationFunctionEnum getFunction(String fun, DatabaseTypeEnum databaseType, MetadataColumnTypeEnum columnTypeEnum) {
        for (AggregationFunctionEnum conversionEnum : AggregationFunctionEnum.values()) {
            if (Objects.equals(conversionEnum.getFun(), fun)
                && conversionEnum.getDatabaseTypeEnums().contains(databaseType)
                && conversionEnum.getColumnTypeEnums().contains(columnTypeEnum)) {
                return conversionEnum;
            }
        }
        return null;
    }

    AggregationFunctionEnum(String fun, String remark, List<DatabaseTypeEnum> databaseTypeEnums, List<MetadataColumnTypeEnum> columnTypeEnums) {
        this.fun = fun;
        this.remark = remark;
        this.databaseTypeEnums = databaseTypeEnums;
        this.columnTypeEnums = columnTypeEnums;
    }
}
