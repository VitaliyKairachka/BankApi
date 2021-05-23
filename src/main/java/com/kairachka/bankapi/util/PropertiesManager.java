package com.kairachka.bankapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
    private static final String property = "src/main/resources/property/property";
    private final Properties properties = new Properties();
    private final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);

    public String getUrl() {
        try (FileInputStream fileInputStream = new FileInputStream(property)) {
            properties.load(fileInputStream);
            return properties.getProperty("connection.url");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public int getPort() {
        try (FileInputStream fileInputStream = new FileInputStream(property)) {
            properties.load(fileInputStream);
            return Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return -1;
    }
}
