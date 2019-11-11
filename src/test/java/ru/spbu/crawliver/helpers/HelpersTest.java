package ru.spbu.crawliver.helpers;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpersTest {

    private final Logger logger = LoggerFactory.getLogger(HelpersTest.class);

    @Test
    public void testUrlHelper() {
        String link = "http://www.apmath.spbu.ru/ru/education/curriculum/";
        logger.info(UrlHelper.domain(link));
        logger.info(UrlHelper.subDomain(link, "spbu.ru"));
        logger.info(UrlHelper.rest(link));

        Assert.assertEquals("www.apmath.spbu.ru", UrlHelper.domain(link));
        Assert.assertEquals("www.apmath", UrlHelper.subDomain(link, "spbu.ru"));
        Assert.assertEquals("ru/education/curriculum/", UrlHelper.rest(link));

        link = "http://math.spbu.ru/ru/";
        Assert.assertEquals("math.spbu.ru", UrlHelper.domain(link));
        Assert.assertEquals("math", UrlHelper.subDomain(link, "spbu.ru"));
        Assert.assertEquals("ru/", UrlHelper.rest(link));

        link = "http://spbu.ru/ru/";
        Assert.assertEquals("spbu.ru", UrlHelper.domain(link));
        Assert.assertEquals("", UrlHelper.subDomain(link, "spbu.ru"));
        Assert.assertEquals("ru/", UrlHelper.rest(link));
    }
}
