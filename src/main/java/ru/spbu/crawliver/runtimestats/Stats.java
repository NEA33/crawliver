package ru.spbu.crawliver.runtimestats;

import java.util.HashMap;
import java.util.Map;

class Stats {
    int pages = 0;
    long links = 0L;
    long externalLinks = 0L;
    long textLength = 0L;
    long htmlLength = 0L;
    long bytesLength = 0L;

    Map<String, Integer> subDomains = new HashMap<>();
}
