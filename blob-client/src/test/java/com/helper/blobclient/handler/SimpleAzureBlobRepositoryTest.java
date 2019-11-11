package com.helper.blobclient.handler;

import com.helper.blobclient.client.AzureClientSources;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Jhun
 * 2019-11-11
 */
public class SimpleAzureBlobRepositoryTest {

    @Test
    public void generateLink() throws URISyntaxException {
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(null);
        String z = simpleAzureBlobRepository.generateUrl(new URI("http://www.naver.com"), "some/some2/some3", "file.png");
        Assert.assertEquals("http://www.naver.com/some/some2/some3/file.png", z);
    }

    @Test
    public void jsonFileSplit() throws URISyntaxException {
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(null);
        String fileName = simpleAzureBlobRepository.extractFileName(new URI("http://www.naver.com/asdasd/aa.json"));
        Assert.assertEquals("aa.json", fileName);
        String container = simpleAzureBlobRepository.extractPath(new URI("http://www.navber.com/aaa/aab/aa.json"));
        Assert.assertEquals("/aaa/aab/", container);
    }

    @Test
    public void jsonFileSplit2() throws URISyntaxException {
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(null);
        String ee = simpleAzureBlobRepository.extractFileName(new URI("asdasdaa.json"));
        Assert.assertEquals("asdasdaa.json", ee);
    }


    @Test
    public void upload() {
        URL dummyJson = ClassLoader.getSystemClassLoader().getResource("dummy.json");
        final String connectionUrl;
        final String container;
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("test.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            connectionUrl = properties.getProperty("azure.blob.connection.url");
            container = properties.getProperty("azure.blob.upload.target.container");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(AzureClientSources.make(connectionUrl));
        URI r = simpleAzureBlobRepository.upload(new File(dummyJson.getPath()), container);
        Assert.assertNotNull(r.getPath());
    }

    @Test
    public void download() {
        final String connectionUrl;
        final String targetUrl;
        final String downloadPath;
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("test.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            connectionUrl = properties.getProperty("azure.blob.connection.url");
            targetUrl = properties.getProperty("azure.blob.upload.target.url");
            downloadPath = System.getProperty("java.io.tmpdir");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(AzureClientSources.make(connectionUrl));
        File ee = simpleAzureBlobRepository.download(new File(downloadPath), targetUrl);
        boolean d = ee.delete();
        if (!d) {
            Assert.fail();
        }
    }
}