package com.glqdlt.help.keyvalueclient;

/**
 * @author Jhun
 * 2019-11-18
 */
public class KeyvaultConnectorError extends RuntimeException {
    public KeyvaultConnectorError(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
