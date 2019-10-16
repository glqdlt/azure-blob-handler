package com.glqdlt.helper.handler;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Jhun
 * 2019-10-16
 */
public class SimpleAzureBlobRepositoryTest {



    @Test
    public void generateLink() throws URISyntaxException {
        SimpleAzureBlobRepository simpleAzureBlobRepository = new SimpleAzureBlobRepository(null);
        String z = simpleAzureBlobRepository.generateUrl(new URI("http://www.naver.com"), "some/some2/some3", "file.png");
        Assert.assertEquals("http://www.naver.com/some/some2/some3/file.png", z);
    }
}