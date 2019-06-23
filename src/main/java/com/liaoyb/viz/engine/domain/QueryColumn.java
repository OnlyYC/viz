package com.liaoyb.viz.engine.domain;

import lombok.Data;

/**
 * 数据库列
 *
 * @author liaoyanbo
 */
@Data
public class QueryColumn {
    /**
     * 列名称
     */
    private String name;
    /**
     * 数据库类类型
     */
    private String type;
    /**
     * jdbc类型
     */
    private String jdbcType;
    /**
     * 列注释
     */
    private String comment;

    public QueryColumn(String name, String type, String jdbcType, String comment) {
        this.name = name;
        this.type = type;
        this.jdbcType = jdbcType;
        this.comment = comment;
    }
}
