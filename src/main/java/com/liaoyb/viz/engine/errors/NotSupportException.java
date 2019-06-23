package com.liaoyb.viz.engine.errors;

/**
 * 不支持异常
 *
 * @author liaoyb
 */
public class NotSupportException extends RuntimeException {
    public NotSupportException(String message) {
        super(message);
    }

    public NotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
