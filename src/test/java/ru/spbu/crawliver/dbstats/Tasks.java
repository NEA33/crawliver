package ru.spbu.crawliver.dbstats;

import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;

public class Tasks {

    private final String url = "jdbc:postgresql://localhost/...";
    private final String user = "...";
    private final String password = "...";

    @Test
    public void w3Crawl() throws Exception {
        CrawlerController controller = new PostgreSQLStoringCrawlerController(
                System.getProperty("user.dir") + "/src/test/resources/temp",
                "https://www.w3schools.com/php/",
                "www.w3schools.com/php",
                200,
                1,
                4,
                url,
                user,
                password
        );
        controller.crawl();
    }

    @Test
    public void spbuCrawl() throws Exception {
        CrawlerController controller = new PostgreSQLStoringCrawlerController(
                System.getProperty("user.dir") + "/src/main/resources/temp",
                "https://spbu.ru/",
                "spbu.ru",
                50,
                -1,
                8,
                url,
                user,
                password
        );
        controller.crawl();
    }
}
