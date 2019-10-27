package ru.spbu.crawliver.runtimestats;

import org.junit.Test;
import ru.spbu.crawliver.controllers.CrawlerController;

public class Tasks {

    @Test
    public void simpleCrawl() throws Exception {
        CrawlerController controller = new MainController(
                System.getProperty("user.dir") + "/src/main/resources/temp",
                "https://www.w3schools.com/php/",
                "www.w3schools.com/php",
                200,
                1,
                4
        );
        controller.crawl();
    }
}
