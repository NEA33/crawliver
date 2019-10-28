package ru.spbu.crawliver.runtimestats;

import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;

public class Tasks {

    private CrawlerController controller;

    @Test
    public void testW3SchoolCrawler() throws Exception {
        controller = new MainController(
                new CrawlerProperties("w3school.crawler.properties")
        );
        controller.crawl();
    }
}
