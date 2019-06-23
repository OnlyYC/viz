package com.liaoyb.viz.engine.enums;

import com.google.common.collect.Lists;
import com.liaoyb.viz.engine.errors.NotImplementedException;

import java.util.List;

/**
 * 显示转换枚举
 *
 * @author liaoyanbo
 * @date 2019-03-26 10:29
 */
public enum DisplayConversionEnum {
    //日期=====================================================================================
    /**
     * mysql 按时分秒
     */
    MYSQL_BY_HMS(Lists.newArrayList("time:hms", "date:yyyy-MM-dd HH:mm:ss"), "按时分秒", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y-%m-%d %H:%i:%S') END)";
        }
    },
    /**
     * mysql 按日
     */
    MYSQL_BY_DAY(Lists.newArrayList("time:day", "date:yyyy-MM-dd"), "按日", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y-%m-%d') END)";
        }
    },
    /**
     * mysql 按月
     */
    MYSQL_BY_MONTH(Lists.newArrayList("time:month", "date:yyyy-MM"), "按月", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y-%m') END)";
        }
    },
    /**
     * mysql 按年
     */
    MYSQL_BY_YEAR(Lists.newArrayList("time:year", "date:yyyy"), "按年", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN DATE_FORMAT(" + column + ", '%Y-%m-%d')='1970-01-01' THEN '' ELSE DATE_FORMAT(" + column + ", '%Y') END)";
        }
    },
    /**
     * MaxCompute 按时分秒
     */
    MAXCOMPUTE_BY_HMS(Lists.newArrayList("time:hms", "date:yyyy-MM-dd HH:mm:ss"), "按时分秒", Lists.newArrayList(DatabaseTypeEnum.MaxCompute), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm-dd hh:mi:ss') END)";
        }
    },
    /**
     * MaxCompute、LIGHTNING 按日
     */
    MAXCOMPUTE_BY_DAY(Lists.newArrayList("time:day", "date:yyyy-MM-dd"), "按日", Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm-dd') END)";
        }
    },
    /**
     * MaxCompute、LIGHTNING 按月
     */
    MAXCOMPUTE_BY_MONTH(Lists.newArrayList("time:month", "date:yyyy-MM"), "按月", Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm') END)";
        }
    },
    /**
     * MaxCompute、LIGHTNING 按年
     */
    MAXCOMPUTE_BY_YEAR(Lists.newArrayList("time:year", "date:yyyy"), "按年", Lists.newArrayList(DatabaseTypeEnum.MaxCompute, DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy') END)";
        }
    },
    /**
     * lightning 按时分秒
     */
    LIGHTNING_BY_HMS(Lists.newArrayList("time:hms", "date:yyyy-MM-dd HH:mm:ss"), "按时分秒", Lists.newArrayList(DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.DATE)) {
        @Override
        public String convertColumn(String column) {
            return "(CASE WHEN TO_CHAR(" + column + ", 'yyyy-mm-dd')='1970-01-01' THEN '' ELSE TO_CHAR(" + column + ", 'yyyy-mm-dd hh24:mi:ss') END)";
        }
    },


    //转换为字符串==============================================================================
    /**
     * MaxCompute字符串转换
     */
    MAXCOMPUTE_STRING(Lists.newArrayList("string"), "字符串格式化", Lists.newArrayList(DatabaseTypeEnum.MaxCompute), Lists.newArrayList(MetadataColumnTypeEnum.STRING)) {
        @Override
        public String convertColumn(String column) {
            return "TO_CHAR(" + column + ")";
        }
    },
    /**
     * MYSQL字符串转换
     */
    MAXCOMPUTE_MYSQL(Lists.newArrayList("string"), "字符串格式化", Lists.newArrayList(DatabaseTypeEnum.MYSQL), Lists.newArrayList(MetadataColumnTypeEnum.NUMBER)) {
        @Override
        public String convertColumn(String column) {
            return "CONCAT(" + column + ")";
        }
    },
    /**
     * lightning字符串转换
     */
    LIGHTNING_MYSQL(Lists.newArrayList("string"), "字符串格式化", Lists.newArrayList(DatabaseTypeEnum.LIGHTNING), Lists.newArrayList(MetadataColumnTypeEnum.NUMBER)) {
        @Override
        public String convertColumn(String column) {
            return "''||" + column + "";
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
     * 转换列
     */
    public String convertColumn(String column) {
        throw new NotImplementedException(this.name() + "显示转换未实现");
    }

    /**
     * 根据转换和数据类型获取显示转换枚举
     *
     * @param conversion   转换
     * @param databaseType 数据库类型
     * @return 显示转换枚举
     */
    public static DisplayConversionEnum getDisplayConversion(String conversion, DatabaseTypeEnum databaseType, MetadataColumnTypeEnum columnTypeEnum) {
        for (DisplayConversionEnum conversionEnum : DisplayConversionEnum.values()) {
            if (conversionEnum.getConversions().contains(conversion)
                    && conversionEnum.getDatabaseTypeEnums().contains(databaseType)
                    && conversionEnum.getColumnTypeEnums().contains(columnTypeEnum)) {
                return conversionEnum;
            }
        }
        return null;
    }

    DisplayConversionEnum(List<String> conversions, String remark, List<DatabaseTypeEnum> databaseTypeEnums, List<MetadataColumnTypeEnum> columnTypeEnums) {
        this.conversions = conversions;
        this.remark = remark;
        this.databaseTypeEnums = databaseTypeEnums;
        this.columnTypeEnums = columnTypeEnums;
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
}
