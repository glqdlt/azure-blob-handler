package com.helper.blobclient.handler;

/**
 * @author Jhun
 * 2019-10-16
 */
public class AzureInfraError extends HandlerError {
    public AzureInfraError(String message) {
        super(message);
    }

    public AzureInfraError(String message, Throwable cause) {
        super(message, cause);
    }
}
