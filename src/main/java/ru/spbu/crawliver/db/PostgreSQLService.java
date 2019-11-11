package ru.spbu.crawliver.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static ru.spbu.crawliver.helpers.UrlHelper.*;

public class PostgreSQLService implements DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLService.class);

    private ComboPooledDataSource comboPooledDataSource;
    private PreparedStatement statement;
    private Connection connection;

    private final String insertPageSQL = "INSERT INTO page(" +
            "domain, " +
            "url, " +
            "language, " +
            "bytes_length, " +
            "total_links, " +
            "external_links, " +
            "sub_domain_links, " +
            "text_size, " +
            "html_size, " +
            "title, " +
            "stamp" +
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    private final String selectPageSQL = "SELECT id, stamp FROM page WHERE domain = (?) AND url = (?)";

    private final String updateDomainSQL = "UPDATE domain SET number = (?) WHERE id = (?)";

    private final String insertDomainSQL = "INSERT INTO domain(name, number) VALUES (?, ?)";

    private final String selectDomainSQL = "SELECT id, number FROM domain WHERE name = (?)";

    public PostgreSQLService(ComboPooledDataSource comboPooledDataSource) throws SQLException {
        this.comboPooledDataSource = comboPooledDataSource;
        connection = comboPooledDataSource.getConnection();
    }

    @Override
    public void store(Page page, String domainRestriction) {
        final String url = page.getWebURL().getURL();
        final String domain = domain(url);
        final String rest = rest(url);

        try {
            statement = connection.prepareStatement(insertPageSQL);

            statement.setString(1, domain);
            statement.setString(2, rest);
            statement.setString(3, page.getLanguage());
            statement.setInt(4, page.getContentData().length);

            ParseData parseData = page.getParseData();
            statement.setInt(5, parseData.getOutgoingUrls().size());
            statement.setInt(6, (int) parseData.getOutgoingUrls()
                    .stream()
                    .filter(link -> !link.getURL().contains(domainRestriction))
                    .count()
            );
            statement.setInt(7, (int) parseData.getOutgoingUrls()
                    .stream()
                    .filter(link -> !subDomain(link.getURL(), domainRestriction).isEmpty())
                    .count()
            );

            if (parseData instanceof TextParseData) {
                TextParseData textParseData = (TextParseData) parseData;
                statement.setInt(8, textParseData.getTextContent().length());
            }

            if (parseData instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) parseData;
                statement.setInt(8, htmlParseData.getText().length());
                statement.setInt(9, htmlParseData.getHtml().length());
                statement.setString(10, htmlParseData.getTitle());
            }

            statement.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));

            logger.info("Storing page: {}", url);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception while storing page for url'{}'", url, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void incrementDomainStats(String domain) {
        try {
            statement = connection.prepareStatement(selectDomainSQL);
            statement.setString(1, domain);
            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int references = resultSet.getInt("number");
                logger.info("Sub domain: {} is present in database, id: {}, references: {}, incrementing...",
                        domain,
                        id,
                        references
                );
                statement = connection.prepareStatement(updateDomainSQL);
                statement.setInt(1, references + 1);
                statement.setInt(2, id);
            } else {
                logger.info("New sub domain: {} storing...", domain);
                statement = connection.prepareStatement(insertDomainSQL);
                statement.setString(1, domain);
                statement.setInt(2, 1);
            }
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception while storing domain {}", domain, e);
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
            statement = connection.prepareStatement(selectPageSQL);
            statement.setString(1, domain(url));
            statement.setString(2, rest(url));

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
