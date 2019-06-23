package com.liaoyb.viz.engine.core;

import com.google.common.collect.Lists;
import com.liaoyb.viz.VizApp;
import com.liaoyb.viz.engine.domain.Column;
import com.liaoyb.viz.engine.domain.FilterGroup;
import com.liaoyb.viz.engine.domain.FilterParam;
import com.liaoyb.viz.engine.domain.FilterParamGroup;
import com.liaoyb.viz.engine.domain.JdbcConfig;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.enums.DatabaseTypeEnum;
import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;
import com.liaoyb.viz.engine.source.MySqlSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author liaoyb
 */
@SpringBootTest(classes = VizApp.class)
public class QueryManagerTest {
    @Autowired
    private QueryManager queryManager;

    @Test
    public void testGetData() {
        JdbcConfig jdbcConfig = JdbcConfig.builder()
                .url("jdbc:mysql://localhost:3306/query_engine_data_test?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC")
                .username("root")
                .password("root")
                .build();
        MySqlSource mySqlSource = MySqlSource.newInstanceWithTableName(jdbcConfig, DatabaseTypeEnum.MYSQL, "user");


        PerformParam performParam = new PerformParam();

        Column idColumn = new Column();
        idColumn.setColumn("id");
        idColumn.setAlias("id");

        Column nameColumn = new Column();
        nameColumn.setColumn("name");
        nameColumn.setAlias("name");
        performParam.setColumns(Lists.newArrayList(idColumn, nameColumn));


        FilterParam filterParam = new FilterParam();
        filterParam.setParam("name");
        filterParam.setValues(Lists.newArrayList("涛逼"));
        filterParam.setOperation("equal");
//        filterParam.setColumnTypeEnum(MetadataColumnTypeEnum.STRING);

        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setRelation("and");

        FilterParamGroup filterParamGroup = new FilterParamGroup();
        filterParamGroup.setRelation("and");
        filterParamGroup.setFilterParams(org.assertj.core.util.Lists.newArrayList(filterParam));

        filterGroup.setFilterParamGroups(org.assertj.core.util.Lists.newArrayList(filterParamGroup));
        performParam.setFilterGroups(Lists.newArrayList(filterGroup));

        List<Map<String, Object>> data = queryManager.getData(mySqlSource, performParam);
        System.out.println(data);




    }
}
