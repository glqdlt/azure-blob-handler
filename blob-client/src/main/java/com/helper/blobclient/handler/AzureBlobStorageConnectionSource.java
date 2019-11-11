package com.helper.blobclient.handler;

/**
 * @author Jhun
 * 2019-10-16
 */
public class AzureBlobStorageConnectionSource {

    private final String connectionString;

    public String getConnectionString() {
        return connectionString;
    }


    public AzureBlobStorageConnectionSource(String connectionString) {
        this.connectionString = connectionString;
    }

    public AzureBlobStorageConnectionSource(String accountName, String accountKey) {
        this.connectionString = makeConnectionString("https", accountName, accountKey);
    }

    public AzureBlobStorageConnectionSource(String accountName, String accountKey, String scheme) {
        this.connectionString = makeConnectionString(scheme, accountName, accountKey);
    }


    private String makeConnectionString(String scheme, String accountName, String accountKey) {
        return String.format("DefaultEndpointsProtocol=%s; AccountName=%s; AccountKey=%s",
                scheme,
                accountName,
                accountKey);
    }

}
