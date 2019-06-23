package com.liaoyb.viz.engine.core;

import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.errors.NotSupportException;
import com.liaoyb.viz.engine.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 查询管理(入口)
 *
 * @author liaoyb
 */
@Component
public class QueryManager {
    @Autowired
    private Validator globalValidator;

    @Autowired
    private List<QueryFunction> queryFunctionList;

    /**
     * 获取数据
     *
     * @param source       数据源
     * @param performParam 查询参数
     * @return 数据
     */
    public List<Map<String, Object>> getData(Source source, PerformParam performParam) {
        //检查执行参数
        check(performParam);

        //获取匹配的查询引擎
        QueryFunction queryEngine = getMatchQueryEngine(source);
        if (queryEngine == null) {
            throw new NotSupportException("不支持的数据源类型:" + source.getClass().getName());
        }

        //获取数据
        return queryEngine.getData(source, performParam);
    }

    /**
     * 检查执行参数
     *
     * @param performParam
     */
    private void check(PerformParam performParam) {
        //todo valid，抛出异常
        Set<ConstraintViolation<PerformParam>> set = globalValidator.validate(performParam);
        for (ConstraintViolation<PerformParam> constraintViolation : set) {
            System.out.println(constraintViolation.getMessage());
        }
    }


    /**
     * 获取匹配的查询引擎
     */
    private QueryFunction getMatchQueryEngine(Source source) {
        return queryFunctionList.stream()
                .filter(dataHandler -> dataHandler.support(source))
                .findFirst()
                .orElse(null);
    }

}
