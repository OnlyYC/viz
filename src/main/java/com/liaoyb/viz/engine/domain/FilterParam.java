package com.liaoyb.viz.engine.domain;

import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 过滤参数
 *
 * @author liaoyb
 */
@Data
public class FilterParam {
    /**
     * 过滤参数名
     */
    @NotBlank(message = "过滤参数名不能为空")
    private String param;
    /**
     * 条件转换
     */
    private String conditionConversion;
    /**
     * 列类型
     */
    @NotNull(message = "列类型不能为空")
    private MetadataColumnTypeEnum columnTypeEnum;
    /**
     * 操作类型
     */
    @NotBlank(message = "过滤参数操作类型不能为空")
    private String operation;
    /**
     * 值
     */
    private List<String> values;
}
