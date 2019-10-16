package com.glqdlt.helper.handler;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * @author Jhun
 * 2019-10-16
 */
public class SimpleClientFactory implements ClientFactory {
    public CloudBlobClient create(AzureBlobStorageConnectionSource azureBlobStorageConnectionSource) {
        try {
            CloudStorageAccount
                    account = CloudStorageAccount.parse(azureBlobStorageConnectionSource.getConnectionString());
            return account.createCloudBlobClient();
        } catch (URISyntaxException | InvalidKeyException e) {
            throw new AzureInfraError(e.getMessage(), e);
        }
    }
}
