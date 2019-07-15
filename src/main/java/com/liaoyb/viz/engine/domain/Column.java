package com.liaoyb.viz.engine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author liaoyanbo
 */
@Data
public class Column {
    /**
     * 列名
     */
    @NotBlank(message = "列名不能为空")
    private String column;
    /**
     * 别名
     */
    @NotBlank(message = "列的别名不能为空")
    private String alias;
    /**
     * 显示转换(STR_TO_DATE:%Y-%m-%d)
     */
    @JsonProperty("conversion")
    private String displayConversion;
    /**
     * 列类型
     */
    @NotNull(message = "列类型不能为空")
    private MetadataColumnTypeEnum columnTypeEnum;
}
