package com.liaoyb.viz.engine.errors;

/**
 * sql执行异常
 *
 * @author liaoyanbo
 */
public class SqlExecutionException extends DataHandlerException {
    private static final long serialVersionUID = 1L;
    private static final String ERROR_MESSAGE = "sql执行异常:";

    public SqlExecutionException(String message) {
        super(ERROR_MESSAGE + message);
    }

    public SqlExecutionException(String message, Throwable cause) {
        super(ERROR_MESSAGE + message, cause);
    }
}
