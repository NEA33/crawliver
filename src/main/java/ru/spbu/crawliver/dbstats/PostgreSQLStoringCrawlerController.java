package ru.spbu.crawliver.dbstats;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import org.flywaydb.core.Flyway;
import ru.spbu.crawliver.controllers.AbstractCrawlerController;

import java.beans.PropertyVetoException;

public class PostgreSQLStoringCrawlerController extends AbstractCrawlerController {

    private final String url;
    private final String user;
    private final String password;

    public PostgreSQLStoringCrawlerController(String crawlStorageFolder, String entryPoint,
                                              String domain, int politenessDelay,
                                              int depth, int numberOfCrawlers,
                                              String url, String user, String password) {

        super(crawlStorageFolder, entryPoint, domain, politenessDelay, depth, numberOfCrawlers);

        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public void crawl() throws Exception {
        final CrawlController controller = configureController();
        migrate();
        final ComboPooledDataSource pool = configurePool();
        controller.start(new PostgreSQLCrawlerFactory(pool, domain), numberOfCrawlers);
    }

    private void migrate() {
        Flyway flyway = Flyway.configure().dataSource(url, user, password).load();
        flyway.migrate();
    }

    private ComboPooledDataSource configurePool() throws PropertyVetoException {
        final ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setDriverClass("org.postgresql.Driver");
        pool.setJdbcUrl(url);
        pool.setUser(user);
        pool.setPassword(password);
        pool.setMaxPoolSize(numberOfCrawlers);
        pool.setMinPoolSize(numberOfCrawlers);
        pool.setInitialPoolSize(numberOfCrawlers);
        return pool;
    }
}
