package ru.spbu.crawliver.controllers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import ru.spbu.crawliver.helpers.CrawlerProperties;

public abstract class AbstractCrawlerController implements CrawlerController {

    protected CrawlerProperties crawlerProps;

    public AbstractCrawlerController(CrawlerProperties crawlerProps) {
        this.crawlerProps = crawlerProps;
    }

    public abstract void crawl() throws Exception;

    protected CrawlController configureController() throws Exception {
        final CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlerProps.getCrawlStorageFolder());
        config.setPolitenessDelay(crawlerProps.getPolitenessDelay());
        config.setMaxDepthOfCrawling(crawlerProps.getMaxDepthOfCrawling());
        config.setMaxPagesToFetch(crawlerProps.getMaxPagesToFetch());
        config.setResumableCrawling(crawlerProps.isResumableCrawling());

        final PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        final CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(crawlerProps.getEntryPoint());
        return controller;
    }
}
