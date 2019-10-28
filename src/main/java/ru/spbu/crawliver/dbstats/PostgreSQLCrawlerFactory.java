package ru.spbu.crawliver.dbstats;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import ru.spbu.crawliver.db.PostgreSQLService;
import ru.spbu.crawliver.helpers.CrawlerProperties;

public class PostgreSQLCrawlerFactory implements CrawlController.WebCrawlerFactory<DatabaseStoringCrawler> {

    private final ComboPooledDataSource comboPooledDataSource;
    private final CrawlerProperties crawlerProps;

    public PostgreSQLCrawlerFactory(ComboPooledDataSource comboPooledDataSource, CrawlerProperties crawlerProps) {
        this.comboPooledDataSource = comboPooledDataSource;
        this.crawlerProps = crawlerProps;
    }

    public DatabaseStoringCrawler newInstance() throws Exception {
        return new DatabaseStoringCrawler(new PostgreSQLService(comboPooledDataSource), crawlerProps);
    }
}
