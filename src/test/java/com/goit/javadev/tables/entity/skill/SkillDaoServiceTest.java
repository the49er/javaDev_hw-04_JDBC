package com.goit.javadev.tables.entity.skill;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class SkillDaoServiceTest {
    private Connection connection;
    private SkillDaoService skillDaoService;
    Skill skillTest1;
    Skill skillTest2;
    Skill skillTest3;
    String jsonFilePath;
    List<Skill> expectedList;

    @BeforeEach
    public void beforeEach () throws SQLException {
        final String jdbc = "jdbc:h2:mem:./testDataBase;DB_CLOSE_DELAY=-1";
        String sqlCreateDataBase = "CREATE SCHEMA IF NOT EXISTS `homework_4`";
        String sqlCreateTableCompany = "CREATE TABLE IF NOT EXISTS `homework_4`.skills (" +
                "id IDENTITY PRIMARY KEY, programming_lang VARCHAR(100), level VARCHAR(100))";
        connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);
        skillDaoService = new SkillDaoService(connection);
        skillTest1 = new Skill(1, ProgramLang.java, SkillLevel.junior);
        skillTest2 = new Skill(2, ProgramLang.cPlus, SkillLevel.middle);
        skillTest3 = new Skill(3, ProgramLang.javaScript, SkillLevel.senior);
        expectedList = List.of(skillTest1, skillTest2, skillTest3);
        jsonFilePath = "src/test/resource/json/test_skills.json";
        skillDaoService.clearTable();
    }

    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    public void testThatSkillCreated() {
        long id = skillDaoService.insertNewEntity(skillTest1);
        Skill createdSkill = skillDaoService.getEntityById(id);
        Assertions.assertEquals(id, createdSkill.getId());
    }

    @Test
    void testThatEntitiesCreated() {
        int expected = skillDaoService.insertNewEntities(expectedList);
        int actual = skillDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntityFieldsUpdated() {
        skillDaoService.insertNewEntity(skillTest1);
        boolean actual = skillDaoService.updateEntityFieldsById(skillTest2, 1);
        Assertions.assertTrue(true, String.valueOf(actual));
    }


    @Test
    void testThatEntityDeleted() {
        skillDaoService.insertNewEntity(skillTest1);
        skillDaoService.deleteById(1);
        Optional<Skill> actual = Optional.ofNullable(skillDaoService.getEntityById(1));
        Assertions.assertNull(null, String.valueOf(actual));
    }

    @Test
    void testThatEntitiesWereDeleted() {
        long[] longArr = {1, 2, 3};
        int expected = skillDaoService.insertNewEntities(expectedList);
        int actual = skillDaoService.deleteEntitiesFromListById(longArr);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatEntitiesWereInsertedFromJsonFile() {
        int expected = skillDaoService.insertEntitiesFromJsonFile(jsonFilePath);
        int actual = skillDaoService.getAllEntities().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testThatTableWasCleared() {
        skillDaoService.insertNewEntities(expectedList);
        skillDaoService.clearTable();
        Optional<Skill> actual = Optional.ofNullable(skillDaoService.getEntityById(1));
        Assertions.assertFalse(false, String.valueOf(actual.isPresent()));
    }
}
