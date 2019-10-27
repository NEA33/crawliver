package ru.spbu.crawliver.crawler4j;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainController {

    private static final String crawlStorageFolder = System.getProperty("user.dir") + "/src/test/resources/temp";
    private static final String entryPoint = "https://www.w3schools.com/sql";
    private static final String domain = "www.w3schools.com/sql";
    private static final int politenessDelay = 1000;
    private static final int depth = 1;
    private static final int numberOfCrawlers = 4;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public static void main(String[] args) throws Exception {

        // config settings
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(politenessDelay);
        config.setMaxDepthOfCrawling(depth);
        // config.setMaxPagesToFetch(1000);

        // controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // entry point
        controller.addSeed(entryPoint);

        CrawlController.WebCrawlerFactory<MainCrawler> factory = () -> new MainCrawler(domain);
        controller.start(factory, numberOfCrawlers);

        accumulate(controller.getCrawlersLocalData());
    }

    private static void accumulate(List<Object> data) {
        long pages = 0L;
        long links = 0L;
        long externalLinks = 0L;
        long textLength = 0L;
        long htmlLength = 0L;
        final Set<String> subDomains = new HashSet<>();

        for (Object obj : data) {
            final Stats stats = (Stats) obj;
            pages += stats.pages.get();
            links += stats.links.get();
            externalLinks += stats.externalLinks.get();
            textLength += stats.textLength.get();
            htmlLength += stats.htmlLength.get();
            subDomains.addAll(stats.subDomains);
        }

        logger.info("Total Processed Pages: {}", pages);
        logger.info("Total Links Found: {}", links);
        logger.info("Total External Links Found: {}", externalLinks);
        logger.info("Total Text Length: {}", textLength);
        logger.info("Total HTML Length: {}", htmlLength);
        logger.info("Total Subdomains: {}", subDomains);
        logger.info("Subdomains Number: {}", subDomains.size());
    }
}
