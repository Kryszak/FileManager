package com.kryszak.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfiguration {

    private static final String PROPERTY_FILE = "application.properties";

    private static Properties properties;

    private static void loadProperties() throws IOException {
        properties = new Properties();
        try (InputStream inputStream = ApplicationConfiguration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            properties.load(inputStream);
        }
    }

    public static String getProperty(String key) throws IOException {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }

}
