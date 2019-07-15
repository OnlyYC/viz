package com.liaoyb.viz.engine.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 过滤组
 *
 * @author liaoyanbo
 */
@Data
public class FilterGroup {
    /**
     * 参数关系（且、或）
     */
    @NotBlank(message = "参数关系不能为空")
    @Pattern(regexp = "and|or", message = "参数关系不合法")
    private String relation;

    /**
     * 参数组
     */
    @Valid
    private List<FilterParamGroup> filterParamGroups;
}
