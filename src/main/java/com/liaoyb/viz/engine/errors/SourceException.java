package com.liaoyb.viz.engine.errors;

/**
 * 数据源异常
 *
 * @author liaoyanbo
 */
public class SourceException extends DataHandlerException {
    private static final long serialVersionUID = 1L;
    private static final String ERROR_MESSAGE = "数据源异常:";

    public SourceException(String message) {
        super(ERROR_MESSAGE + message);
    }

    public SourceException(String message, Throwable cause) {
        super(ERROR_MESSAGE + message, cause);
    }
}
