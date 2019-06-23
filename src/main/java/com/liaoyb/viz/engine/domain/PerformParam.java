package com.liaoyb.viz.engine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * 执行参数
 *
 * @author liaoyb
 */
@Data
public class PerformParam {
    /**
     * 分组
     */
    private List<Group> groups;
    /**
     * 聚合
     */
    @Valid
    private List<Aggregator> aggregators;

    /**
     * 过滤参数组
     */
    @Valid
    private List<FilterGroup> filterGroups;
    /**
     * 列
     */
    private List<Column> columns;

    /**
     * 排序
     */
    @Valid
    private List<Order> orders;

    /**
     * 行权限参数
     */
    private List<FilterGroup> rowAuths;

    /**
     * todo 行权限表达式
     */
    private String rowAuthExpression;

    /**
     * 每页条数
     */
    @JsonProperty("per_page")
    private Integer pageSize;
    /**
     * 当前页
     */
    @JsonProperty("current_page")
    private Integer curPage;
}
