package com.kryszak.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationConfiguration {

    private static final Logger LOGGER = Logger.getLogger(ApplicationConfiguration.class.getName());

    private static final String PROPERTY_FILE = "application.properties";

    private static Properties properties;

    private ApplicationConfiguration() {
        // blank
    }

    private static void loadProperties() throws IOException {
        properties = new Properties();
        try (InputStream inputStream = ApplicationConfiguration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            properties.load(inputStream);
        }
    }

    public static String getProperty(String key) {
        if (properties == null) {
            try {
                loadProperties();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
        return properties.getProperty(key);
    }

}
