package com.goit.javadev.tables.entity.skill;

import com.goit.javadev.exception.DaoException;
import com.goit.javadev.tables.entity.crudEntityDAO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SkillDaoService implements crudEntityDAO<Skill> {

    private static final String TABLE_NAME = "`homework_4`.skills";
    PreparedStatement insertSt;
    PreparedStatement updateEntityFieldsSt;
    PreparedStatement getEntityByIdSt;
    PreparedStatement getBySpecificFieldLikeSt;
    PreparedStatement getAllEntitiesSt;
    PreparedStatement deleteById;
    PreparedStatement clearTableSt;
    PreparedStatement getMaxIdSt;


    public SkillDaoService(Connection connection) {
        try {
            getMaxIdSt = connection.prepareStatement(
                    "SELECT max(id) AS maxId FROM " + TABLE_NAME
            );

            insertSt = connection.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (programming_lang, level) VALUES (?, ?)"
            );

            updateEntityFieldsSt = connection.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET programming_lang = ?, level = ? WHERE id = ?"
            );

            getEntityByIdSt = connection.prepareStatement(
                    "SELECT id, programming_lang, level FROM " +
                            TABLE_NAME + " WHERE id = ?"
            );

            getBySpecificFieldLikeSt = connection.prepareStatement(
                    "SELECT id, programming_lang, level FROM " +
                            TABLE_NAME + " WHERE programming_lang LIKE ?"
            );

            getAllEntitiesSt = connection.prepareStatement(
                    "SELECT id, programming_lang, level FROM " + TABLE_NAME
            );

            deleteById = connection.prepareStatement(
                    "DELETE FROM " + TABLE_NAME + " WHERE ID = ?"
            );

            clearTableSt = connection.prepareStatement(
                    "DELETE FROM " + TABLE_NAME
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public long insertNewEntity(Skill element) {
        long id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            insertSt.setString(1, element.getProgramLang().getProgLang());
            insertSt.setString(2, element.getSkillLevel().getLevel());
            insertSt.executeUpdate();
            rs.next();
            id = rs.getLong("maxId");
            return id;
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean clearTable() {
        try {
            clearTableSt.executeUpdate();
            log.info(TABLE_NAME + " has been cleared");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int insertNewEntities(List<Skill> element) {
        try {
            for (Skill skill : element) {
                String programmingLang = skill.getProgramLang().getProgLang();
                String level = skill.getSkillLevel().getLevel();
                insertSt.setString(1, programmingLang);
                insertSt.setString(2, level);
                insertSt.addBatch();
            }
            insertSt.executeBatch();
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
        }

        if (element.size() > 1) {
            log.info("Insert " + element.size() + " new records");
        } else if (element.size() == 1) {
            log.info("Insert " + element.size() + "new record");
        }
        return element.size();
    }

    @Override
    public int insertEntitiesFromJsonFile(String jsonFilePath) {
        Gson gson = new Gson();
        String inString = null;
        try {
            inString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Skill[] skills = gson.fromJson(inString, Skill[].class);
        List<Skill> skillList =
                Arrays.stream(skills)
                        .collect(Collectors.toList());

        insertNewEntities(skillList);
        if (skillList.size() > 1) {
            log.info("Created " + skillList.size() + " new records from JSON in table " + TABLE_NAME);
        } else if (skillList.size() == 1) {
            log.info("Created " + skillList.size() + "new record from JSON in table " + TABLE_NAME);
        }
        return skillList.size();
    }

    @Override
    public boolean updateEntityFieldsById(Skill element, long id) {
        return false;
    }


    @Override
    public Skill getEntityById(long id) {
        try {
            getEntityByIdSt.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            Skill skill = new Skill();
            while (rs.next()) {
                skill.setId(id);
                skill.setProgramLang(ProgramLang.valueOf(rs.getNString("programming_lang")));
                skill.setSkillLevel(SkillLevel.valueOf(rs.getString("level")));
            }
            log.info("get Skill by id: " + id);
            return skill;

        } catch (DaoException | SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public List<Skill> getAllEntities() {
        return null;
    }

    @Override
    public int deleteEntitiesFromListById(long[] ids) {
        return 0;
    }

    @Override
    public boolean deleteById(long id) {
        try {
            deleteById.setLong(1, id);
            deleteById.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
}
