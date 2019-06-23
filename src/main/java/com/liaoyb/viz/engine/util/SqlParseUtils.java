package com.liaoyb.viz.engine.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.liaoyb.viz.engine.config.Consts;
import com.liaoyb.viz.engine.errors.SqlParseException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * sql解析工具
 *
 * @author liaoyanbo
 */
@Slf4j
public class SqlParseUtils {


    private static final char ST_START_CHAR = '{';

    private static final char ST_END_CHAR = '}';

    private static final String REG_SQL_STRUCT = "[{].*[}]";

    private static final String SELECT = "select";

    private static final String WITH = "with";

    private static final String REG_PLACEHOLDER = "\\$.+\\$";

    private static final String REG_TEAMVAR = "\\([a-zA-Z0-9_.-]{1,}\\s?\\w*[<>!=]*\\s?\\(?%s\\w+%s\\)?\\s?\\)";


    public static List<String> getQuerySqlList(String sql) {
        sql = sql.trim();
        if (StringUtils.isEmpty(sql)) {
            return null;
        }

        if (sql.startsWith(Consts.SQL_SEPARATOR)) {
            sql = sql.substring(1);
        }

        if (sql.endsWith(Consts.SQL_SEPARATOR)) {
            sql = sql.substring(0, sql.length() - 1);
        }

        List<String> list = null;
        String[] split = sql.split(Consts.SQL_SEPARATOR);
        if (null != split && split.length > 0) {
            list = new ArrayList<>();
            for (String sqlStr : split) {
                sqlStr = sqlStr.trim();
                if (sqlStr.toLowerCase().startsWith(SELECT) || sqlStr.toLowerCase().startsWith(WITH)) {
                    list.add(sqlStr);
                } else {
                    continue;
                }
            }
        }
        return list;
    }


    public static void main(String[] args) {
        Set<String> expSet = Sets.newHashSet("status >= $status$", "name between $name1$ and $name2$");
        Map<String, List<String>> teamParamMap = Maps.newHashMap();
        teamParamMap.put("$status$", Lists.newArrayList("0", "2"));
        teamParamMap.put("$ids$", Lists.newArrayList("0", "1"));
        teamParamMap.put("$name1$", Lists.newArrayList("0"));
        teamParamMap.put("$name2$", Lists.newArrayList("1"));


        //条件解析
        List<String> sqlCondition = Lists.newArrayList("status >= status and (id in (ids))", "name between $name1$ and $name2$");
    }


    public static char getSqlTempDelimiter(String sqlTempDelimiter) {
        return sqlTempDelimiter.charAt(sqlTempDelimiter.length() - 1);
    }


    /**
     * 单引号
     */
    public static String formatterSingleQuotes(String obj) {
        return String.format("'%s'", obj.toString());
    }

    /**
     * 检查sql条件
     *
     * @param condition sql条件
     */
    public static void checkCondition(String condition) {
        String sql = String.format(Consts.SELECT_EXEPRESSION, condition);
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
            throw new SqlParseException("检查sql条件失败:" + condition, e);
        }
    }
}
