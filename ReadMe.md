
# Azure-blob-handler

## Concept

간단한 azure blob storage 헬퍼 라이브러리 입니다.
azure 에서 제공해주는 SDK 를 사용하기 쉽게 랩핑 된 수준입니다.
 
blob에 특정 파일을 업로드하고 다운로드하는 기능을 제공합니다.
사용 SDK 버전은 8.4.0 입니다.
SDK 공식 문서는 [아래](https://github.com/Azure/azure-storage-java/tree/legacy-master) 링크를 참고하십시오

- https://github.com/Azure/azure-storage-java/tree/legacy-master



```xml
   <dependency>
        <groupId>com.microsoft.azure</groupId>
        <artifactId>azure-storage</artifactId>
        <version>8.4.0</version>
    </dependency>

```


## How to use

자세한 것은 테스트 코드 SimpleAzureBlobRepositoryTest.java 를 참고하여주십시오.


### upload
```java

public class SomeUpload{
    public static void main(String[] args){
        final String connectionUrl = "azure-connectionUrl-with-token";
        final String blobUploadPath = "/container/path1/path2";
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(AzureBlobClientFactories.createSimpleClient(connectionUrl));
        URI r = simpleAzureBlobRepository.upload(new File("/some/file/direcotry/image.png"), blobUploadPath); 
    }
}
    
```

### download

```java
public class SomeDownload{
    public static void main(String[] args){
        final String connectionUrl = "azure-connectionUrl-with-token";
        final String targetBlobPath = "/container/path1/path2/image.png";
        final String downloadDirectory = "/some/my/download/path";
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(AzureBlobClientFactories.createSimpleClient(connectionUrl));
        URI r = simpleAzureBlobRepository.download(new File(downloadPath), targetUrl);
    }
}

```
