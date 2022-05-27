package com.goit.javadev.entity.company;

import com.goit.javadev.feature.prefs.Config;
import com.goit.javadev.feature.storage.DataBaseInitService;
import com.goit.javadev.feature.storage.Storage;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CompanyDaoServiceTest {
    private Connection connection;
    private CompanyDaoService companyDaoService;

    @BeforeEach
    public void beforeEach () throws SQLException {
        Config config = new Config();
        Storage storage = Storage.getInstance();
        final String connectionUrl = config.getString(Config.DB_JDBC_CONNECTION_URL);
        final String user = config.getString(Config.DB_JDBC_USER_PASSWORD_1);
        final String password = config.getString(Config.DB_JDBC_USER_PASSWORD_1);
        new DataBaseInitService().initDbFlyWay(storage);
        connection = storage.getConnection();
        connection.setAutoCommit(false);
        companyDaoService = new CompanyDaoService(connection);
        companyDaoService.clearTable();
    }

    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    public void testThatCompanyCreated() throws SQLException {
        Company companyTest = new Company(1, "TestCompany", "UnitTest");
        long id = companyDaoService.createNewCompany(companyTest);
        Company createdCompany = companyDaoService.getCompanyById(id);

        Assertions.assertEquals(1, createdCompany.getId());
    }
}
