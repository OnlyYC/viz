package com.liaoyb.viz.engine.queryadapter;

import com.liaoyb.viz.engine.domain.ExportParam;
import com.liaoyb.viz.engine.domain.QueryParam;
import com.liaoyb.viz.engine.source.DataSource;

/**
 * 查询适配器接口
 *
 * @author liaoyanbo
 */
public interface QueryAdapter<T extends QueryParam<R>, R> {
    /**
     * 是否支持当前数据源 & 参数
     *
     * @param source 数据源
     * @param queryParam 查询参数
     * @return 是否支持
     */
    boolean support(DataSource source, T queryParam);

    /**
     * 获取查询数据
     *
     * @param source 数据源
     * @param queryParam 查询参数
     * @return 查询结果
     */
    R getQueryData(DataSource source, T queryParam);

    /**
     * 导出(异步)
     *
     * 1、把查询结果写入文件
     * 2、回调方式通知进度、文件
     *
     * @param source 数据源
     * @param queryParam 查询参数
     * @param exportParam 导出参数
     * @param exportCallback 导出回调
     */
    void exportQueryData(DataSource source, T queryParam, ExportParam exportParam, ExportCallback exportCallback);
}
