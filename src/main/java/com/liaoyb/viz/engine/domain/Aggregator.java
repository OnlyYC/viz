package com.liaoyb.viz.engine.domain;

import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 聚合
 *
 * @author liaoyb
 */
@Data
public class Aggregator {
    @NotBlank(message = "聚合列不能为空")
    private String column;
    /**
     * 别名
     */
    @NotBlank(message = "聚合别名不能为空")
    private String alias;
    /**
     * sum、avg、count、max、min等聚合函数
     */
    @NotBlank(message = "聚合函数不能为空")
    private String func;

    /**
     * 列类型
     */
    private MetadataColumnTypeEnum columnTypeEnum;
}