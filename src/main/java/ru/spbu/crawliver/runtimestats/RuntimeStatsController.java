package ru.spbu.crawliver.runtimestats;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbu.crawliver.controllers.AbstractCrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        logger.info("Subdomains: {}", subDomains);
        logger.info("Subdomains Number: {}", subDomains.size());
    }
}
