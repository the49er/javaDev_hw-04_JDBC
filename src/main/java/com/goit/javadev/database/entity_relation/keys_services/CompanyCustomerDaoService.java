package com.goit.javadev.database.entity_relation.keys_services;

import com.goit.javadev.database.entity_relation.keys.CompanyCustomerKey;
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
public class CompanyCustomerDaoService implements ManyToMany<CompanyCustomerKey> {
    PreparedStatement insertSt;
    PreparedStatement deleteSt;
    PreparedStatement getMaxIdSt;
    PreparedStatement getAllSt;
    private static final String TABLE_NAME = "`homework_4`.company_customer";

    public CompanyCustomerDaoService(Connection connection) {
        try {
            insertSt = connection.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (company_id, customer_id) VALUES (?, ?)"
            );

            deleteSt = connection.prepareStatement(
                    "DELETE FROM " + TABLE_NAME + " WHERE company_id = ? and customer_id = ?"
            );

            getMaxIdSt = connection.prepareStatement(
                    "SELECT count(company_id) AS maxId FROM " + TABLE_NAME
            );

            getAllSt = connection.prepareStatement(
                    "SELECT company_id, customer_id FROM " + TABLE_NAME
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean insertKey(CompanyCustomerKey key) {
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            insertSt.setLong(1, key.getCompanyId());
            insertSt.setLong(2, key.getCustomerId());
            rs.next();
            insertSt.executeUpdate();
            log.info("Key " + key + " was created");
            return true;
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("Can't create key : " + key.getCompanyId() + ", " + key.getCustomerId() + ".\n" +
                    "Due to key already exists");
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            log.info("Key " + key + " wasn't created");
        }

        return false;
    }

    @Override
    public boolean deleteKey(long companyId, long customerId) {
        try {
            deleteSt.setLong(1, companyId);
            deleteSt.setLong(1, customerId);
            deleteSt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<List<CompanyCustomerKey>> getAll() {
        List<CompanyCustomerKey> list = new ArrayList<>();

        try (ResultSet rs = getAllSt.executeQuery()) {
            while (rs.next()) {
                CompanyCustomerKey companyCustomerKey = new CompanyCustomerKey();
                companyCustomerKey.setCompanyId(rs.getInt("company_id"));
                companyCustomerKey.setCustomerId(rs.getInt("customer_id"));
                list.add(companyCustomerKey);

            }
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
        }
        log.info("Received list of: " + list.size() + " company_customer keys");
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
        CompanyCustomerKey[] companyCustomerKeys = gson.fromJson(inString, CompanyCustomerKey[].class);
        List<CompanyCustomerKey> keysList =
                Arrays.stream(companyCustomerKeys)
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
    public int createKeysFromList(List<CompanyCustomerKey> keysList) {
        try {
            for (CompanyCustomerKey companyCustomerKey : keysList) {
                int companyId = companyCustomerKey.getCompanyId();
                int customerId = companyCustomerKey.getCustomerId();


                insertSt.setInt(1, companyId);
                insertSt.setInt(2, customerId);
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
