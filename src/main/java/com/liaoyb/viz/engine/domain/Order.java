package com.liaoyb.viz.engine.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * @author liaoyanbo
 */
@Data
public class Order {
    @NotBlank(message = "排序列不能为空")
    private String column;
    /**
     * 排序方向asc、desc
     */
    @NotBlank(message = "排序方向不能为空")
    @Pattern(regexp = "ASC|asc|DESC|desc", message = "排序方向不合法")
    private String direction = "ASC";

    /**
     * sum、avg、count、countdistinct、max、min等聚合函数
     */
    private String func;
}
