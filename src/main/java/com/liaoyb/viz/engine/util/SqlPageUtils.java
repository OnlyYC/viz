package com.liaoyb.viz.engine.util;

import com.google.common.collect.Lists;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.errors.SqlParseException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.io.StringReader;
import java.util.List;

/**
 * sql 分页工具
 *
 * @author liaoyb
 */
public enum SqlPageUtils {
    /**
     * mysql分页，使用limit进行分页
     */
    MYSQL(Lists.newArrayList(DatabaseTypeEnum.MYSQL)) {
        @Override
        public String getPageSql(String srcSql, int pageSize, int curPage) {
            String pageSqlTemplate = "select T.* from (%s) T limit %s,%s";

            return String.format(pageSqlTemplate, srcSql, (curPage - 1) * pageSize, pageSize);
        }
    },

    /**
     * LIGHTNING分页，使用limit进行分页
     */
    LIGHTNING(Lists.newArrayList(DatabaseTypeEnum.LIGHTNING)) {
        @Override
        public String getPageSql(String srcSql, int pageSize, int curPage) {
            String pageSqlTemplate = "select T.* from (%s) T limit %s offset %s";

            return String.format(pageSqlTemplate, srcSql, pageSize, (curPage - 1) * pageSize);
        }
    };

    private List<DatabaseTypeEnum> databaseTypeEnumList;

    SqlPageUtils(List<DatabaseTypeEnum> databaseTypeEnumList) {
        this.databaseTypeEnumList = databaseTypeEnumList;
    }

    public String getCountSql(String srcSql) {
        try {
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            net.sf.jsqlparser.statement.Statement parse = parserManager.parse(new StringReader(srcSql));

            if (parse instanceof Select) {
                Select select = (Select) parse;
                PlainSelect selectBody = (PlainSelect) select.getSelectBody();
                selectBody.setOrderByElements(null);

                String pageSqlTemplate = "select count(*) from (%s) T";
                return String.format(pageSqlTemplate, select.toString());
            } else {
                throw new SqlParseException(String.format("无法解析select语句,%s", srcSql));
            }
        } catch (JSQLParserException e) {
            throw new SqlParseException(String.format("转换为统计总数sql异常,%s", srcSql), e);
        }
    }

    public abstract String getPageSql(String srcSql, int pageSize, int curPage);


    /**
     * 获取匹配的分页工具
     *
     * @param databaseTypeEnum 数据库类型
     * @return 匹配的分页工具
     */
    public static SqlPageUtils getPageUtil(DatabaseTypeEnum databaseTypeEnum) {
        for (SqlPageUtils sqlPageUtil : values()) {
            if (sqlPageUtil.databaseTypeEnumList.contains(databaseTypeEnum)) {
                return sqlPageUtil;
            }
        }
        return null;
    }
}
