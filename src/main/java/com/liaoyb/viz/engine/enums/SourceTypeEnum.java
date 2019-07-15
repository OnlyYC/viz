package com.liaoyb.viz.engine.enums;

import com.google.common.collect.Lists;

import java.util.Objects;

/**
 * 数据源类型枚举
 *
 * @author liaoyanbo
 */
public enum SourceTypeEnum {
    /**
     * mysql类型数据源
     */
    MYSQL("mysql", "mysql"),
    /**
     * 阿里云lightning数据源
     */
    LIGHTNING("lightning", "lightning");
    /**
     * 值
     */
    private final String value;
    /**
     * 描述
     */
    private final String description;

    SourceTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static SourceTypeEnum from(String value) {
        SourceTypeEnum[] enums = SourceTypeEnum.values();
        for (SourceTypeEnum type : enums) {
            if (Objects.equals(type.getValue(), value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 是否是jdbc类型数据源
     *
     * @param sourceTypeEnum 数据源
     * @return 是否是jdbc类型数据源
     */
    public static boolean isJdbcType(SourceTypeEnum sourceTypeEnum) {
        return Lists.newArrayList(SourceTypeEnum.MYSQL, SourceTypeEnum.LIGHTNING).contains(sourceTypeEnum);
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
