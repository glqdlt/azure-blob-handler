package com.helper.blobclient.handler;

import com.microsoft.azure.storage.blob.CloudBlobClient;

/**
 * @author Jhun
 * 2019-10-16
 */
public interface ClientFactory {
    CloudBlobClient create(AzureBlobStorageConnectionSource source);
}
