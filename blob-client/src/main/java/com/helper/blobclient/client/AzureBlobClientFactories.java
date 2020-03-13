package com.helper.blobclient.client;

import com.microsoft.azure.storage.blob.CloudBlobClient;

/**
 * @author Jhun
 * 2019-11-11
 */
public class AzureBlobClientFactories {

    public static CloudBlobClient createSimpleClient(String azureConnectionUrl) {
        final ConnectionStringFactory connectionStringGenerateHelper = new ConnectionStringFactory(azureConnectionUrl);
        final SimpleClientFactory simpleClientFactory = new SimpleClientFactory();
        return simpleClientFactory.create(connectionStringGenerateHelper);
    }
}
