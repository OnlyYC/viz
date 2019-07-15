package com.liaoyb.viz.engine.config;

/**
 * 常量配置
 *
 * @author liaoyanbo
 */
public class Consts {
    /**
     * 空格
     */
    public static final String SPACE = " ";
    /**
     * 分号
     */
    public static final String SQL_END = ";";

    public static final String SQL_SEPARATOR = ";";


    public static final String PARENTHESES_START = "(";

    public static final String PARENTHESES_END = ")";


    public static final char DOLLAR_DELIMITER = '$';

    public static final String ADD = "and";
    public static final String OR = "or";

    /**
     * 升序
     */
    public static final String DIRECTION_ASC = "asc";
    /**
     * 降序
     */
    public static final String DIRECTION_DESC = "desc";

    /**
     * 敏感sql操作
     */
    public static final String REG_SENSITIVE_SQL = "drop\\s|alert\\s|grant\\s|delete\\s|update\\s|remove\\s";
    /**
     * sql ST模板
     */
    public static final String SQL_TEMPLATE = "templates/sql/sqlTemplate.stg";

    /**
     * sql查询格式
     */
    public static final String INNER_SQL_QUERY_FORMAT = "(%s)";
    /**
     * select 表达式
     */
    public static final String SELECT_EXEPRESSION = "SELECT * FROM TABLE WHERE %s";
}
