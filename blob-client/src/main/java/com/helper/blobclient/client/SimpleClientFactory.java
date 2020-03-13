package com.helper.blobclient.client;

import com.helper.blobclient.handler.error.AzureInfraError;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * @author Jhun
 * 2019-10-16
 */
public class SimpleClientFactory implements AzureBlobClientFactory {
    public CloudBlobClient create(ConnectionStringFactory source) {
        try {
            CloudStorageAccount
                    account = CloudStorageAccount.parse(source.getConnectionString());
            return account.createCloudBlobClient();
        } catch (URISyntaxException | InvalidKeyException e) {
            throw new AzureInfraError(e.getMessage(), e);
        }
    }
}
