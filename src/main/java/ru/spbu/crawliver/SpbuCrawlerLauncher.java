package ru.spbu.crawliver;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbu.crawliver.controllers.CrawlerController;
import ru.spbu.crawliver.dbstats.PostgreSQLStoringCrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;
import ru.spbu.crawliver.helpers.DatabaseProperties;

public class SpbuCrawlerLauncher {

    private static Logger logger = LoggerFactory.getLogger(SpbuCrawlerLauncher.class);

    public static void main(String[] args) {
        try {
            final CrawlerProperties spbuCrawlerProps
                    = new CrawlerProperties("spbu.crawler.properties");
            final DatabaseProperties databaseProps
                    = new DatabaseProperties("database.properties");

            migrate(databaseProps);

            final CrawlerController controller = new PostgreSQLStoringCrawlerController(
                    spbuCrawlerProps,
                    databaseProps
            );

            controller.crawl();

        } catch (Exception e) {
            logger.error("Exception in main launcher: {}", e.getMessage());
        }
    }

    private static void migrate(DatabaseProperties databaseProps) {
        Flyway flyway = Flyway.configure().dataSource(
                databaseProps.getUrl(),
                databaseProps.getUser(),
                databaseProps.getPassword()
        ).load();
        flyway.migrate();
    }
}
