package ru.spbu.crawliver.db;

import edu.uci.ics.crawler4j.crawler.Page;

public interface DatabaseService {

    void store(Page page, String domain);

    void incrementDomainStats(String domain);

    boolean isNew(Page page);

    boolean isNew(String url);

    void close();
}
