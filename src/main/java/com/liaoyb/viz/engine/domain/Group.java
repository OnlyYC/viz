package com.liaoyb.viz.engine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 分组(column or 表达式)
 *
 * @author liaoyanbo
 */
@Data
public class Group {
    /**
     * 分组列（可以是表达式列(已经生成好的sql表达式)）
     */
    @NotBlank(message = "分组列名不能为空")
    private String column;

    /**
     * 别名
     */
    @NotBlank(message = "分组别名不能为空")
    private String alias;
    /**
     * 分组列转换（如日期按照年、月）
     */
    @JsonProperty("conversion")
    private String groupConversion;
    /**
     * 列类型
     */
    @NotNull(message = "列类型不能为空")
    private MetadataColumnTypeEnum columnTypeEnum;

}
