package com.liaoyb.viz.engine.errors;

/**
 * 数据处理器异常父类
 *
 * @author liaoyanbo
 */
public class DataHandlerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataHandlerException() {
    }

    public DataHandlerException(String message) {
        super(message);
    }

    public DataHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataHandlerException(Throwable cause) {
        super(cause);
    }

    public DataHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
