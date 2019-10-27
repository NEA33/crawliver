package ru.spbu.crawliver.runtimestats;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

public class MainCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav" +
            "|avi|mov|mpeg|ram|m4v|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private final Logger logger = LoggerFactory.getLogger(MainCrawler.class);
    private final Stats stats = new Stats();
    private final String domain;

    MainCrawler(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        final String href = url.getURL().toLowerCase();
        if (href.contains(domain)) {
            return !FILTERS.matcher(href).matches();
        } else {
            stats.externalLinks.incrementAndGet();
            return false;
        }
    }

    @Override
    public void visit(Page page) {
        final WebURL url = page.getWebURL();
        logger.info("URL: " + url.getURL());

        // increment number of visited pages
        stats.pages.incrementAndGet();

        // add domain to list if it is new
        if (!stats.subDomains.contains(url.getDomain()))
            stats.subDomains.add(url.getDomain());

        if (page.getParseData() instanceof HtmlParseData) {
            final HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            final String text = htmlParseData.getText();
            final String html = htmlParseData.getHtml();
            final Set<WebURL> links = htmlParseData.getOutgoingUrls();

            stats.textLength.addAndGet(text.length());
            stats.htmlLength.addAndGet(html.length());
            stats.links.addAndGet(links.size());

            logger.info("Text length: " + text.length());
            logger.info("Html length: " + html.length());
            logger.info("Number of outgoing links: " + links.size());
        }
    }

    @Override
    public Object getMyLocalData() {
        return stats;
    }

    @Override
    public void onBeforeExit() {
        int id = getMyId();

        logger.info("Crawler {} > Processed Pages: {}", id, stats.pages);
        logger.info("Crawler {} > Total Links Found: {}", id, stats.links);
        logger.info("Crawler {} > Total External Links Found: {}", id, stats.externalLinks);
        logger.info("Crawler {} > Total Text Length: {}", id, stats.textLength);
        logger.info("Crawler {} > Total HTML Length: {}", id, stats.htmlLength);
        logger.info("Crawler {} > Subdomains: {}", id, stats.subDomains);
    }
}
