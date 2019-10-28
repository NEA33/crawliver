package ru.spbu.crawliver.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostgreSQLService implements DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLService.class);

    private ComboPooledDataSource comboPooledDataSource;
    private PreparedStatement statement;

    private final String insertSQL = "INSERT INTO page(" +
            "url, " +
            "domain, " +
            "sub_domain, " +
            "type, " +
            "encoding, " +
            "language, " +
            "status_code, " +
            "bytes_length, " +
            "total_links, " +
            "external_links, " +
            "text_size, " +
            "html_size, " +
            "title, " +
            "stamp" +
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private final String selectSQL = "SELECT id, stamp FROM page WHERE url = (?)";

    public PostgreSQLService(ComboPooledDataSource comboPooledDataSource) {
        this.comboPooledDataSource = comboPooledDataSource;
    }

    @Override
    public void store(Page page, String domainRestriction) {
        final String url = page.getWebURL().getURL();

        try {
            statement = comboPooledDataSource
                    .getConnection()
                    .prepareStatement(insertSQL);

            statement.setString(1, url);
            statement.setString(2, page.getWebURL().getDomain());
            statement.setString(3, page.getWebURL().getSubDomain());
            statement.setString(4, page.getContentType());
            statement.setString(5, page.getContentEncoding());
            statement.setString(6, page.getLanguage());
            statement.setInt(7, page.getStatusCode());
            statement.setInt(8, page.getContentData().length);

            ParseData parseData = page.getParseData();
            statement.setInt(9, parseData.getOutgoingUrls().size());
            statement.setInt(10, (int) parseData.getOutgoingUrls()
                    .stream()
                    .filter(link -> !link.getURL().contains(domainRestriction))
                    .count()
            );

            if (parseData instanceof TextParseData) {
                TextParseData textParseData = (TextParseData) parseData;
                statement.setInt(11, textParseData.getTextContent().length());
            }

            if (parseData instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) parseData;
                statement.setInt(11, htmlParseData.getText().length());
                statement.setInt(12, htmlParseData.getHtml().length());
                statement.setString(13, htmlParseData.getTitle());
            }

            statement.setTimestamp(14, new Timestamp(new java.util.Date().getTime()));

            logger.info("Storing page with url: {}", url);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception while storing page for url'{}'", url, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isNew(Page page) {
        return isNew(page.getWebURL().getURL());
    }

    @Override
    public boolean isNew(String url) {
        try {
            statement = comboPooledDataSource
                    .getConnection()
                    .prepareStatement(selectSQL);

            statement.setString(1, url);

            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                logger.info("Page with url: {} is already present in database, id: {}, stamp: {}",
                        url,
                        resultSet.getLong("id"),
                        resultSet.getString("stamp")
                );
                return false;
            } else {
                return true;
            }

        } catch (SQLException e) {
            logger.error("SQL Exception while retrieving page with url'{}'", url, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (comboPooledDataSource != null) {
            comboPooledDataSource.close();
        }
    }
}
