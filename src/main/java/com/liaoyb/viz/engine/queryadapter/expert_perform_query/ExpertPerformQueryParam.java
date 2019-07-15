package com.liaoyb.viz.engine.queryadapter.expert_perform_query;

import java.util.List;

import lombok.Data;

/**
 * @author liaoyanbo
 * @date 2019-07-08 17:40
 */
@Data
public class ExpertPerformQueryParam {
    /**
     * 聚合
     */
    private List<Aggregation> aggregation;

    /**
     * 表达式列
     */
    private List<ExpressionColumn> expressionColumnList;

}
