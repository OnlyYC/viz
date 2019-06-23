package com.liaoyb.viz.engine.enums;

import java.util.Objects;

/**
 * @author liaoyb
 */
public enum MetadataColumnTypeEnum {
    /**
     * 字符串
     */
    STRING("string", "字符串"),
    /**
     * 数字
     */
    NUMBER("number", "数字"),
    /**
     * 时间
     */
    DATE("date", "时间"),
    /**
     * 布尔类型
     */
    BOOLEAN("boolean", "布尔类型"),
    /**
     * 其他(大文本、字节等)
     */
    OTHER("other", "其他(大文本、字节等)");
    /**
     * 值
     */
    private final String value;
    /**
     * 描述
     */
    private final String description;

    MetadataColumnTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static MetadataColumnTypeEnum from(String value) {
        MetadataColumnTypeEnum[] enums = MetadataColumnTypeEnum.values();
        for (MetadataColumnTypeEnum type : enums) {
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
