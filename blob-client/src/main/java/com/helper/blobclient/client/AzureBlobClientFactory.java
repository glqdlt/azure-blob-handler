package com.helper.blobclient.client;

import com.microsoft.azure.storage.blob.CloudBlobClient;

/**
 * @author Jhun
 * 2019-10-16
 */
public interface AzureBlobClientFactory {
    CloudBlobClient create(ConnectionStringFactory source);
}
