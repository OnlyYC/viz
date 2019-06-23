package com.liaoyb.viz.engine.core.mysql;

import com.liaoyb.viz.engine.core.AbstractJdbcQueryEngine;
import com.liaoyb.viz.engine.domain.PerformParam;
import com.liaoyb.viz.engine.source.MySqlSource;
import com.liaoyb.viz.engine.source.Source;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * mysql查询引擎
 *
 * @author liaoyb
 */
@Component
public class MySqlQueryEngine extends AbstractJdbcQueryEngine {

    @Override
    public boolean support(Source source) {
        Assert.notNull(source);
        return source instanceof MySqlSource;
    }

    @Override
    public List<Map<String, Object>> getData(Source source, PerformParam performParam) {
        MySqlSource mySqlSource = (MySqlSource) source;

        return getData(mySqlSource, performParam);
    }
}
