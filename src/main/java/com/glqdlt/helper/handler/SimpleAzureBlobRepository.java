package com.glqdlt.helper.handler;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Jhun
 * 2019-10-16
 */
public class SimpleAzureBlobRepository implements AzureBlobRepository {

    private CloudBlobClient cloudBlobContainer;

    public SimpleAzureBlobRepository(CloudBlobClient cloudBlobContainer) {
        this.cloudBlobContainer = cloudBlobContainer;
    }

    public CloudBlobClient getCloudBlobContainer() {
        return cloudBlobContainer;
    }

    public void setCloudBlobContainer(CloudBlobClient cloudBlobContainer) {
        this.cloudBlobContainer = cloudBlobContainer;
    }

    @Override
    public CloudBlobClient getClient() {
        return cloudBlobContainer;
    }

    @Override
    public URI upload(File file, String uploadPath) {
        return upload(file, file.getName(), uploadPath);
    }

    @Override
    public URI upload(File file, String uploadName, String uploadPath) {
        final CloudBlobClient client = getClient();
        try {
            CloudBlobContainer container = client.getContainerReference(uploadPath);
            CloudBlockBlob blob = container.getBlockBlobReference(uploadName);
            blob.upload(new FileInputStream(file), file.length());
            return new URI(generateUrl(client.getStorageUri().getPrimaryUri(), uploadPath, uploadName));
        } catch (URISyntaxException | StorageException e) {
            throw new AzureInfraError(e.getMessage(), e);
        } catch (IOException e) {
            throw new InternalIoError(e.getMessage(), e);
        }
    }

    public String generateUrl(URI host, String uploadPath, String uploadName) {
        String u = host.toString();
        return u + addSlush(uploadPath) + addSlush(uploadName);
    }

    private String addSlush(String str) {
        if (str.indexOf("/") != 0) {
            str = "/" + str;
        }
        return str;
    }
}
