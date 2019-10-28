package ru.spbu.crawliver.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    public Properties getProperties(String propertiesFileName) throws IOException {
        final Properties properties = new Properties();
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        if (stream != null) {
            properties.load(stream);
        } else {
            throw new FileNotFoundException("Property file " + propertiesFileName + "was not found in classpath");
        }

        return properties;
    }
}
