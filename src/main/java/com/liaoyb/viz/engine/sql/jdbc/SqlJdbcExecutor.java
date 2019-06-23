package com.liaoyb.viz.engine.sql.jdbc;

import com.liaoyb.viz.engine.config.Consts;
import com.liaoyb.viz.engine.domain.QueryColumn;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.enums.TypeEnum;
import com.liaoyb.viz.engine.errors.DataHandlerException;
import com.liaoyb.viz.engine.errors.SourceException;
import com.liaoyb.viz.engine.errors.SqlExecutionException;
import com.liaoyb.viz.engine.source.AbstractJdbcSource;
import com.liaoyb.viz.engine.util.SqlUtils;
import com.liaoyb.viz.security.DomainUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql执行器
 *
 * @author liaoyanbo
 */
@Slf4j
@Component
public class SqlJdbcExecutor {
    /**
     * 列注释
     */
    public static final String RESULTSET_COLUMN_REMARKS = "REMARKS";
    /**
     * jdbc类型
     */
    public static final String RESULTSET_COLUMN_JDBC_TYPE = "DATA_TYPE";
    /**
     * 列名的结果集下标
     */
    private static final int RESULTSET_INDEX_COLUMN_NAME = 4;
    /**
     * 列类型的结果集下标
     */
    private static final int RESULTSET_INDEX_COLUMN_TYPE = 6;

    @Autowired
    private JdbcDataSource jdbcDataSource;

    /**
     * 敏感sql操作
     */
    private static Pattern SENSITIVE_SQL_PATTERN = Pattern.compile(Consts.REG_SENSITIVE_SQL);

    public void execute(AbstractJdbcSource source, String sql) throws DataHandlerException {
        checkSensitiveSql(sql);
        long startTime = System.nanoTime();
        try {
            jdbcTemplate(source).execute(sql);
            long estimatedTime = System.nanoTime() - startTime;
            log.info("【{}ms】execute sql:[{}] in {}:{}", estimatedTime / 1000000, sql, source.getJdbcConfig().getUsername(), source.getJdbcConfig().getUrl());
        } catch (Exception e) {
            long estimatedTime = System.nanoTime() - startTime;
            log.error("【{}ms】execute sql error:[{}] in {}:{}", estimatedTime / 1000000, sql, source.getJdbcConfig().getUsername(), source.getJdbcConfig().getUrl());
            throw new DataHandlerException(e.getMessage());
        }
    }

    /**
     * 执行sql，并转换为指定类型
     *
     * @param sql   sql
     * @param clazz 类型
     */
    public <T> T queryForObject(AbstractJdbcSource source, String sql, Class<T> clazz) {
        checkSensitiveSql(sql);
        sql = SqlUtils.formatSql(sql);
        long startTime = System.nanoTime();
        try {
            T result = jdbcTemplate(source).queryForObject(sql, clazz);
            long estimatedTime = System.nanoTime() - startTime;
            log.info("【{}ms】execute sql:[{}] in {}:{}", estimatedTime / 1000000, sql, source.getJdbcConfig().getUsername(), source.getJdbcConfig().getUrl());
            return result;
        } catch (Exception e) {
            long estimatedTime = System.nanoTime() - startTime;
            log.error("【{}ms】execute sql error:[{}] in {}:{}", estimatedTime / 1000000, sql, source.getJdbcConfig().getUsername(), source.getJdbcConfig().getUrl());
            throw new SqlExecutionException(e.getMessage());
        }
    }

    /**
     * 执行sql，返回集合
     *
     * @param sql   sql
     * @param limit 限制条数
     * @return 集合数据
     */
    public List<Map<String, Object>> query4List(AbstractJdbcSource source, String sql, int limit) {
        checkSensitiveSql(sql);
        sql = SqlUtils.formatSql(sql);
        List<Map<String, Object>> list = null;
        long startTime = System.nanoTime();
        try {
            JdbcTemplate jdbcTemplate = jdbcTemplate(source);
            jdbcTemplate.setMaxRows(limit);
            list = jdbcTemplate.query(sql, new DateColumnMapRowMapper());
            long estimatedTime = System.nanoTime() - startTime;
            log.info("【{}ms】execute sql:[{}] in {}:{}", estimatedTime / 1000000, sql, source.getJdbcConfig().getUsername(), source.getJdbcConfig().getUrl());
        } catch (Exception e) {
            long estimatedTime = System.nanoTime() - startTime;
            log.error("【{}ms】execute sql error:[{}] in {}:{}", estimatedTime / 1000000, sql, source.getJdbcConfig().getUsername(), source.getJdbcConfig().getUrl());
            throw new SqlExecutionException(e.getMessage());
        }
        return list;
    }

    /**
     * 查询返回集合列表
     *
     * @param sql sql
     */
    public List<Map<String, Object>> syncQuery4List(AbstractJdbcSource source, String sql) {
        List<Map<String, Object>> list = query4List(source, sql, -1);
        return list;
    }

