package ru.spbu.crawliver.dbstats;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;
import ru.spbu.crawliver.helpers.DatabaseProperties;

import java.io.IOException;

public class DatabaseStatsCrawlerCases {

    private CrawlerController controller;
    private CrawlerProperties crawlerProps;
    private DatabaseProperties databaseProps;

    @Before
    public void init() throws IOException {
        databaseProps = new DatabaseProperties("database.test.properties");
        migrate(databaseProps);
    }

    @Test
    public void testW3SchoolCrawler() throws Exception {
        crawlerProps = new CrawlerProperties("w3school.crawler.properties");
        controller = new PostgreSQLStoringCrawlerController(
                crawlerProps,
                databaseProps
        );
        controller.crawl();
    }

    @Test
    public void testExampleCom() throws Exception {
        crawlerProps = new CrawlerProperties("example.crawler.properties");
        controller = new PostgreSQLStoringCrawlerController(
                crawlerProps,
                databaseProps
        );
        controller.crawl();
    }

    @Test
    public void showProps() {
        System.out.println(crawlerProps);
        System.out.println(databaseProps);
    }

    private void migrate(DatabaseProperties databaseProps) {
        final Flyway flyway = Flyway.configure().dataSource(
                databaseProps.getUrl(),
                databaseProps.getUser(),
                databaseProps.getPassword()
        ).load();
        flyway.migrate();
    }
}
