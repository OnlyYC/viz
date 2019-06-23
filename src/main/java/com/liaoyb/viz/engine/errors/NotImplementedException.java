package com.liaoyb.viz.engine.errors;

/**
 * 不支持
 *
 * @author liaoyb
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
