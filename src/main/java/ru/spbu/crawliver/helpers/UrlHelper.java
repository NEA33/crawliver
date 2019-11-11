package ru.spbu.crawliver.helpers;

public class UrlHelper {

    public static String domain(String url) {
        return url.substring(url.indexOf("//") + 2, url.indexOf("/", url.indexOf("//") + 2));
    }

    public static String subDomain(String url, String domain) {
        final int nameBeginning = url.indexOf("//") + 2;
        final int nameEnd = url.indexOf(domain);
        if (nameBeginning == nameEnd) return "";
        return url.substring(nameBeginning, nameEnd - 1);
    }

    public static String rest(String url) {
        return url.substring(url.indexOf("/", url.indexOf("//") + 2) + 1);
    }
}
