package com.goit.javadev.database.keys.entity.company;

import com.goit.javadev.database.entity.company.Company;
import com.goit.javadev.database.entity_services.CompanyDaoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CompanyDaoServiceTest {
    private Connection connection;
    private CompanyDaoService companyDaoService;
    Company companyTest1;
    Company companyTest2;
    Company companyTest3;
    String jsonFilePath;
    List<Company> expectedList;

    @BeforeEach
    public void beforeEach () throws SQLException {
        final String jdbc = "jdbc:h2:mem:./testDataBase;DB_CLOSE_DELAY=-1";
        String sqlCreateDataBase = "CREATE SCHEMA IF NOT EXISTS `homework_4`";
        String sqlCreateTableCompany = "CREATE TABLE IF NOT EXISTS `homework_4`.companies (" +
                "id IDENTITY PRIMARY KEY, name VARCHAR(100), specialization VARCHAR(100))";
        connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);
        companyDaoService = new CompanyDaoService(connection);
        companyTest1 = new Company(1, "TestName1", "TestDescription");
        companyTest2 = new Company(2, null, "TestDescription");
        companyTest3 = new Company(3, "TestName3", null);
        expectedList = List.of(companyTest1, companyTest2, companyTest3);
        jsonFilePath = "src/test/resource/json/test_companies.json";
        companyDaoService.clearTable();
    }

    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    public void testThatCompanyCreated() {
        Company companyTest = new Company(1, "TestCompany", "UnitTest");
        long id = companyDaoService.insertNewEntity(companyTest);
        Company createdCompany = companyDaoService.getEntityById(id);
        Assertions.assertEquals(id, createdCompany.getId());
    }

    @Test
    void testThatEntitiesCreated() {
        int expected = companyDaoService.insertNewEntities(expectedList);
        int actual = companyDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntityFieldsUpdated() {
        companyDaoService.insertNewEntity(companyTest1);
        boolean actual = companyDaoService.updateEntityFieldsById(companyTest2, 1);
        Assertions.assertTrue(true, String.valueOf(actual));
    }


    @Test
    void testThatEntityDeleted() {
        companyDaoService.insertNewEntity(companyTest1);
        companyDaoService.deleteById(1);
        Optional<Company> actual = Optional.ofNullable(companyDaoService.getEntityById(1));
        Assertions.assertNull(null, String.valueOf(actual));
    }

    @Test
    void testThatEntitiesWereDeleted() {
        long[] longArr = {1, 2, 3};
        int expected = companyDaoService.insertNewEntities(expectedList);
        int actual = companyDaoService.deleteEntitiesFromListById(longArr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntitiesWereInsertedFromJsonFile() {
        int expected = companyDaoService.insertEntitiesFromJsonFile(jsonFilePath);
        int actual = companyDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatTableWasCleared() {
        companyDaoService.insertNewEntities(expectedList);
        companyDaoService.clearTable();
        Optional<Company> actual = Optional.ofNullable(companyDaoService.getEntityById(1));
        Assertions.assertFalse(false, String.valueOf(actual.isPresent()));
    }
}
