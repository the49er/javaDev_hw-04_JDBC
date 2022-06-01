package com.goit.javadev.tables.entity.project;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ProjectDaoService {

    private static final String TABLE_NAME = "`homework_4`.projects";
    private PreparedStatement insertSt;
    private PreparedStatement updateEntityFieldsSt;
    private PreparedStatement updateEntityNameFieldSt;
    private PreparedStatement getEntityByIdSt;
    private PreparedStatement getBySpecificFieldLikeSt;
    private PreparedStatement getAllEntitiesSt;
    private PreparedStatement deleteById;
    private PreparedStatement clearTableSt;
    private PreparedStatement getMaxIdSt;


    public ProjectDaoService(Connection connection) throws SQLException {

        getMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM " + TABLE_NAME
        );

        insertSt = connection.prepareStatement(
                "INSERT INTO " + TABLE_NAME + " (name, description, date_contract, customer_id, company_id) VALUES (?, ?, ?, ?, ?)"
        );

        updateEntityFieldsSt = connection.prepareStatement(
                "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, date_contract = ?" +
                        "customer_id = ?, company_id = ? WHERE id = ?"
        );

        updateEntityNameFieldSt = connection.prepareStatement(
                "UPDATE " + TABLE_NAME + " SET name = ? WHERE id = ?"
        );

        getEntityByIdSt = connection.prepareStatement(
                "SELECT id, name, description, date_contract, customer_id, company_id FROM " +
                        TABLE_NAME + " WHERE id = ?"
        );

        getBySpecificFieldLikeSt = connection.prepareStatement(
                "SELECT id, name, description, date_contract, customer_id, company_id FROM " +
                        TABLE_NAME + " WHERE name LIKE ?"
        );

        getAllEntitiesSt = connection.prepareStatement(
                "SELECT id, name, description, date_contract, customer_id, company_id FROM " + TABLE_NAME
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


    public long insertNewEntity(Project project) throws SQLException {
        insertSt.setString(1, project.getName());
        insertSt.setString(2, project.getDescription());
        insertSt.setString(3, project.getDate().toString());
        insertSt.setInt(4, project.getCustomerId());
        insertSt.setInt(5, project.getCompanyId());
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

    public int insertNewProjectsFromJsonFile(String jsonFilePath) throws SQLException {
        Gson gson = new Gson();
        String inString = null;
        try {
            inString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Project[] projects = gson.fromJson(inString, Project[].class);
        List<Project> projectList =
                Arrays.stream(projects)
                        .collect(Collectors.toList());

        createProjectFromList(projectList);
        if (projectList.size() > 1) {
            log.info("Created " + projectList.size() + " new records from JSON");
        } else if (projectList.size() == 1) {
            log.info("Created " + projectList.size() + "new record from JSON");
        }
        return projectList.size();
    }

    public int createProjectFromList(List<Project> projectList) throws SQLException {
        for (int i = 0; i < projectList.size(); i++) {

            String name = projectList.get(i).getName();
            String description = projectList.get(i).getDescription();
            String dateContract = projectList.get(i).getDate().toString();
            int customerId = projectList.get(i).getCustomerId();
            int companyId = projectList.get(i).getCompanyId();

            insertSt.setString(1, name);
            insertSt.setString(2, description);
            insertSt.setString(3, dateContract);
            insertSt.setInt(4, customerId);
            insertSt.setInt(5, companyId);
            insertSt.addBatch();
        }
        if (projectList.size() > 1) {
            log.info("Insert " + projectList.size() + " new records");
        } else if (projectList.size() == 1) {
            log.info("Insert " + projectList.size() + "new record");
        }
        insertSt.executeBatch();

        return projectList.size();
    }

    public boolean updateEntityFieldsStOfProjectById(Project project, long id) {
        try {
            updateEntityFieldsSt.setString(1, project.getName());
            updateEntityFieldsSt.setString(2, project.getDescription());
            updateEntityFieldsSt.setString(3, project.getDate().toString());
            updateEntityFieldsSt.setInt(4, project.getCustomerId());
            updateEntityFieldsSt.setInt(5, project.getCompanyId());
            updateEntityFieldsSt.setLong(6, id);
            return updateEntityFieldsSt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Project getEntityById(long id) throws SQLException {
        getEntityByIdSt.setLong(1, id);

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Project project = new Project();
            project.setId(id);
            project.setName(rs.getNString("name"));
            project.setDescription(rs.getString("description"));
            project.setDate(LocalDate.parse(rs.getNString("dateContract")));
            project.setCustomerId(rs.getInt("customer_id"));
            project.setCompanyId(rs.getInt("company_id"));
            log.info("get Project by id: " + id);
            return project;
        }
    }

    public List<Project> getProjectBySpecificFieldLike(String query) throws SQLException {
        getBySpecificFieldLikeSt.setString(1, "%" + query + "%");
        log.info("get list of Projects by:  query: " + "\"" + query + "\"");
        return getProjects(getBySpecificFieldLikeSt);
    }

    public List<Project> getAllEntities() throws SQLException {
        return getProjects(getAllEntitiesSt);
    }

    private List<Project> getProjects(PreparedStatement st) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            List<Project> projects = new ArrayList<>();
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getLong("id"));
                project.setName(rs.getNString("name"));
                project.setDescription(rs.getString("description"));
                project.setDate(LocalDate.parse(rs.getString("dateContract")));
                project.setCustomerId(rs.getInt("customer_id"));
                project.setCompanyId(rs.getInt("company_id"));
                projects.add(project);

            }
            log.info("Received list of: " + projects.size() + " Projects");
            return projects;
        }
    }
}
