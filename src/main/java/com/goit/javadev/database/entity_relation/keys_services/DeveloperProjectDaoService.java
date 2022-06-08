package com.goit.javadev.database.entity_relation.keys_services;

import com.goit.javadev.database.entity_relation.keys.DeveloperProjectKey;
import com.goit.javadev.database.exception.DaoException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class DeveloperProjectDaoService implements ManyToMany<DeveloperProjectKey> {
    PreparedStatement insertSt;
    PreparedStatement deleteSt;
    PreparedStatement getMaxIdSt;
    PreparedStatement getAllSt;
    private static final String TABLE_NAME = "`homework_4`.developer_project";

    public DeveloperProjectDaoService(Connection connection) {
        try {
            insertSt = connection.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (developer_id, project_id) VALUES (?, ?)"
            );

            deleteSt = connection.prepareStatement(
                    "DELETE FROM " + TABLE_NAME + " WHERE developer_id = ? and project_id = ?"
            );

            getMaxIdSt = connection.prepareStatement(
                    "SELECT count(developer_id) AS maxId FROM " + TABLE_NAME
            );

            getAllSt = connection.prepareStatement(
                    "SELECT developer_id, project_id FROM " + TABLE_NAME
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean insertKey(DeveloperProjectKey key) {
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            insertSt.setLong(1, key.getDeveloperId());
            insertSt.setLong(2, key.getProjectId());
            rs.next();
            insertSt.executeUpdate();
            log.info("Key " + key + " was created");
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Can't create key : " + key.getDeveloperId() + ", " + key.getProjectId() + ".\n" +
                    "Due to key already exists");
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            log.info("Key " + key + " wasn't created");
        }

        return false;
    }

    @Override
    public boolean deleteKey(long developerId, long projectId) {
        try {
            deleteSt.setLong(1, developerId);
            deleteSt.setLong(1, projectId);
            deleteSt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<List<DeveloperProjectKey>> getAll() {
        List<DeveloperProjectKey> list = new ArrayList<>();

        try (ResultSet rs = getAllSt.executeQuery()) {
            while (rs.next()) {
                DeveloperProjectKey developerProjectKeyList = new DeveloperProjectKey();
                developerProjectKeyList.setDeveloperId(rs.getInt("developer_id"));
                developerProjectKeyList.setProjectId(rs.getInt("project_id"));
                list.add(developerProjectKeyList);

            }
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
        }
        log.info("Received list of: " + list.size() + " developer_project keys");
        return Optional.of(list);
    }

    @Override
    public int insertKeysFromJsonFile(String jsonFilePath) {
        Gson gson = new Gson();
        String inString = null;
        try {
            inString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DeveloperProjectKey[] developerProjectKeyLists = gson.fromJson(inString, DeveloperProjectKey[].class);
        List<DeveloperProjectKey> keysList =
                Arrays.stream(developerProjectKeyLists)
                        .collect(Collectors.toList());

        createKeysFromList(keysList);
        if (keysList.size() > 1) {
            log.info("Created " + keysList.size() + " new key records from JSON");
        } else if (keysList.size() == 1) {
            log.info("Created " + keysList.size() + "new key record from JSON");
        }
        return keysList.size();
    }

    @Override
    public int createKeysFromList(List<DeveloperProjectKey> keysList) {
        try {
            for (DeveloperProjectKey developerProjectKeyList : keysList) {
                long developerId = developerProjectKeyList.getDeveloperId();
                long projectId = developerProjectKeyList.getProjectId();

                insertSt.setLong(1, developerId);
                insertSt.setLong(2, projectId);
                insertSt.addBatch();
            }
            insertSt.executeBatch();
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
        }
        if (keysList.size() > 1) {
            log.info("Insert " + keysList.size() + " new key records");
        } else if (keysList.size() == 1) {
            log.info("Insert " + keysList.size() + "new key record");
        }
        return keysList.size();
    }
}
