package com.liaoyb.viz.engine.domain;

import java.util.List;

import lombok.Data;

/**
 * 查询结果
 *
 * @author liaoyanbo
 */
@Data
public class QueryResult<T> {
    /**
     * 数据
     */
    private List<T> data;
    /**
     * 结果列
     */
    private List<QueryColumn> columns;

    /**
     * 行数
     */
    private Long rowCount;
    /**
     * 总行数
     */
    private Long totalRowCount;
}
