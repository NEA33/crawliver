package ru.spbu.crawliver.runtimestats;

import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;

public class RuntimeStatsCrawlerCases {

    private CrawlerController controller;

    @Test
    public void testW3SchoolCrawler() throws Exception {
        controller = new RuntimeStatsController(
                new CrawlerProperties("w3school.crawler.properties")
        );
        controller.crawl();
    }
}