    /**
     * 执行sql返回map
     *
     * @param sql sql
     */
    public Map<String, Object> query4Map(AbstractJdbcSource source, String sql) {
        checkSensitiveSql(sql);
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate(source).queryForMap(sql);
        } catch (Exception e) {
            throw new SqlExecutionException(e.getMessage());
        }
        return map;
    }


    /**
     * 判断表是否存在
     */
    public boolean tableIsExist(AbstractJdbcSource source, String tableName) throws SourceException {
        boolean result = false;
        Connection connection = null;
        try {
            connection = getConnection(source);
            if (null != connection) {
                ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
                if (null != tables && tables.next()) {
                    result = true;
                } else {
                    result = false;
                }
                tables.close();
            }
        } catch (Exception e) {
            throw new SourceException("Get connection meta data error, jdbcUrl=" + source.getJdbcConfig().getUrl());
        } finally {
            releaseConnection(connection);
        }

        return result;
    }

    /**
     * 根据sql查询列
     */
    public List<QueryColumn> getColumns(AbstractJdbcSource source, String sql) throws DataHandlerException {
        checkSensitiveSql(sql);
        Connection connection = null;
        List<QueryColumn> columnList = new ArrayList<>();
        try {
            connection = getConnection(source);
            if (null != connection) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnType = TypeEnum.getType(rsmd.getColumnType(i));
                    QueryColumn queryColumn = new QueryColumn(
                            rsmd.getColumnLabel(i),
                            columnType, columnType, null);
                    columnList.add(queryColumn);
                }
                resultSet.close();
                statement.close();
            }
        } catch (Exception e) {
            throw new DataHandlerException("获取sql列属性异常：" + e.getMessage());
        } finally {
            releaseConnection(connection);
        }
        return columnList;
    }

    /**
     * 根据表名获取表元数据
     */
    public List<QueryColumn> getColumnsByTableName(AbstractJdbcSource source, String tableName) throws DataHandlerException {
        Connection connection = null;
        List<QueryColumn> columnList = new ArrayList<>();
        try {
            connection = getConnection(source);
            if (null != connection) {
                DatabaseMetaData metaData = connection.getMetaData();
                columnList = getColumns(connection, tableName, metaData);
            }
        } catch (Exception e) {
            throw new SourceException(e.getMessage() + ", jdbcUrl=" + source.getJdbcConfig().getUrl());
        } finally {
            releaseConnection(connection);
        }
        return columnList;
    }


    /**
     * 获取数据表主键
     */
    private List<String> getPrimaryKeys(AbstractJdbcSource source, String tableName, DatabaseMetaData metaData) throws DataHandlerException {
        ResultSet rs = null;
        List<String> primaryKeys = new ArrayList<>();
        try {
            rs = metaData.getPrimaryKeys(null, null, tableName);
            while (rs.next()) {
                primaryKeys.add(rs.getString(4));
            }
        } catch (Exception e) {
            throw new DataHandlerException(e.getMessage());
        } finally {
            closeResult(rs);
        }
        return primaryKeys;
    }


    /**
     * 获取数据表列
     */
    private List<QueryColumn> getColumns(Connection connection, String tableName, DatabaseMetaData metaData) throws DataHandlerException {
        ResultSet rs = null;
        List<QueryColumn> columnList = new ArrayList<>();
        try {
            rs = metaData.getColumns(null, null, tableName, "%");
            while (rs.next()) {
                String comment = rs.getString(RESULTSET_COLUMN_REMARKS);
                //jdbc类型
                String jdbcType = TypeEnum.getType(rs.getInt(RESULTSET_COLUMN_JDBC_TYPE));
                String originalType = rs.getString(RESULTSET_INDEX_COLUMN_TYPE);
                columnList.add(new QueryColumn(rs.getString(RESULTSET_INDEX_COLUMN_NAME), originalType, jdbcType, Optional.ofNullable(comment).orElse("")));
            }
        } catch (Exception e) {
            throw new DataHandlerException(e.getMessage());
        } finally {
            closeResult(rs);
        }
        return columnList;
    }


    /**
     * 获取数据源
     */
    private DataSource getDataSource(DatabaseTypeEnum databaseTypeEnum, String jdbcUrl, String userename, String password) throws SourceException {
        return jdbcDataSource.getDataSource(databaseTypeEnum, jdbcUrl, userename, password);
    }

    /**
     * 检查敏感操作
     */
    private void checkSensitiveSql(String sql) throws DataHandlerException {
        Matcher matcher = SENSITIVE_SQL_PATTERN.matcher(sql.toLowerCase());
        if (matcher.find()) {
            String group = matcher.group();
            throw new DataHandlerException("Sensitive SQL operations are not allowed: " + group.toUpperCase());
        }
    }

    private Connection getConnection(AbstractJdbcSource source) throws SourceException {
        DataSource dataSource = getDataSource(source.getDatabaseTypeEnum(), source.getJdbcConfig().getUrl(), source.getJdbcConfig().getUsername(), source.getJdbcConfig().getPassword());
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            log.error("create connection error, jdbcUrl: {}", source.getJdbcConfig().getUrl());
            throw new SourceException("create connection error, jdbcUrl: " + source.getJdbcConfig().getUrl());
        }
        return connection;
    }

    private void releaseConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                log.error("connection close error", e.getMessage());
            }
        }
    }


    public static void closeResult(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean testConnection(AbstractJdbcSource source) throws SourceException {
        Connection connection = null;
        try {
            connection = getConnection(source);
            if (null != connection) {
                return true;
            } else {
                return false;
            }
        } catch (SourceException sourceException) {
            throw sourceException;
        } finally {
            releaseConnection(connection);
        }
    }

    public JdbcTemplate jdbcTemplate(AbstractJdbcSource source) throws SourceException {
        DataSource dataSource = getDataSource(source.getDatabaseTypeEnum(), source.getJdbcConfig().getUrl(), source.getJdbcConfig().getUsername(), source.getJdbcConfig().getPassword());
        return new JdbcTemplate(dataSource);
    }


}
