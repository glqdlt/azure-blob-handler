package com.helper.blobclient.handler;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;
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
        try (FileInputStream a = new FileInputStream(file)) {
            CloudBlobContainer container = client.getContainerReference(uploadPath);
            CloudBlockBlob blob = container.getBlockBlobReference(uploadName);
            blob.upload(a, file.length());
            return new URI(generateUrl(client.getStorageUri().getPrimaryUri(), uploadPath, uploadName));
        } catch (URISyntaxException | StorageException e) {
            throw new AzureInfraError(e.getMessage(), e);
        } catch (Throwable e) {
            throw new InternalIoError(e.getMessage(), e);
        }
    }

    @Override
    public File download(File saveDir, String blobContainerPath, String downloadFileName) {
        return download(saveDir, blobContainerPath + "/" + downloadFileName);
    }

    public String extractFileName(URI path) {
        String[] ee = extractString(path);
        return ee[ee.length - 1];
    }

    public String extractPath(URI path) {
        String fileName = extractFileName(path);
        String p = path.getPath();
        int rr = p.indexOf(fileName);
        return p.substring(0, rr);
    }

    private String[] extractString(URI path) {
        String zz = path.getPath();
        return zz.split("/");
    }

    @Override
    public File download(File saveDir, String blobUrl) {
        if (!saveDir.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s is not directory", saveDir.getName()));
        }
        try {
            URI u = new URI(blobUrl);
            final CloudBlobClient client = getClient();
            CloudBlobContainer container = client.getContainerReference(extractPath(u));
            String fileName = extractFileName(u);
            CloudBlockBlob origin = container.getBlockBlobReference(fileName);
            String saveFileNamne = saveDir.getPath() + File.separator + fileName;
            origin.downloadToFile(saveFileNamne);
            return new File(saveFileNamne);
        } catch (URISyntaxException | StorageException e) {
            throw new AzureInfraError(e.getMessage(), e);
        } catch (Throwable e) {
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
