package com.kairachka.bankapi.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    private static final String property = "src/main/resources/property/property";
    private FileInputStream fileInputStream;
    Properties properties = new Properties();

    public String getUrl() {
        try {
            fileInputStream = new FileInputStream(property);
            properties.load(fileInputStream);
            return properties.getProperty("connection.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPort() {
        try{
            fileInputStream = new FileInputStream(property);
            properties.load(fileInputStream);
            return Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getRealm() {
        try{
            fileInputStream = new FileInputStream(property);
            properties.load(fileInputStream);
            return properties.getProperty("authenticator.realm");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
