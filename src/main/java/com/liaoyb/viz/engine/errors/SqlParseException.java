package com.liaoyb.viz.engine.errors;

/**
 * sql解析异常
 *
 * @author liaoyb
 */
public class SqlParseException extends RuntimeException {
    public SqlParseException(String message) {
        super(message);
    }

    public SqlParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
