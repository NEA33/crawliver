package ru.spbu.crawliver.controllers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbu.crawliver.runtimestats.MainController;

public abstract class AbstractCrawlerController implements CrawlerController {

    protected final String crawlStorageFolder;
    protected final String entryPoint;
    protected final String domain;
    protected final int politenessDelay;
    protected final int depth;
    protected final int numberOfCrawlers;

    public AbstractCrawlerController(String crawlStorageFolder, String entryPoint,
                                     String domain, int politenessDelay,
                                     int depth, int numberOfCrawlers) {
        this.crawlStorageFolder = crawlStorageFolder;
        this.entryPoint = entryPoint;
        this.domain = domain;
        this.politenessDelay = politenessDelay;
        this.depth = depth;
        this.numberOfCrawlers = numberOfCrawlers;
    }

    protected final Logger logger = LoggerFactory.getLogger(MainController.class);

    public abstract void crawl() throws Exception;

    protected CrawlController configureController() throws Exception {
        final CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(politenessDelay);
        config.setMaxDepthOfCrawling(depth);
        config.setMaxPagesToFetch(1000000000);
        config.setResumableCrawling(true);

        final PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        final CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(entryPoint);
        return controller;
    }
}
