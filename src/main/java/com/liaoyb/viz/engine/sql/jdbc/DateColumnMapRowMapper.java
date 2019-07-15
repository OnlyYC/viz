package com.liaoyb.viz.engine.sql.jdbc;

import com.liaoyb.viz.engine.util.DateUtils;

import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 行映射器
 *
 * @author liaoyanbo
 */
public class DateColumnMapRowMapper extends ColumnMapRowMapper {
    @Override
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        Object columnValue = super.getColumnValue(rs, index);
        return convertValue(columnValue);
    }

    /**
     * 转换值
     *
     * @param columnValue 值
     * @return 转换值
     */
    private Object convertValue(Object columnValue) {
        //转换日期类型，转换为LocalDateTime
        if (columnValue instanceof Timestamp) {
            Timestamp value = (Timestamp) columnValue;
            return DateUtils.toDateTime(value);
        } else if (columnValue instanceof Date) {
            Date value = (Date) columnValue;
            return DateUtils.toDateTime(value);
        } else {
            return columnValue;
        }
    }
}
