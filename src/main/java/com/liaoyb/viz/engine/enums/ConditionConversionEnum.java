package com.liaoyb.viz.engine.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 条件转换枚举
 *
 * @author liaoyanbo
 */
public enum ConditionConversionEnum {
    //日期=====================================================================================
    /**
     * mysql 按时分秒
     */
    MYSQL_BY_HMS(Lists.newArrayList("time:hms", "date:yyyy-MM-dd HH:mm:ss"),
            "按时分秒", Lists.newArrayList(DatabaseTypeEnum.MYSQL),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y-%m-%d %H:%i:%S') END)";
        }
    },
    /**
     * mysql 按日
     */
    MYSQL_BY_DAY(Lists.newArrayList("time:day", "date:yyyy-MM-dd"),
            "按日",
            Lists.newArrayList(DatabaseTypeEnum.MYSQL),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y-%m-%d') END)";
        }
    },
    /**
     * mysql 按月
     */
    MYSQL_BY_MONTH(Lists.newArrayList("time:month", "date:yyyy-MM"),
            "按月",
            Lists.newArrayList(DatabaseTypeEnum.MYSQL),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y-%m') END)";
        }
    },
    /**
     * mysql 按年
     */
    MYSQL_BY_YEAR(Lists.newArrayList("time:year", "date:yyyy"),
            "按年",
            Lists.newArrayList(DatabaseTypeEnum.MYSQL),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y') END)";
        }
    },
    /**
     * MaxCompute 按时分秒
     */
    MAXCOMPUTE_BY_HMS(Lists.newArrayList("time:hms", "date:yyyy-MM-dd HH:mm:ss"),
            "按时分秒",
            Lists.newArrayList(DatabaseTypeEnum.MaxCompute),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm-dd hh:mi:ss') END)";
        }
    },
    /**
     * MaxCompute、LIGHTNING 按日
     */
    MAXCOMPUTE_BY_DAY(Lists.newArrayList("time:day", "date:yyyy-MM-dd"),
            "按日",
            Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm-dd') END)";
        }
    },
    /**
     * MaxCompute、LIGHTNING 按月
     */
    MAXCOMPUTE_BY_MONTH(Lists.newArrayList("time:month", "date:yyyy-MM"),
            "按月",
            Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE, MetadataColumnTypeEnum.STRING),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm') END)";
        }
    },
    /**
     * MaxCompute、LIGHTNING 按年
     */
    MAXCOMPUTE_BY_YEAR(Lists.newArrayList("time:year", "date:yyyy"),
            "按年",
            Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy') END)";
        }
    },
    /**
     * lightning 按时分秒
     */
    LIGHTNING_BY_HMS(Lists.newArrayList("time:hms", "date:yyyy-MM-dd HH:mm:ss"),
            "按时分秒",
            Lists.newArrayList(DatabaseTypeEnum.LIGHTNING),
            Lists.newArrayList(MetadataColumnTypeEnum.DATE),
            MetadataColumnTypeEnum.STRING) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm-dd hh24:mi:ss') END)";
        }
    };

    /**
     * 转换
     */
    private List<String> conversions;
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
     * 转换后的类型
     */
    private MetadataColumnTypeEnum resultTypeEnum;

    /**
     * 转换列
     */
    public String convertColumn(String column) {
        return column;
    }

    /**
     * 根据转换和数据类型获取条件转换枚举
     *
     * @param conversion   转换
     * @param databaseType 数据库类型
     * @return 条件转换枚举
     */
    public static ConditionConversionEnum getConditionConversion(String conversion, DatabaseTypeEnum databaseType, MetadataColumnTypeEnum columnTypeEnum) {
        for (ConditionConversionEnum conversionEnum : ConditionConversionEnum.values()) {
            if (conversionEnum.getConversions().contains(conversion)
                    && conversionEnum.getDatabaseTypeEnums().contains(databaseType)
                    && conversionEnum.getColumnTypeEnums().contains(columnTypeEnum)) {
                return conversionEnum;
            }
        }
        return null;
    }

    ConditionConversionEnum(List<String> conversions, String remark, List<DatabaseTypeEnum> databaseTypeEnums, List<MetadataColumnTypeEnum> columnTypeEnums, MetadataColumnTypeEnum resultTypeEnum) {
        this.conversions = conversions;
        this.remark = remark;
        this.databaseTypeEnums = databaseTypeEnums;
        this.columnTypeEnums = columnTypeEnums;
        this.resultTypeEnum = resultTypeEnum;
    }

    public List<String> getConversions() {
        return conversions;
    }

    public String getRemark() {
        return remark;
    }

    public List<DatabaseTypeEnum> getDatabaseTypeEnums() {
        return databaseTypeEnums;
    }

    public List<MetadataColumnTypeEnum> getColumnTypeEnums() {
        return columnTypeEnums;
    }

    public MetadataColumnTypeEnum getResultTypeEnum() {
        return resultTypeEnum;
    }
}
