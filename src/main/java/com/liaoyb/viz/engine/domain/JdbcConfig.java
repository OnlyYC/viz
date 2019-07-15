package com.liaoyb.viz.engine.domain;

import lombok.Builder;
import lombok.Data;

/**
 * jdbc连接信息
 *
 * @author liaoyanbo
 */
@Builder
@Data
public class JdbcConfig {
    /**
     * url
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
