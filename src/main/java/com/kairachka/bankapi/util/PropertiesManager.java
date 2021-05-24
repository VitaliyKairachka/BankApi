package com.kairachka.bankapi.util;

import com.kairachka.bankapi.BankApiApplication;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    private static final String property = "/property/property";
    private final Properties properties = new Properties();

    public String getUrl() {
        try {
            properties.load(BankApiApplication.class.getResourceAsStream(property));
            return properties.getProperty("connection.url");
        } catch (IOException e) {
            System.out.println("IO error");
        }
        return null;
    }

    public int getPort() {
        try {
            properties.load(BankApiApplication.class.getResourceAsStream(property));
            return Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException e) {
            System.out.println("IO error");
        }
        return -1;
    }
}
