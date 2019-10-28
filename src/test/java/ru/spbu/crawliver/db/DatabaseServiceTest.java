package ru.spbu.crawliver.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.spbu.crawliver.helpers.DatabaseProperties;

public class DatabaseServiceTest {

    private DatabaseService service;
    private DatabaseProperties databaseProps;

    @Before
    public void init() throws Exception {
        databaseProps = new DatabaseProperties("database.properties");

        final ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setDriverClass(databaseProps.getDriver());
        pool.setJdbcUrl(databaseProps.getUrl());
        pool.setUser(databaseProps.getUser());
        pool.setPassword(databaseProps.getPassword());
        pool.setMaxPoolSize(4);
        pool.setMinPoolSize(4);
        pool.setInitialPoolSize(4);
        service = new PostgreSQLService(pool);
    }

    @Test
    public void testIsNew() {
        // Assert.assertFalse(service.isNew("https://www.w3schools.com/sql/func_mysql_current_time.asp"));
        Assert.assertTrue(service.isNew("https://www.w3schools.com/html/"));
    }

    @Test
    public void showProps() {
        System.out.println(databaseProps);
    }

    @After
    public void finale() {
        service.close();
    }
}
