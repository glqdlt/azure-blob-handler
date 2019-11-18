package com.glqdlt.help.keyvalueclient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Jhun
 * 2019-11-18
 */
@Ignore
public class KeyvaludConnecoterTest {

    private String fonesAdServerUrl;
    private String appClientId;
    private String appClientSecretKey;
    private String fonesKeyVaultUrl;

    @Before
    public void setUp() throws Exception {
        try (InputStream inputStream = new FileInputStream(ClassLoader.getSystemResource("azure.properties").getPath())) {
            Properties properties = new Properties();
            properties.load(inputStream);
            fonesAdServerUrl = properties.getProperty("azure.ad.url");
            appClientId = properties.getProperty("azure.app.client.id");
            appClientSecretKey = properties.getProperty("azure.app.client.secret");
            fonesKeyVaultUrl = properties.getProperty("azure.keyvault.url");
        }
    }

    /**
     * @throws MalformedURLException
     * @see <a href='https://docs.microsoft.com/en-us/java/api/com.microsoft.azure.keyvault.authentication.keyvaultcredentials?view=azure-java-stable'>https://docs.microsoft.com/en-us/java/api/com.microsoft.azure.keyvault.authentication.keyvaultcredentials?view=azure-java-stable</a>
     */
    @Test
    public void name() throws MalformedURLException {
        String[] ItemKey = {"SEEDKEY"};
        KeyvaaultConnector keyvaludConnecoter = new KeyvaaultConnector(appClientSecretKey, appClientId, fonesAdServerUrl, fonesKeyVaultUrl, ItemKey);
        Map<String, String> keyvaultItems = keyvaludConnecoter.getKeyvaultItems();
        Assert.assertNotNull(keyvaultItems.get(ItemKey[0]));
    }
}