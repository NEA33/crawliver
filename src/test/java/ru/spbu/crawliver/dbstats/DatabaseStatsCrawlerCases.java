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
    private CrawlerProperties w3CrawlerProps;
    private DatabaseProperties databaseProps;

    @Before
    public void init() throws IOException {
        w3CrawlerProps = new CrawlerProperties("w3school.crawler.properties");
        databaseProps = new DatabaseProperties("database.test.properties");

        migrate(databaseProps);

        controller = new PostgreSQLStoringCrawlerController(
                w3CrawlerProps,
                databaseProps
        );
    }

    @Test
    public void testW3SchoolCrawler() throws Exception {
        controller.crawl();
    }

    @Test
    public void showProps() {
        System.out.println(w3CrawlerProps);
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
