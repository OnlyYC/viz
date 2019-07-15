package com.liaoyb.viz.engine.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 过滤参数组
 *
 * @author liaoyanbo
 */
@Data
public class FilterParamGroup {
    /**
     * 参数关系（且、或）
     */
    @NotBlank(message = "参数关系不能为空")
    @Pattern(regexp = "and|or", message = "参数关系不合法")
    private String relation;

    /**
     * 查询参数
     */
    @Valid
    private List<FilterParam> filterParams;

    /**
     * 表达式
     */
    private String sqlExpression;
}
