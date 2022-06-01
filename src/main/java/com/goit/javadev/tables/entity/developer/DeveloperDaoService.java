package com.goit.javadev.tables.entity.developer;

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
public class DeveloperDaoService {

    private static final String TABLE_NAME = "`homework_4`.developers";
    private PreparedStatement insertSt;
    private PreparedStatement updateEntityFieldsSt;
    private PreparedStatement updateNameFieldSt;
    private PreparedStatement getEntityByIdSt;
    private PreparedStatement getBySpecificFieldLikeSt;
    private PreparedStatement getAllEntitiesSt;
    private PreparedStatement deleteById;
    private PreparedStatement clearTableSt;
    private PreparedStatement getMaxIdSt;


    public DeveloperDaoService(Connection connection) throws SQLException {

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
    }

    public int deleteEntitiesFromListById (long[] ids) throws SQLException {
        int result = 0;
        for (long id: ids) {
            deleteById.setLong(1, id);
            deleteById.addBatch();
            result++;
        }
        deleteById.executeBatch();
        if (ids.length > 1) {
            log.info("Attention! " + ids.length + " records were deleted");
        } else if (ids.length == 1) {
            log.info("Attention! " + ids.length + " record was deleted");
        }
        return result;
    }

    public boolean deleteById (long id) throws SQLException {
        try {
            deleteById.setLong(1, id);
            deleteById.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public long insertNewEntity(Developer developer) throws SQLException {
        insertSt.setString(1, developer.getName());
        insertSt.setInt(2, developer.getAge());
        insertSt.setString(3, developer.getGender().name());
        insertSt.setInt(4, developer.getSalary());
        insertSt.setInt(5, developer.getCompanyId());
        insertSt.executeUpdate();

        long id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }
        return id;
    }

    public void clearTable() throws SQLException {
        clearTableSt.executeUpdate();
        log.info(TABLE_NAME + " has been cleared");
    }

    public int insertNewDevelopersFromJsonFile(String jsonFilePath) throws SQLException {
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

        createDeveloperFromList(developerList);
        if (developerList.size() > 1) {
            log.info("Created " + developerList.size() + " new records from JSON");
        } else if (developerList.size() == 1) {
            log.info("Created " + developerList.size() + "new record from JSON");
        }
        return developerList.size();
    }

    public int createDeveloperFromList(List<Developer> developerList) throws SQLException {
        for (int i = 0; i < developerList.size(); i++) {

            String name = developerList.get(i).getName();
            int age = developerList.get(i).getAge();
            String gender = developerList.get(i).getGender().name();
            int salary = developerList.get(i).getSalary();
            int companyId = developerList.get(i).getCompanyId();

            insertSt.setString(1, name);
            insertSt.setInt(2, age);
            insertSt.setString(3, gender);
            insertSt.setInt(4, salary);
            insertSt.setInt(5, companyId);
            insertSt.addBatch();
        }
        if (developerList.size() > 1) {
            log.info("Insert " + developerList.size() + " new records");
        } else if (developerList.size() == 1) {
            log.info("Insert " + developerList.size() + "new record");
        }
        insertSt.executeBatch();

        return developerList.size();
    }

    public boolean updateEntityFieldsStOfDeveloperById(Developer developer, long id) {
        try {
            updateEntityFieldsSt.setString(1, developer.getName());
            updateEntityFieldsSt.setInt(2, developer.getAge());
            updateEntityFieldsSt.setString(3, developer.getGender().name());
            updateEntityFieldsSt.setInt(4, developer.getSalary());
            updateEntityFieldsSt.setInt(5, developer.getCompanyId());
            updateEntityFieldsSt.setLong(6, id);
            return updateEntityFieldsSt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Developer getEntityById(long id) throws SQLException {
        getEntityByIdSt.setLong(1, id);

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Developer developer = new Developer();
            developer.setId(id);
            developer.setName(rs.getNString("name"));
            developer.setAge(rs.getInt("age"));
            developer.setGender(Developer.Gender.valueOf(rs.getNString("gender")));
            developer.setSalary(rs.getInt("salary"));
            developer.setCompanyId(rs.getInt("company_id"));
            log.info("get Developer by id: " + id);
            return developer;
        }
    }

    public List<Developer> getDeveloperBySpecificFieldLike(String query) throws SQLException {
        getBySpecificFieldLikeSt.setString(1, "%" + query + "%");
        log.info("get list of Developers by:  query: " + "\"" + query + "\"");
        return getDevelopers(getBySpecificFieldLikeSt);
    }

    public List<Developer> getAllEntities() throws SQLException {
        return getDevelopers(getAllEntitiesSt);
    }

    private List<Developer> getDevelopers(PreparedStatement st) throws SQLException {
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
        }
    }
}
