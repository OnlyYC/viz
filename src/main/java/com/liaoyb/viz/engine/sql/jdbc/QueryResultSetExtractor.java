package com.liaoyb.viz.engine.sql.jdbc;

import com.liaoyb.viz.engine.domain.QueryColumn;
import com.liaoyb.viz.engine.domain.QueryResult;
import com.liaoyb.viz.engine.enums.TypeEnum;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * sql查询结果抽取
 *
 * @author liaoyanbo
 */
public class QueryResultSetExtractor<T> implements ResultSetExtractor<QueryResult<T>> {
    private final RowMapper<T> rowMapper;
    private final int rowsExpected;

    public QueryResultSetExtractor(RowMapper<T> rowMapper) {
        this(rowMapper, 0);
    }

    public QueryResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
    }

    @Override
    public QueryResult<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        //提取结果
        List<T> results = (this.rowsExpected > 0 ? new ArrayList<>(this.rowsExpected) : new ArrayList<>());
        int rowNum = 0;
        while (rs.next()) {
            results.add(this.rowMapper.mapRow(rs, rowNum++));
        }

        //提取列
        List<QueryColumn> columnList = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnType = TypeEnum.getType(rsmd.getColumnType(i));
            QueryColumn queryColumn = new QueryColumn(
                rsmd.getColumnLabel(i),
                columnType, columnType, null);
            columnList.add(queryColumn);
        }

        QueryResult<T> queryResult = new QueryResult<T>();
        queryResult.setData(results);
        queryResult.setColumns(columnList);
        return queryResult;
    }
}
