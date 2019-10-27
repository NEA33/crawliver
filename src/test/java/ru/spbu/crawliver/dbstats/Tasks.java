package ru.spbu.crawliver.dbstats;

import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;

public class Tasks {

    @Test
    public void test() throws Exception {
        CrawlerController controller = new PostgreSQLStoringCrawlerController(
                System.getProperty("user.dir") + "/src/main/resources/temp",
                "https://www.w3schools.com/sql/",
                "www.w3schools.com/sql",
                200,
                1,
                4,
                "jdbc:postgresql://localhost/scraped",
                "_USER_",
                "_PASSWORD_"
        );
        controller.crawl();
    }
}
