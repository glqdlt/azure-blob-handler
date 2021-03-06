package com.helper.blobclient.handler;

import com.microsoft.azure.storage.blob.CloudBlobClient;

import java.io.File;
import java.net.URI;

/**
 * @author Jhun
 * 2019-10-16
 */
public interface AzureBlobRepository {
    CloudBlobClient getClient();

    URI upload(File file, String uploadPath);

    URI upload(File file, String uploadName, String uploadPath);

    File download(File saveDir, String blobContainerPath, String downloadFileName);

    File download(File saveFile, String blobDownloadUrl);
}
