package com.liaoyb.viz.engine.source;

import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;

/**
 * mysql数据源
 *
 * @author liaoyanbo
 */
public class MySqlDataSource extends AbstractJdbcDataSource {
    public MySqlDataSource() {
        this.setDatabaseTypeEnum(DatabaseTypeEnum.MYSQL);
    }
}
