package com.liaoyb.viz.engine.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 查询结果
 *
 * @author liaoyanbo
 */
@Data
public class PerformQueryResult {
    /**
     * 数据
     */
    private List<Map<String, Object>> data;
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
