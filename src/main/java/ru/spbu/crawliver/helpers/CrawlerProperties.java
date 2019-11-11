package ru.spbu.crawliver.helpers;

import java.io.IOException;
import java.util.Properties;

public class CrawlerProperties {

    private final String crawlStorageFolder;
    private final String entryPoint;
    private final String domain;
    private final String linkFilter;
    private final int politenessDelay;
    private final int maxDepthOfCrawling;
    private final int maxPagesToFetch;
    private final boolean resumableCrawling;
    private final int numberOfCrawlers;

    private final PropertiesHelper helper = new PropertiesHelper();

    public CrawlerProperties(String propertiesFileName) throws IOException {
        final Properties properties = helper.getProperties(propertiesFileName);
        this.crawlStorageFolder = properties.getProperty("crawlStorageFolder");
        this.entryPoint = properties.getProperty("entryPoint");
        this.domain = properties.getProperty("domain");
        this.linkFilter = properties.getProperty("linkFilter");
        this.politenessDelay = Integer.parseInt(properties.getProperty("politenessDelay"));
        this.maxDepthOfCrawling = Integer.parseInt(properties.getProperty("maxDepthOfCrawling"));
        this.maxPagesToFetch = Integer.parseInt(properties.getProperty("maxPagesToFetch"));
        this.resumableCrawling = Boolean.parseBoolean(properties.getProperty("resumableCrawling"));
        this.numberOfCrawlers = Integer.parseInt(properties.getProperty("numberOfCrawlers"));
    }

    public String getCrawlStorageFolder() {
        return crawlStorageFolder;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getDomain() {
        return domain;
    }

    public String getLinkFilter() {
        return linkFilter;
    }

    public int getPolitenessDelay() {
        return politenessDelay;
    }

    public int getMaxDepthOfCrawling() {
        return maxDepthOfCrawling;
    }

    public int getMaxPagesToFetch() {
        return maxPagesToFetch;
    }

    public boolean isResumableCrawling() {
        return resumableCrawling;
    }

    public int getNumberOfCrawlers() {
        return numberOfCrawlers;
    }

    @Override
    public String toString() {
        return "CrawlerProperties{" +
                "crawlStorageFolder='" + crawlStorageFolder + '\'' +
                ", entryPoint='" + entryPoint + '\'' +
                ", domain='" + domain + '\'' +
                ", linkFilter='" + linkFilter + '\'' +
                ", politenessDelay=" + politenessDelay +
                ", maxDepthOfCrawling=" + maxDepthOfCrawling +
                ", maxPagesToFetch=" + maxPagesToFetch +
                ", resumableCrawling=" + resumableCrawling +
                ", numberOfCrawlers=" + numberOfCrawlers +
                '}';
    }
}
