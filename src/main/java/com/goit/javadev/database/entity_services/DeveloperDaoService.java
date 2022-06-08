package com.goit.javadev.database.entity_services;

import com.goit.javadev.database.exception.DaoException;
import com.goit.javadev.database.entity.developer.Developer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

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
public class DeveloperDaoService implements CrudEntityDAO<Developer> {

    private static final String TABLE_NAME = "`homework_4`.developers";
    PreparedStatement insertSt;
    PreparedStatement updateEntityFieldsSt;
    PreparedStatement updateNameFieldSt;
    PreparedStatement getEntityByIdSt;
    PreparedStatement getBySpecificFieldLikeSt;
    PreparedStatement getAllEntitiesSt;
    PreparedStatement deleteById;
    PreparedStatement clearTableSt;
    PreparedStatement getMaxIdSt;


    public DeveloperDaoService(Connection connection) {
        try {

            getMaxIdSt = connection.prepareStatement(
                    "SELECT max(id) AS maxId FROM " + TABLE_NAME
            );

            insertSt = connection.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (name, age, gender, " +
                            "salary, company_id) VALUES (?, ?, ?, ?, ?)"
            );

            updateEntityFieldsSt = connection.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET name = ?, age = ?, " +
                            "gender = ?, salary = ?, company_id = ? WHERE id = ?"
            );

            updateNameFieldSt = connection.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET name = ? WHERE id = ?"
            );

            getEntityByIdSt = connection.prepareStatement(
                    "SELECT id, name, age, gender, salary, company_id FROM " +
                            TABLE_NAME + " WHERE id = ?"
            );

            getBySpecificFieldLikeSt = connection.prepareStatement(
                    "SELECT id, name, age, gender, salary, company_id FROM " +
                            TABLE_NAME + " WHERE name LIKE ?"
            );

            getAllEntitiesSt = connection.prepareStatement(
                    "SELECT id, name, age, gender, salary, company_id FROM " + TABLE_NAME
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
    public boolean deleteById(long id) {
        try {
            deleteById.setLong(1, id);
            deleteById.executeUpdate();
            log.info("Developer with id: " + id + " has been deleted");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long insertNewEntity(Developer developer) {
        long id = 0;
        try {
            insertSt.setString(1, developer.getName());
            insertSt.setInt(2, developer.getAge());
            insertSt.setString(3, developer.getGender().name());
            insertSt.setInt(4, developer.getSalary());
            insertSt.setInt(5, developer.getCompanyId());
            insertSt.executeUpdate();

        } catch (JdbcSQLIntegrityConstraintViolationException exception){
            System.out.println("Null not allowed");
            return -1;
        } catch (NullPointerException | SQLException exception) {
            System.out.println("Developer was not created");
            return -1;
        }

        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            while (rs.next()) {
                id = rs.getLong("maxId");
            }
            log.info("Developer with id: " + id + " has been created");
            return id;
        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean clearTable() {
        try {
            clearTableSt.executeUpdate();
            log.info(TABLE_NAME + " has been cleared");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        Developer[] developers = gson.fromJson(inString, Developer[].class);
        List<Developer> developerList =
                Arrays.stream(developers)
                        .collect(Collectors.toList());

        insertNewEntities(developerList);
        if (developerList.size() > 1) {
            log.info("Created " + developerList.size() + " new records from JSON");
        } else if (developerList.size() == 1) {
            log.info("Created " + developerList.size() + "new record from JSON");
        }
        return developerList.size();
    }

    @Override
    public int insertNewEntities(List<Developer> developerList) {
        try {
            for (Developer developer : developerList) {
                String name = developer.getName();
                int age = developer.getAge();
                String gender = developer.getGender().name();
                int salary = developer.getSalary();
                int companyId = developer.getCompanyId();

                insertSt.setString(1, name);
                insertSt.setInt(2, age);
                insertSt.setString(3, gender);
                insertSt.setInt(4, salary);
                insertSt.setInt(5, companyId);
                insertSt.addBatch();
            }
            insertSt.executeBatch();
            if (developerList.size() > 1) {
                log.info("Insert " + developerList.size() + " new records");
            } else if (developerList.size() == 1) {
                log.info("Insert " + developerList.size() + "new record");
            }
            return developerList.size();
        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateEntityFieldsById(Developer developer, long id) {
        try {
            updateEntityFieldsSt.setString(1, developer.getName());
            updateEntityFieldsSt.setInt(2, developer.getAge());
            updateEntityFieldsSt.setString(3, developer.getGender().name());
            updateEntityFieldsSt.setInt(4, developer.getSalary());
            updateEntityFieldsSt.setInt(5, developer.getCompanyId());
            updateEntityFieldsSt.setLong(6, id);
            updateEntityFieldsSt.executeUpdate();
            log.info("Developer with id: " + id + " has been updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Developer getEntityById(long id) {
        try {
            getEntityByIdSt.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            Developer developer = new Developer();
            while (rs.next()) {
                developer.setId(id);
                developer.setName(rs.getNString("name"));
                developer.setAge(rs.getInt("age"));
                developer.setGender(Developer.Gender.valueOf(rs.getNString("gender")));
                developer.setSalary(rs.getInt("salary"));
                developer.setCompanyId(rs.getInt("company_id"));
            }
            log.info("get Developer by id: " + id);
            return developer;

        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Developer> getDeveloperBySpecificFieldLike(String query) throws SQLException {
        getBySpecificFieldLikeSt.setString(1, "%" + query + "%");
        log.info("get list of Developers by:  query: " + "\"" + query + "\"");
        return getDevelopers(getBySpecificFieldLikeSt);
    }

    @Override
    public List<Developer> getAllEntities() {
        return getDevelopers(getAllEntitiesSt);

    }

    private List<Developer> getDevelopers(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            List<Developer> developers = new ArrayList<>();
            while (rs.next()) {
                Developer developer = new Developer();
                developer.setId(rs.getLong("id"));
                developer.setName(rs.getNString("name"));
                developer.setAge(rs.getInt("age"));
                developer.setGender(Developer.Gender.valueOf(rs.getNString("gender")));
                developer.setSalary(rs.getInt("salary"));
                developer.setCompanyId(rs.getInt("company_id"));
                developers.add(developer);
            }
            log.info("Received list of: " + developers.size() + " Developers");
            return developers;
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
