package com.goit.javadev.tables.entity.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CompanyDaoServiceTest {
    private Connection connection;
    private CompanyDaoService companyDaoService;

    @BeforeEach
    public void beforeEach () throws SQLException {
        final String jdbc = "jdbc:h2:mem:./testDataBase;DB_CLOSE_DELAY=-1";
        String sqlCreateDataBase = "CREATE SCHEMA `homework_4`";
        String sqlCreateTableCompany = "CREATE TABLE `homework_4`.companies (" +
                "id IDENTITY PRIMARY KEY, name VARCHAR(100), specialization VARCHAR(100))";
        connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);
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
        long id = companyDaoService.insertNewCompany(companyTest);
        Company createdCompany = companyDaoService.getCompanyById(id);

        Assertions.assertEquals(1, createdCompany.getId());
    }
}
