package ru.spbu.crawliver.runtimestats;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbu.crawliver.controllers.AbstractCrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;

import java.util.*;

public class RuntimeStatsController extends AbstractCrawlerController {

    private final Logger logger = LoggerFactory.getLogger(RuntimeStatsController.class);

    public RuntimeStatsController(CrawlerProperties crawlerProps) {
        super(crawlerProps);
    }

    @Override
    public void crawl() throws Exception {
        final CrawlController controller = configureController();

        CrawlController.WebCrawlerFactory<RuntimeStatsCrawler> factory =
                () -> new RuntimeStatsCrawler(crawlerProps);
        controller.start(factory, crawlerProps.getNumberOfCrawlers());

        accumulate(controller.getCrawlersLocalData());
    }

    private void accumulate(List<Object> data) {
        long pages = 0L;
        long links = 0L;
        long externalLinks = 0L;
        long textLength = 0L;
        long htmlLength = 0L;
        long bytesLength = 0L;
        final Map<String, Integer> subDomains = new HashMap<>();

        for (Object obj : data) {
            final Stats stats = (Stats) obj;
            pages += stats.pages;
            links += stats.links;
            externalLinks += stats.externalLinks;
            textLength += stats.textLength;
            htmlLength += stats.htmlLength;
            bytesLength += stats.bytesLength;

            stats.subDomains.forEach((key, value) -> subDomains.merge(
                    key,
                    value,
                    Integer::sum)
            );
        }

        logger.info("Total Processed Pages: {}", pages);
        logger.info("Total Links Found: {}", links);
        logger.info("Total External Links Found: {}", externalLinks);
        logger.info("Total Text Length: {}", textLength);
        logger.info("Total HTML Length: {}", htmlLength);
        logger.info("Total Bytes Length: {}", bytesLength);
        logger.info("Subdomains: {}", subDomains);
        logger.info("Subdomains Number: {}", subDomains.size());
        logger.info("Subdomains Links: {}", subDomains.values().stream().mapToInt(it -> it).sum());
    }
}
