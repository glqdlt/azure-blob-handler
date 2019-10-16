package com.glqdlt.helper.handler;

/**
 * @author Jhun
 * 2019-10-16
 */
public class HandlerError extends RuntimeException {

    public HandlerError(String message) {
        super(message);
    }

    public HandlerError(String message, Throwable cause) {
        super(message, cause);
    }
}
