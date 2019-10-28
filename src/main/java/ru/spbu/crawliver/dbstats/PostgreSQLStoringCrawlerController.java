package ru.spbu.crawliver.dbstats;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.flywaydb.core.Flyway;
import ru.spbu.crawliver.controllers.AbstractCrawlerController;
import ru.spbu.crawliver.helpers.CrawlerProperties;
import ru.spbu.crawliver.helpers.DatabaseProperties;

import java.beans.PropertyVetoException;

public class PostgreSQLStoringCrawlerController extends AbstractCrawlerController {

    private final DatabaseProperties databaseProps;

    public PostgreSQLStoringCrawlerController(CrawlerProperties crawlerProps,
                                              DatabaseProperties databaseProps) {
        super(crawlerProps);
        this.databaseProps = databaseProps;
    }

    @Override
    public void crawl() throws Exception {
        final CrawlController controller = configureController();
        migrate();
        final ComboPooledDataSource pool = configurePool();
        controller.start(new PostgreSQLCrawlerFactory(pool, crawlerProps), crawlerProps.getNumberOfCrawlers());

        pool.close();
    }

    private void migrate() {
        Flyway flyway = Flyway.configure().dataSource(
                databaseProps.getUrl(),
                databaseProps.getUser(),
                databaseProps.getPassword()
        ).load();
        flyway.migrate();
    }

    private ComboPooledDataSource configurePool() throws PropertyVetoException {
        final ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setDriverClass(databaseProps.getDriver());
        pool.setJdbcUrl(databaseProps.getUrl());
        pool.setUser(databaseProps.getUser());
        pool.setPassword(databaseProps.getPassword());
        pool.setMaxPoolSize(crawlerProps.getNumberOfCrawlers());
        pool.setMinPoolSize(crawlerProps.getNumberOfCrawlers());
        pool.setInitialPoolSize(crawlerProps.getNumberOfCrawlers());
        return pool;
    }
}
