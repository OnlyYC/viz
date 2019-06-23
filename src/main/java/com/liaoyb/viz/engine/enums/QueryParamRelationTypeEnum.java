package com.liaoyb.viz.engine.enums;

import java.util.Objects;

/**
 * 查询参数关系类型枚举
 *
 * @author liaoyanbo
 */
public enum QueryParamRelationTypeEnum {
    /**
     * 且
     */
    AND("and", "且"),
    /**
     * 或
     */
    OR("or", "或");
    /**
     * 值
     */
    private final String value;
    /**
     * 描述
     */
    private final String description;

    QueryParamRelationTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static QueryParamRelationTypeEnum from(String value) {
        QueryParamRelationTypeEnum[] enums = QueryParamRelationTypeEnum.values();
        for (QueryParamRelationTypeEnum type : enums) {
            if (Objects.equals(type.getValue(), value)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
