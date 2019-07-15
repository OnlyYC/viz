package com.liaoyb.viz.engine.queryengine;

import com.liaoyb.viz.engine.domain.ExportParam;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.domain.QueryParam;
import com.liaoyb.viz.engine.errors.NotSupportException;
import com.liaoyb.viz.engine.queryadapter.ExportCallback;
import com.liaoyb.viz.engine.queryadapter.QueryAdapter;
import com.liaoyb.viz.engine.source.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

/**
 * 查询管理(入口)
 *
 * @author liaoyanbo
 */
@Component
public class QueryManager {
    @Autowired
    private Validator globalValidator;

    @Autowired
    private List<QueryAdapter> queryAdapterList;

    /**
     * 获取数据
     *
     * @param source     数据源
     * @param queryParam 查询参数
     * @return 数据
     */
    public <R> R getQueryData(DataSource source, QueryParam<R> queryParam) {
        //获取匹配的QueryAdapter
        QueryAdapter<QueryParam<R>, R> queryAdapter = getMatchQueryAdapter(source, queryParam);
        if (queryAdapter == null) {
            throw new NotSupportException(String.format("未找到匹配的QueryAdapter,source:【%s】,queryParam:【%s】", source, queryParam));
        }

        //获取数据
        return queryAdapter.getQueryData(source, queryParam);
    }


    public <R> void exportQueryData(DataSource source, QueryParam<R> queryParam, ExportParam exportParam, ExportCallback exportCallback) {
        //获取匹配的QueryAdapter
        QueryAdapter<QueryParam<R>, R> queryAdapter = getMatchQueryAdapter(source, queryParam);
        if (queryAdapter == null) {
            throw new NotSupportException(String.format("未找到匹配的QueryAdapter,source:【%s】,queryParam:【%s】", source, queryParam));
        }
        queryAdapter.exportQueryData(source, queryParam, exportParam, exportCallback);
    }

    /**
     * 获取匹配QueryAdapter
     */
    @SuppressWarnings("unchecked")
    private <R> QueryAdapter<QueryParam<R>, R> getMatchQueryAdapter(DataSource source, QueryParam<R> queryParam) {
        return queryAdapterList.stream()
            .filter(queryAdapter -> queryAdapter.support(source, queryParam))
            .findFirst()
            .orElse(null);
    }


    /**
     * 检查执行参数
     */
    private void check(PerformParam performParam) {
        //检查执行参数
        Set<ConstraintViolation<PerformParam>> set = globalValidator.validate(performParam);
        if (!CollectionUtils.isEmpty(set)) {
            throw new ConstraintViolationException(set);
        }
    }
}
