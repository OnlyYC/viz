package com.liaoyb.viz.engine.util;

/**
 * sql工具
 *
 * @author liaoyanbo
 */
public class SqlUtils {

    /**
     * 格式化sql
     *
     * @param sql sql
     * @return 格式化后sql
     */
    public static String formatSql(String sql) {
        // 用空格替换/r/n
        sql = sql.replaceAll("(\r\n|\n)", " ");
        return sql;
    }

}
