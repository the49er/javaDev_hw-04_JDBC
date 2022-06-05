package com.goit.javadev.tables.entity.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProjectDaoServiceTest {
    private Connection connection;
    private ProjectDaoService projectDaoService;

    @BeforeEach
    public void beforeEach () throws SQLException {
        final String jdbc = "jdbc:h2:mem:./testDataBase;DB_CLOSE_DELAY=-1";
        String sqlCreateDataBase = "CREATE SCHEMA `homework_4`";
        String sqlCreateTableCompany = "CREATE TABLE `homework_4`.projects (" +
                "id IDENTITY PRIMARY KEY, name VARCHAR(100), description VARCHAR(100)," +
                "date_contract DATE, customer_id INT, company_id INT)";
        connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);
        projectDaoService = new ProjectDaoService(connection);
        projectDaoService.clearTable();
    }

    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    public void testThatProjectCreated() {
        Project projectTest1 = new Project(1, "TestProject1", "TestDescription", LocalDate.parse("2021-01-24"), 1, 1);
        long id = projectDaoService.insertNewEntity(projectTest1);
        Project createdProject = projectDaoService.getEntityById(id);
        Assertions.assertEquals(id, createdProject.getId());
    }
}
