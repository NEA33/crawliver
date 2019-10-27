package ru.spbu.crawliver.runtimestats;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class Stats {
    AtomicInteger pages = new AtomicInteger();
    // AtomicInteger brokenPages = new AtomicInteger();
    AtomicInteger links = new AtomicInteger();
    AtomicInteger externalLinks = new AtomicInteger();
    AtomicLong textLength = new AtomicLong();
    AtomicLong htmlLength = new AtomicLong();

    Set<String> subDomains = new HashSet<>();
}
