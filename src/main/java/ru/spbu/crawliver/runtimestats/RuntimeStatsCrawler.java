package ru.spbu.crawliver.runtimestats;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spbu.crawliver.helpers.CrawlerProperties;

import java.util.Set;
import java.util.regex.Pattern;

import static ru.spbu.crawliver.helpers.UrlHelper.*;

public class RuntimeStatsCrawler extends WebCrawler {

    private static final Pattern FILE_ENDING_EXCLUSION_PATTERN = Pattern.compile(".*(\\.(" +
            "css|js" +
            "|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw" +
            "|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g" +
            "|avi|mov|mpeg|ram|m4v|wmv|rm|smil" +
            "|swf" +
            "|zip|rar|gz|bz2|7z|bin" +
            "|java|c|cpp|exe" +
            "))$");

    private final Logger logger = LoggerFactory.getLogger(RuntimeStatsCrawler.class);
    private final Stats stats = new Stats();
    private final CrawlerProperties crawlerProps;

    RuntimeStatsCrawler(CrawlerProperties crawlerProps) {
        this.crawlerProps = crawlerProps;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        final String domain = domain(url.getURL());
        final String subDomain = subDomain(url.getURL(), domain);
        final String rest = rest(url.getURL());

        stats.links++;
        if (domain.equals(crawlerProps.getDomain()) &&
                rest.startsWith(crawlerProps.getLinkFilter())) {
            return !FILE_ENDING_EXCLUSION_PATTERN.matcher(url.getURL().toLowerCase()).matches();
        } else {
            if (!subDomain.isEmpty() && domain.contains(crawlerProps.getDomain())) {
                stats.subDomains.put(
                        subDomain,
                        1 + stats.subDomains.getOrDefault(subDomain, 0)
                );
            } else {
                stats.externalLinks++;
            }
            return false;
        }
    }

    @Override
    public void visit(Page page) {
        final WebURL url = page.getWebURL();
        logger.info("URL: " + url.getURL());

        stats.pages++;

        if (page.getParseData() instanceof HtmlParseData) {
            final HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            final String text = htmlParseData.getText();
            final String html = htmlParseData.getHtml();
            final Set<WebURL> links = htmlParseData.getOutgoingUrls();

            stats.textLength += text.length();
            stats.htmlLength += html.length();
            stats.bytesLength += html.getBytes().length;

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
        logger.info("Crawler {} > Links Found: {}", id, stats.links);
        logger.info("Crawler {} > External Links Found: {}", id, stats.externalLinks);
        logger.info("Crawler {} > Total Text Length: {}", id, stats.textLength);
        logger.info("Crawler {} > Total HTML Length: {}", id, stats.htmlLength);
        logger.info("Crawler {} > Total Bytes Length: {}", id, stats.bytesLength);
        logger.info("Crawler {} > Subdomains: {}", id, stats.subDomains);
    }
}
