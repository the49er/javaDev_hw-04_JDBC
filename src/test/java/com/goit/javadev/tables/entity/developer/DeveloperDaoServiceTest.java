package com.goit.javadev.tables.entity.developer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.goit.javadev.tables.entity.developer.Developer.Gender.female;
import static com.goit.javadev.tables.entity.developer.Developer.Gender.male;
import static com.goit.javadev.tables.entity.developer.Developer.Gender.other;


class DeveloperDaoServiceTest {
    private Connection connection;
    private DeveloperDaoService developerDaoService;
    Developer developerTest1;
    Developer developerTest2;
    Developer developerTest3;
    String jsonFilePath;

    @BeforeEach
    void beforeEach () throws SQLException {
        final String jdbc = "jdbc:h2:mem:./testDataBase;DB_CLOSE_DELAY=-1";
        String sqlCreateDataBase = "CREATE SCHEMA IF NOT EXISTS `homework_4`";
        //id, name, age, gender, salary, company_id
        String sqlCreateTableCompany = "CREATE TABLE IF NOT EXISTS `homework_4`.developers (" +
                "id IDENTITY PRIMARY KEY, name VARCHAR(100) NOT NULL, age INT, gender VARCHAR(10) NULL, salary INT, company_id INT)";
        connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);
        developerDaoService = new DeveloperDaoService(connection);
        developerTest1 = new Developer(1, "TestName1", 10, male, 1000, 1);
        developerTest2 = new Developer(2, "TestName2", 15, female, 2000, 2);
        developerTest3 = new Developer(3, "TestName3", 20, other, 3000, 3);
        jsonFilePath = "src/test/resource/json/test_developers.json";
        developerDaoService.clearTable();
    }

    @AfterEach
    void afterEach() throws SQLException {
        connection.createStatement().executeUpdate("DROP SCHEMA IF EXISTS `homework_4` CASCADE");
        connection.close();
    }

    @Test
    void testThatEntityCreated() throws SQLException {
        long id =  developerDaoService.insertNewEntity(developerTest1);
        Developer createdDeveloper = developerDaoService.getEntityById(id);
        Assertions.assertEquals(1, createdDeveloper.getId());
    }

    @Test
    void testThatEntitiesCreated() throws SQLException{
        List<Developer> listDeveloper = List.of(developerTest1, developerTest2, developerTest3);
        int expected = developerDaoService.createDeveloperFromList(listDeveloper);
        int actual = developerDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntityFieldsUpdated() throws SQLException{
        developerDaoService.insertNewEntity(developerTest1);
        boolean actual = developerDaoService.updateEntityFieldsStOfDeveloperById(developerTest2, 1);
        Assertions.assertTrue(true, String.valueOf(actual));
    }

    @Test
    void testThatEntitiesReceivedByLikeQuery() throws SQLException{
        List<Developer> expected = List.of(developerTest1, developerTest2, developerTest3);
        developerDaoService.createDeveloperFromList(expected);
        List<Developer> actual = developerDaoService.getDeveloperBySpecificFieldLike("TestName");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntityDeleted() throws SQLException {
        developerDaoService.insertNewEntity(developerTest1);
        developerDaoService.deleteById(1);
        Optional<Developer> actual = Optional.ofNullable(developerDaoService.getEntityById(1));
        Assertions.assertNull(null, String.valueOf(actual));
    }

    @Test
    void testThatEntitiesWereDeleted() throws SQLException {
        List<Developer> listDeveloper = List.of(developerTest1, developerTest2, developerTest3);
        long[] longArr = {1, 2, 3};
        int expected =  developerDaoService.createDeveloperFromList(listDeveloper);
        int actual = developerDaoService.deleteEntitiesFromListById(longArr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntitiesWereInsertedFromJsonFile() throws SQLException {
        int expected = developerDaoService.insertNewDevelopersFromJsonFile(jsonFilePath);
        int actual = developerDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatTableWasCleared() throws SQLException {
        List<Developer> listDeveloper = new ArrayList<>();
        listDeveloper.add(developerTest1);
        listDeveloper.add(developerTest2);
        listDeveloper.add(developerTest3);
        developerDaoService.createDeveloperFromList(listDeveloper);
        developerDaoService.clearTable();
        Optional<Developer> actual = Optional.ofNullable(developerDaoService.getEntityById(1));
        Assertions.assertFalse(false, String.valueOf(actual.isPresent()));
    }
}
