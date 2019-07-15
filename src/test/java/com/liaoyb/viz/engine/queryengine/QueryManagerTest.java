package com.liaoyb.viz.engine.queryengine;

import com.google.common.collect.Lists;

import com.liaoyb.viz.VizApp;
import com.liaoyb.viz.engine.domain.Aggregator;
import com.liaoyb.viz.engine.domain.ExportParam;
import com.liaoyb.viz.engine.domain.FilterGroup;
import com.liaoyb.viz.engine.domain.FilterParam;
import com.liaoyb.viz.engine.domain.FilterParamGroup;
import com.liaoyb.viz.engine.domain.Group;
import com.liaoyb.viz.engine.domain.JdbcConfig;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.domain.PerformQueryResult;
import com.liaoyb.viz.engine.enums.MetadataColumnTypeEnum;
import com.liaoyb.viz.engine.enums.SourceTypeEnum;
import com.liaoyb.viz.engine.queryadapter.ExportCallback;
import com.liaoyb.viz.engine.source.AbstractJdbcDataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 查询管理器测试
 *
 * @author liaoyanbo
 */
@SpringBootTest(classes = VizApp.class)
public class QueryManagerTest {
    @Autowired
    private QueryManager queryManager;


    @Test
    public void testGetQueryData() {
        JdbcConfig jdbcConfig = JdbcConfig.builder()
            .url("jdbc:mysql://192.168.0.212:3306/prism_report_data?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC")
            .username("root")
            .password("123456")
            .build();
        AbstractJdbcDataSource mySqlSource = AbstractJdbcDataSource.newInstanceWithTableName(jdbcConfig, SourceTypeEnum.MYSQL, "ware_stock");


        PerformParam performParam = new PerformParam();

        Group idGroup = new Group();
        idGroup.setColumn("CONCAT(id,ware_name)");
        idGroup.setAlias("id");

        Group nameGroup = new Group();
        nameGroup.setColumn("ware_name");
        nameGroup.setAlias("ware_name");
        performParam.setGroups(Lists.newArrayList(idGroup, nameGroup));

        Aggregator aggregator = new Aggregator();
        aggregator.setColumn("(CONCAT(id,ware_name))");
        aggregator.setFunc("count");
        aggregator.setAlias("co");
        aggregator.setColumnTypeEnum(MetadataColumnTypeEnum.STRING);
        performParam.setAggregators(Lists.newArrayList(aggregator));


        FilterParam filterParam = new FilterParam();
        filterParam.setParam("fromway");
        filterParam.setValues(Lists.newArrayList("iphone"));
        filterParam.setOperation("equal");
        filterParam.setColumnTypeEnum(MetadataColumnTypeEnum.STRING);

        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setRelation("and");

        FilterParamGroup filterParamGroup = new FilterParamGroup();
        filterParamGroup.setRelation("and");
        filterParamGroup.setFilterParams(org.assertj.core.util.Lists.newArrayList(filterParam));

        filterGroup.setFilterParamGroups(org.assertj.core.util.Lists.newArrayList(filterParamGroup));
        performParam.setFilterGroups(Lists.newArrayList(filterGroup));
        performParam.setLimit(2);

        PerformQueryResult performQueryResult = queryManager.getQueryData(mySqlSource, performParam);
        System.out.println(performQueryResult);
    }


    @Test
    public void testExport() throws InterruptedException {
        JdbcConfig jdbcConfig = JdbcConfig.builder()
            .url("jdbc:mysql://192.168.0.212:3306/prism_report_data?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC")
            .username("root")
            .password("123456")
            .build();
        AbstractJdbcDataSource mySqlSource = AbstractJdbcDataSource.newInstanceWithTableName(jdbcConfig, SourceTypeEnum.MYSQL, "ware_stock");


        PerformParam performParam = new PerformParam();

        Group idGroup = new Group();
        idGroup.setColumn("CONCAT(id,ware_name)");
        idGroup.setAlias("id");

        Group nameGroup = new Group();
        nameGroup.setColumn("ware_name");
        nameGroup.setAlias("ware_name");
        performParam.setGroups(Lists.newArrayList(idGroup, nameGroup));

        Aggregator aggregator = new Aggregator();
        aggregator.setColumn("(CONCAT(id,ware_name))");
        aggregator.setFunc("count");
        aggregator.setAlias("co");
        aggregator.setColumnTypeEnum(MetadataColumnTypeEnum.STRING);
        performParam.setAggregators(Lists.newArrayList(aggregator));


        FilterParam filterParam = new FilterParam();
        filterParam.setParam("fromway");
        filterParam.setValues(Lists.newArrayList("iphone"));
        filterParam.setOperation("equal");
        filterParam.setColumnTypeEnum(MetadataColumnTypeEnum.STRING);

        FilterGroup filterGroup = new FilterGroup();
        filterGroup.setRelation("and");

        FilterParamGroup filterParamGroup = new FilterParamGroup();
        filterParamGroup.setRelation("and");
        filterParamGroup.setFilterParams(org.assertj.core.util.Lists.newArrayList(filterParam));

        filterGroup.setFilterParamGroups(org.assertj.core.util.Lists.newArrayList(filterParamGroup));
        performParam.setFilterGroups(Lists.newArrayList(filterGroup));

        ExportParam exportParam = new ExportParam();
        exportParam.setExportTaskId("jklgelagke");
        exportParam.setFilename("geage");
        exportParam.setExportType("csv");

        ExportCallback exportCallback = new ExportCallback() {
            @Override
            public void doComplete(File file) {
                System.out.println("保存file到目标文件," + exportParam.getExportTaskId() + "导出任务id更新为完成");
            }

            @Override
            public void doUpdateProgress(float progressRatio) {
                System.out.println("更新数据库中文件导出任务进度：" + progressRatio);
            }

            @Override
            public void uncaughtException(Throwable e) {
                System.out.println("导出任务发生异常了：" + e);
            }
        };

        queryManager.exportQueryData(mySqlSource, performParam, exportParam, exportCallback);
        System.out.println("已提交导出任务，等待完成");
        TimeUnit.SECONDS.sleep(10);
    }
}
