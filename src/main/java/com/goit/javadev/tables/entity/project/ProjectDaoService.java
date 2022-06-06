package com.goit.javadev.tables.entity.project;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ProjectDaoService implements crudEntityDAO<Project> {

    private static final String TABLE_NAME = "`homework_4`.projects";
    PreparedStatement insertSt;
    PreparedStatement updateEntityFieldsSt;
    PreparedStatement getEntityByIdSt;
    PreparedStatement getAllEntitiesSt;
    PreparedStatement deleteById;
    PreparedStatement clearTableSt;
    PreparedStatement getMaxIdSt;


    public ProjectDaoService(Connection connection) {
        try {
            getMaxIdSt = connection.prepareStatement(
                    "SELECT max(id) AS maxId FROM " + TABLE_NAME
            );

            insertSt = connection.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (name, description, date_contract, customer_id, company_id) VALUES (?, ?, ?, ?, ?)"
            );

            updateEntityFieldsSt = connection.prepareStatement(
                    "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, date_contract = ?," +
                            "customer_id = ?, company_id = ? WHERE id = ?"
            );


            getEntityByIdSt = connection.prepareStatement(
                    "SELECT id, name, description, date_contract, customer_id, company_id FROM " +
                            TABLE_NAME + " WHERE id = ?"
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
                log.info("Attention! " + ids.length + " records were deleted");
            } else if (ids.length == 1) {
                log.info("Attention! " + ids.length + " record was deleted");
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
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long insertNewEntity(Project project) {
        long id;
        try {
            insertSt.setString(1, project.getName());
            insertSt.setString(2, project.getDescription());
            insertSt.setString(3, project.getDate().toString());
            insertSt.setInt(4, project.getCustomerId());
            insertSt.setInt(5, project.getCompanyId());
            insertSt.executeUpdate();
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
            log.info("Project with id: " + id + " has been created");
            return id;
        } catch (SQLException e) {
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
        Project[] projects = gson.fromJson(inString, Project[].class);
        List<Project> projectList =
                Arrays.stream(projects)
                        .collect(Collectors.toList());

        insertNewEntities(projectList);
        if (projectList.size() > 1) {
            log.info("Created " + projectList.size() + " new records from JSON");
        } else if (projectList.size() == 1) {
            log.info("Created " + projectList.size() + "new record from JSON");
        }
        return projectList.size();
    }

    @Override
    public int insertNewEntities(List<Project> projectList) {
        try {
            for (Project project : projectList) {
                String name = project.getName();
                String description = project.getDescription();
                String dateContract = project.getDate().toString();
                int customerId = project.getCustomerId();
                int companyId = project.getCompanyId();

                insertSt.setString(1, name);
                insertSt.setString(2, description);
                insertSt.setString(3, dateContract);
                insertSt.setInt(4, customerId);
                insertSt.setInt(5, companyId);
                insertSt.addBatch();
            }
            insertSt.executeBatch();
            if (projectList.size() > 1) {
                log.info("Insert " + projectList.size() + " new records");
            } else if (projectList.size() == 1) {
                log.info("Insert " + projectList.size() + "new record");
            }
            return projectList.size();
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateEntityFieldsById(Project project, long id) {
        try {
            updateEntityFieldsSt.setString(1, project.getName());
            updateEntityFieldsSt.setString(2, project.getDescription());
            updateEntityFieldsSt.setString(3, project.getDate().toString());
            updateEntityFieldsSt.setInt(4, project.getCustomerId());
            updateEntityFieldsSt.setInt(5, project.getCompanyId());
            updateEntityFieldsSt.setLong(6, id);
            updateEntityFieldsSt.executeUpdate();
            log.info("Project with id: " + id + " has been updated");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Project getEntityById(long id) {
        try {
            getEntityByIdSt.setLong(1, id);
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = getEntityByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Project project = new Project();
            project.setId(id);
            project.setName(rs.getNString("name"));
            project.setDescription(rs.getString("description"));
            project.setDate(LocalDate.parse(rs.getString("date_contract")));
            project.setCustomerId(rs.getInt("customer_id"));
            project.setCompanyId(rs.getInt("company_id"));
            log.info("get Project by id: " + id);
            return project;
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Project> getAllEntities() {
        return getProjects(getAllEntitiesSt);
    }

    private List<Project> getProjects(PreparedStatement st) {
        try (ResultSet rs = st.executeQuery()) {
            List<Project> projects = new ArrayList<>();
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getLong("id"));
                project.setName(rs.getNString("name"));
                project.setDescription(rs.getString("description"));
                project.setDate(LocalDate.parse(rs.getString("date_contract")));
                project.setCustomerId(rs.getInt("customer_id"));
                project.setCompanyId(rs.getInt("company_id"));
                projects.add(project);
            }
            log.info("Received list of: " + projects.size() + " Projects");
            return projects;
        } catch (DaoException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
