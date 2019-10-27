package ru.spbu.crawliver.dbstats;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import ru.spbu.crawliver.db.PostgreSQLService;

public class PostgreSQLCrawlerFactory implements CrawlController.WebCrawlerFactory<DatabaseStoringCrawler> {

    private final ComboPooledDataSource comboPooledDataSource;
    private final String domain;

    public PostgreSQLCrawlerFactory(ComboPooledDataSource comboPooledDataSource, String domain) {
        this.comboPooledDataSource = comboPooledDataSource;
        this.domain = domain;
    }

    public DatabaseStoringCrawler newInstance() throws Exception {
        return new DatabaseStoringCrawler(new PostgreSQLService(comboPooledDataSource, domain), domain);
    }
}
