package ru.spbu.crawliver.helpers;

import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {

    private final String driver;
    private final String url;
    private final String user;
    private final String password;

    private final PropertiesHelper helper = new PropertiesHelper();

    public DatabaseProperties(String propertiesFileName) throws IOException {
        final Properties properties = helper.getProperties(propertiesFileName);
        this.driver = properties.getProperty("driver");
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DatabaseProperties{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user.substring(0, 3) + "***" + '\'' +
                ", password='" + "***" + '\'' +
                '}';
    }
}
