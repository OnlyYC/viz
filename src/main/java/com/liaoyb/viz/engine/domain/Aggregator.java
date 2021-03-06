package com.liaoyb.viz.engine.domain;

import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 聚合
 *
 * @author liaoyanbo
 */
@Data
public class Aggregator {
    /**
     * 可以是原始列、表达式列
     */
    @NotBlank(message = "聚合列不能为空")
    private String column;
    /**
     * sum、avg、count、max、min等聚合函数
     */
    @NotBlank(message = "聚合函数不能为空")
    private String func;
    /**
     * 列类型
     */
    private MetadataColumnTypeEnum columnTypeEnum;


    //todo 可以对自定义列聚合表达式，如（sum(id)+count(geg)）


    /**
     * 别名
     */
    @NotBlank(message = "聚合别名不能为空")
    private String alias;

}
