package ru.spbu.crawliver.dbstats;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;
import ru.spbu.crawliver.helpers.DatabaseProperties;

import java.io.IOException;

public class Tasks {

    private CrawlerController controller;
    private CrawlerProperties spbuCrawlerProps;
    private CrawlerProperties w3CrawlerProps;
    private DatabaseProperties databaseProps;

    @Before
    public void init() throws IOException {
        spbuCrawlerProps = new CrawlerProperties("spbu.crawler.properties");
        w3CrawlerProps = new CrawlerProperties("w3school.crawler.properties");
        databaseProps = new DatabaseProperties("database.properties");
    }

    @Test
    public void testW3SchoolCrawler() throws Exception {
        controller = new PostgreSQLStoringCrawlerController(
                w3CrawlerProps,
                databaseProps
        );
        controller.crawl();
    }

    @Ignore
    @Test
    public void testSpbuCrawler() throws Exception {
        controller = new PostgreSQLStoringCrawlerController(
                spbuCrawlerProps,
                databaseProps
        );
        controller.crawl();
    }

    @Test
    public void showProps() {
        System.out.println(w3CrawlerProps);
        System.out.println(spbuCrawlerProps);
        System.out.println(databaseProps);
    }
}
