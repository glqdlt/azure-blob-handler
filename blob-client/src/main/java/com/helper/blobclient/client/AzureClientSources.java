package com.helper.blobclient.client;

import com.helper.blobclient.handler.AzureBlobStorageConnectionSource;
import com.helper.blobclient.handler.SimpleClientFactory;
import com.microsoft.azure.storage.blob.CloudBlobClient;

/**
 * @author Jhun
 * 2019-11-11
 */
public class AzureClientSources {
    public static CloudBlobClient make(String azureConnectionUrl) {
        AzureBlobStorageConnectionSource azureBlobStorageConnectionSource = new AzureBlobStorageConnectionSource(azureConnectionUrl);
        SimpleClientFactory simpleClientFactory = new SimpleClientFactory();
        return simpleClientFactory.create(azureBlobStorageConnectionSource);
    }
}
