package com.goit.javadev.tables.entity.company;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CompanyDaoService implements crudEntityDAO<Company> {
    private static final String TABLE_NAME = "`homework_4`.";
    PreparedStatement insertSt;
    PreparedStatement updateEntityFieldsSt;
    PreparedStatement getEntityByIdSt;
    PreparedStatement getAllEntitiesSt;
    PreparedStatement deleteById;
    PreparedStatement clearTableSt;
    PreparedStatement getMaxIdSt;


    public CompanyDaoService(Connection connection) {
        try {
            insertSt = connection.prepareStatement(
                    "INSERT INTO `homework_4`.companies (name, specialization) VALUES (?, ?)"
            );

            updateEntityFieldsSt = connection.prepareStatement(
                    "UPDATE " + TABLE_NAME + "companies SET name = ?, specialization = ? WHERE id = ?"
            );

            clearTableSt = connection.prepareStatement(
                    "DELETE FROM `homework_4`.companies"
            );

            deleteById = connection.prepareStatement(
                    "DELETE FROM `homework_4`.companies WHERE id = ?"
            );

            getMaxIdSt = connection.prepareStatement(
                    "SELECT max(id) AS maxId FROM " + TABLE_NAME + "companies"
            );

            getEntityByIdSt = connection.prepareStatement(
                    "SELECT id, name, specialization FROM " + TABLE_NAME + "companies WHERE id = ?"
            );

            getAllEntitiesSt = connection.prepareStatement(
                    "SELECT id, name, name, specialization FROM " + TABLE_NAME + "companies"
            );
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean clearTable() {
        try {
            clearTableSt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            deleteById.setLong(1, id);
            deleteById.executeUpdate();
            log.info("company by id: " + id + " has been deleted");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public long insertNewEntity(Company company) {
        try {
            insertSt.setString(1, company.getName());
            insertSt.setString(2, company.getSpecialization());
            insertSt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        long id;

        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public Company getEntityById(long id) {
        try {
            getEntityByIdSt.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Company company = new Company();
            company.setId(id);
            company.setName(rs.getNString("name"));
            company.setSpecialization(rs.getNString("specialization"));
            return company;
        }catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Company> getAllEntities() {
        return getCompanies(getAllEntitiesSt);
    }

    @Override
    public int deleteEntitiesFromListById(long[] ids) {
        int result = 0;
        try {
            for (long id : ids) {
                deleteById.setLong(1, id);
                deleteById.addBatch();
                result++;
            }
            deleteById.executeBatch();
            if (ids.length > 1) {
                log.info("Attention! " + result + " records were deleted");
            } else if (ids.length == 1) {
                log.info("Attention! " + result + " record was deleted");
            }
            return result;
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int insertEntitiesFromJsonFile(String jsonFilePath) {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        String inString = "";
        try {
            inString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Company[] companies = gson.fromJson(inString, Company[].class);
        List<Company> companyList =
                Arrays.stream(companies)
                        .collect(Collectors.toList());

        insertNewEntities(companyList);
        if (companyList.size() > 1) {
            log.info("Created " + companyList.size() + " new records from JSON");
        } else if (companyList.size() == 1) {
            log.info("Created " + companyList.size() + "new record from JSON");
        }
        return companyList.size();
    }

    @Override
    public boolean updateEntityFieldsById(Company element, long id) {
        try {
            updateEntityFieldsSt.setString(1, element.getName());
            updateEntityFieldsSt.setString(2, element.getSpecialization());
            updateEntityFieldsSt.setLong(3, id);
            updateEntityFieldsSt.executeUpdate();
            log.info("Company with id: " + id + " has been updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int insertNewEntities(List<Company> companyList) {
        try {
            for (Company company : companyList) {
                String name = company.getName();
                String specialization = company.getSpecialization();
                insertSt.setString(1, name);
                insertSt.setString(2, specialization);
                insertSt.addBatch();

            }
            insertSt.executeBatch();
            if (companyList.size() > 1) {
                log.info("Insert " + companyList.size() + " new records");
            } else if (companyList.size() == 1) {
                log.info("Insert " + companyList.size() + "new record");
            }
            return companyList.size();
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    private List<Company> getCompanies(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            List<Company> companies = new ArrayList<>();
            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getLong("id"));
                company.setName(rs.getNString("name"));
                company.setSpecialization(rs.getString("specialization"));

                companies.add(company);
            }
            log.info("Received list of: " + companies.size() + " Developers");
            return companies;
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
