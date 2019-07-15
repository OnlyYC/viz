package com.liaoyb.viz.engine.domain;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

/**
 * 执行参数
 *
 * @author liaoyanbo
 */
@Data
public class PerformParam implements QueryParam<PerformQueryResult> {
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
     * 条数限制（-1无限制）
     */
    private Integer limit;
}
