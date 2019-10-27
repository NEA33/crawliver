package ru.spbu.crawliver.db;

import edu.uci.ics.crawler4j.crawler.Page;

public interface DatabaseService {

    void store(Page webPage);

    void close();
}
