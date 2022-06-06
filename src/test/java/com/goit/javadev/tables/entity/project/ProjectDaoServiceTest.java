package com.goit.javadev.tables.entity.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class ProjectDaoServiceTest {
    private Connection connection;
    private ProjectDaoService projectDaoService;
    Project projectTest1;
    Project projectTest2;
    Project projectTest3;
    String jsonFilePath;
    List<Project> expectedList;

    @BeforeEach
    public void beforeEach () throws SQLException {
        final String jdbc = "jdbc:h2:mem:./testDataBase;DB_CLOSE_DELAY=-1";
        String sqlCreateDataBase = "CREATE SCHEMA IF NOT EXISTS `homework_4`";
        String sqlCreateTableCompany = "CREATE TABLE IF NOT EXISTS `homework_4`.projects (" +
                "id IDENTITY PRIMARY KEY, name VARCHAR(100), description VARCHAR(100)," +
                "date_contract DATE, customer_id INT, company_id INT)";
        connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);
        projectDaoService = new ProjectDaoService(connection);
        projectTest1 = new Project(1, "TestName1", "TestDescription1", LocalDate.now(), 1, 4);
        projectTest2 = new Project(2, "TestName2", "TestDescription2", LocalDate.now().minusMonths(1), 2, 3);
        projectTest3 = new Project(3, "TestName3", "TestDescription3", LocalDate.now().minusMonths(2), 3, 2);
        expectedList = List.of(projectTest1, projectTest2, projectTest3);
        jsonFilePath = "src/test/resource/json/test_projects.json";
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

    @Test
    void testThatEntitiesCreated() {
        int expected = projectDaoService.insertNewEntities(expectedList);
        int actual = projectDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntityFieldsUpdated() {
        projectDaoService.insertNewEntity(projectTest1);
        boolean actual = projectDaoService.updateEntityFieldsById(projectTest2, 1);
        Assertions.assertTrue(true, String.valueOf(actual));
    }


    @Test
    void testThatEntityDeleted() {
        projectDaoService.insertNewEntity(projectTest1);
        projectDaoService.deleteById(1);
        Optional<Project> actual = Optional.ofNullable(projectDaoService.getEntityById(1));
        Assertions.assertNull(null, String.valueOf(actual));
    }

    @Test
    void testThatEntitiesWereDeleted() {
        long[] longArr = {1, 2, 3};
        int expected = projectDaoService.insertNewEntities(expectedList);
        int actual = projectDaoService.deleteEntitiesFromListById(longArr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntitiesWereInsertedFromJsonFile() {
        int expected = projectDaoService.insertEntitiesFromJsonFile(jsonFilePath);
        int actual = projectDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatTableWasCleared() {
        projectDaoService.insertNewEntities(expectedList);
        projectDaoService.clearTable();
        Optional<Project> actual = Optional.ofNullable(projectDaoService.getEntityById(1));
        Assertions.assertFalse(false, String.valueOf(actual.isPresent()));
    }
}
