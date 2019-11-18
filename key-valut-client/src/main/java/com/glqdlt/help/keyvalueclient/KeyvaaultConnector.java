package com.glqdlt.help.keyvalueclient;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;
import com.microsoft.azure.keyvault.models.SecretBundle;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Jhun
 * 2019-11-11
 */
public class KeyvaaultConnector {
    public KeyvaaultConnector(String secretKey, String clientId, String authServerApiUrl, String keyvaultServerApiUrl, String... keys) {
        this.secretKey = secretKey;
        this.clientId = clientId;
        this.authServerApiUrl = authServerApiUrl;
        this.keyvaultServerApiUrl = keyvaultServerApiUrl;
        this.keys = keys;
    }

    final private String secretKey;
    final private String clientId;
    final private String authServerApiUrl;
    final private String keyvaultServerApiUrl;
    final private String[] keys;

    public String[] getKeys() {
        return keys;
    }

    public String getKeyvaultServerApiUrl() {
        return keyvaultServerApiUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthServerApiUrl() {
        return authServerApiUrl;
    }

    public Map<String, String> getKeyvaultItems() throws MalformedURLException {
        ExecutorService service = Executors.newFixedThreadPool(1);
        final AuthenticationContext context = new AuthenticationContext(getAuthServerApiUrl(), true, service);
        KeyVaultCredentials credentials = new KeyVaultCredentials() {
            @Override
            public String doAuthenticate(String authorization, String resource, String scope) {
                ClientCredential credentials = new ClientCredential(getClientId(), getSecretKey());
                Future<AuthenticationResult> resultFuture = context.acquireToken(resource, credentials, null);
                try {
                    return resultFuture.get().getAccessToken();
                } catch (InterruptedException | ExecutionException ex) {
                    throw new KeyvaultConnectorError(ex);
                }
            }
        };

        final KeyVaultClient keyVaultClient = new KeyVaultClient(credentials);
        Map<String, String> map = new HashMap<>();
        for (String k : getKeys()) {
            SecretBundle z = keyVaultClient.getSecret(getKeyvaultServerApiUrl(), k);
            map.put(k, z.toString());
        }
        service.shutdown();
        return map;
    }
}
