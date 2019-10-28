package ru.spbu.crawliver.db;

import edu.uci.ics.crawler4j.crawler.Page;

public interface DatabaseService {

    void store(Page page, String domainRestriction);

    boolean isNew(Page page);

    boolean isNew(String url);

    void close();
}
