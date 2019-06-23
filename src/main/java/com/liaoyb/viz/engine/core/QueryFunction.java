package com.liaoyb.viz.engine.core;

import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.source.Source;

import java.util.List;
import java.util.Map;

/**
 * 查询
 *
 * @author liaoyb
 */
public interface QueryFunction {
    /**
     * 是否支持当前数据源类型
     *
     * @param source 数据源
     * @return 是否支持当前数据源类型
     */
    boolean support(Source source);

    /**
     * 获取数据
     *
     * @param source 数据源
     * @param performParam 查询参数
     * @return 数据
     */
    List<Map<String, Object>> getData(Source source, PerformParam performParam);

}
