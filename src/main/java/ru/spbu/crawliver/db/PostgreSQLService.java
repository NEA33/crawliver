package ru.spbu.crawliver.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostgreSQLService implements DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLService.class);

    private ComboPooledDataSource comboPooledDataSource;
    private PreparedStatement insertKeyStatement;

    private final String insertSQL = "INSERT INTO page(" +
            "url, " +
            "domain, " +
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
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private final String domain;

    public PostgreSQLService(ComboPooledDataSource comboPooledDataSource, String domain) throws SQLException {
        this.comboPooledDataSource = comboPooledDataSource;
        this.domain = domain;
        insertKeyStatement = comboPooledDataSource
                .getConnection()
                .prepareStatement(insertSQL);
    }

    @Override
    public void store(Page page) {
        final String url = page.getWebURL().getURL();

        try {
            insertKeyStatement.setString(1, url);
            insertKeyStatement.setString(2, page.getWebURL().getDomain());
            insertKeyStatement.setString(3, page.getContentType());
            insertKeyStatement.setString(4, page.getContentEncoding());
            insertKeyStatement.setString(5, page.getLanguage());
            insertKeyStatement.setInt(6, page.getStatusCode());
            insertKeyStatement.setInt(7, page.getContentData().length);

            ParseData parseData = page.getParseData();
            insertKeyStatement.setInt(8, parseData.getOutgoingUrls().size());
            insertKeyStatement.setInt(9, (int) parseData.getOutgoingUrls()
                    .stream()
                    .filter(link -> !link.getURL().contains(domain))
                    .count()
            );

            if (parseData instanceof TextParseData) {
                TextParseData textParseData = (TextParseData) parseData;
                insertKeyStatement.setInt(10, textParseData.getTextContent().length());
            }

            if (parseData instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) parseData;
                insertKeyStatement.setInt(10, htmlParseData.getText().length());
                insertKeyStatement.setInt(11, htmlParseData.getHtml().length());
                insertKeyStatement.setString(12, htmlParseData.getTitle());
            }

            insertKeyStatement.setTimestamp(13, new Timestamp(new java.util.Date().getTime()));
            insertKeyStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception while storing page for url'{}'", url, e);
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
