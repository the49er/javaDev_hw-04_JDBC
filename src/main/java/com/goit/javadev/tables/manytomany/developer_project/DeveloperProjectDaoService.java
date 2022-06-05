package com.goit.javadev.tables.manytomany.developer_project;

import com.goit.javadev.exception.DaoException;
import com.goit.javadev.tables.manytomany.ManyToMany;
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
    private static final String TABLE_NAME = "`homework_4`.company_customer";

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
    public boolean deleteKey(int companyId, int customerId) {
        try {
            deleteSt.setInt(1, companyId);
            deleteSt.setInt(1, customerId);
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
            log.info("Created " + keysList.size() + " new records from JSON");
        } else if (keysList.size() == 1) {
            log.info("Created " + keysList.size() + "new record from JSON");
        }
        return keysList.size();
    }

    @Override
    public int createKeysFromList(List<DeveloperProjectKey> keysList) {
        for (DeveloperProjectKey developerProjectKeyList : keysList) {
            int developerId = developerProjectKeyList.getDeveloperId();
            int projectId = developerProjectKeyList.getProjectId();

            try {
                insertSt.setInt(1, developerId);
                insertSt.setInt(2, projectId);
                insertSt.addBatch();
                insertSt.executeBatch();
            } catch (DaoException | SQLException e) {
                e.printStackTrace();
            }
        }
        if (keysList.size() > 1) {
            log.info("Insert " + keysList.size() + " new records");
        } else if (keysList.size() == 1) {
            log.info("Insert " + keysList.size() + "new record");
        }
        return keysList.size();
    }
}
