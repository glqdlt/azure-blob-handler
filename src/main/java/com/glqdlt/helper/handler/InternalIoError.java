package com.glqdlt.helper.handler;

/**
 * @author Jhun
 * 2019-10-16
 */
public class InternalIoError extends HandlerError {
    public InternalIoError(String message) {
        super(message);
    }

    public InternalIoError(String message, Throwable cause) {
        super(message, cause);
    }
}
