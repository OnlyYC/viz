package com.liaoyb.viz.engine.domain;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 过滤组
 *
 * @author liaoyb
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
