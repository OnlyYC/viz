package com.liaoyb.viz.engine.queryadapter.expert_perform_query;

import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;

import lombok.Data;

/**
 * 表达式列(只能针对已有数据模型，简单的加、减、乘、除，传入进来会先进行校验)
 *
 * @author liaoyanbo
 * @date 2019-07-09 16:35
 */
@Data
public class ExpressionColumn {
    /**
     * 表达式列名
     */
    private String columnName;
    /**
     * 字段表达式（ concat(id,ware_name)等）
     */
    private String columnExpression;

    /**
     * 表达式列类型
     */
    private MetadataColumnTypeEnum columnTypeEnum;
}
