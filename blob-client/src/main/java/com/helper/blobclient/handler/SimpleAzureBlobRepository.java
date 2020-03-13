package com.helper.blobclient.handler;

import com.helper.blobclient.client.AzureBlobClientFactories;
import com.helper.blobclient.handler.error.AzureInfraError;
import com.helper.blobclient.handler.error.HandlerError;
import com.helper.blobclient.handler.error.InternalIoError;
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

    private CloudBlobClient blobClient;

    /**
     * @param cloudBlobClient azure blob 에 접근하기 위한 azure client, {{@link AzureBlobClientFactories}} 를 통해서 쉽게 만들수있음
     */
    public SimpleAzureBlobRepository(CloudBlobClient cloudBlobClient) {
        this.blobClient = cloudBlobClient;
    }

    public CloudBlobClient getBlobClient() {
        return blobClient;
    }

    public void setBlobClient(CloudBlobClient cloudBlobClient) {
        this.blobClient = cloudBlobClient;
    }

    @Override
    public CloudBlobClient getClient() {
        return getBlobClient();
    }

    /**
     * 아무생각없이 file 이름 그대로 blob 에 올리고 싶은 경우.
     *
     * @param file       업로드 파일 바이너리
     * @param uploadPath 업로드 되는 경로, 컨테이너/업로드경로/파일
     * @return 업로드 되고 완료 된 blob 파일 경로 URI
     */
    @Override
    public URI upload(File file, String uploadPath) {
        return upload(file, file.getName(), uploadPath);
    }

    /**
     * blob에 업로드 하되 이름을 지정하여 업로드 하고 싶은 경우
     *
     * @param file                 업로드 파일 바이너리
     * @param uploadFileName       업로드 될 파일명 (확장자 포함)
     * @param uploadTargetBlobPath 업로드 되는 경로, 컨테이너/업로드경로/파일
     * @return 업로드 되고 완료 된 blob 파일 경로 URI
     */
    @Override
    public URI upload(File file, String uploadFileName, String uploadTargetBlobPath) {
        final CloudBlobClient client = getClient();
        try (FileInputStream fis = new FileInputStream(file)) {
            CloudBlobContainer container = client.getContainerReference(uploadTargetBlobPath);
            CloudBlockBlob blob = container.getBlockBlobReference(uploadFileName);
            blob.upload(fis, file.length());
            return generateUrl(client.getStorageUri().getPrimaryUri(), uploadTargetBlobPath, uploadFileName);
        } catch (URISyntaxException | StorageException e) {
            throw new AzureInfraError(e.getMessage(), e);
        } catch (Throwable e) {
            throw new InternalIoError(e.getMessage(), e);
        }
    }

    /**
     * 다운로드 시 파일 명을 지정해서 다운 받고 싶은 경우
     *
     * @param saveDir          다운로드 받을 디렉토리
     * @param uploadedBlobPath 다운로드 받을 blob 업로드 되어 있는 주소
     * @param downloadFileName 다운로드 받을 파일 이름
     * @return
     */
    @Override
    public File download(File saveDir, String uploadedBlobPath, String downloadFileName) {
        return download(saveDir, suffixSlush(uploadedBlobPath) + downloadFileName);
    }

    public String extractFileName(URI path) {
        String[] ee = extractString(path);
        if (ee.length > 0) {
            return ee[ee.length - 1];
        }
        return null;
    }

    public String extractPath(URI path) {
        String fileName = extractFileName(path);
        String p = path.getPath();
        if (fileName == null || fileName.equals("")) {
            throw new HandlerError(String.format("filename is null. check your path, %s", path.getPath()));
        }
        int rr = p.indexOf(fileName);
        return p.substring(0, rr);
    }

    private String[] extractString(URI path) {
        String zz = path.getPath();
        return zz.split("/");
    }

    /**
     * 아무생각없이 다운로드 받고 싶을 경우
     *
     * @param saveDir          다운로드 받을 디렉토리
     * @param uploadedBlobPath 다운로드 받을 blob 주소
     * @return
     */
    @Override
    public File download(File saveDir, String uploadedBlobPath) {
        if (!saveDir.isDirectory()) {
            throw new IllegalArgumentException(String.format("%s is not directory", saveDir.getName()));
        }
        try {
            URI u = new URI(uploadedBlobPath);
            final CloudBlobClient client = getClient();
            CloudBlobContainer container = client.getContainerReference(extractPath(u));
            String uploadedFileOriginName = extractFileName(u);
            CloudBlockBlob origin = container.getBlockBlobReference(uploadedFileOriginName);
            String downloadFileName = saveDir.getPath() + File.separator + uploadedFileOriginName;
            origin.downloadToFile(downloadFileName);
            return new File(downloadFileName);
        } catch (URISyntaxException | StorageException e) {
            throw new AzureInfraError(e.getMessage(), e);
        } catch (Throwable e) {
            throw new InternalIoError(e.getMessage(), e);
        }
    }

    public URI generateUrl(URI host, String uploadPath, String uploadName) throws URISyntaxException {
        String u = host.toString();
        return new URI(u + addSlush(uploadPath) + addSlush(uploadName));
    }

    private String addSlush(String str) {
        if (str.indexOf("/") != 0) {
            str = "/" + str;
        }
        return str;
    }

    private String suffixSlush(String str) {
        if (!str.endsWith("/")) {
            return str + "/";
        }
        return str;
    }
}
