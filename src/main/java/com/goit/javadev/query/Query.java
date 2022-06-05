package com.goit.javadev.query;

import com.goit.javadev.exception.DaoException;
import com.goit.javadev.feature.storage.Storage;
import com.goit.javadev.tables.entity.developer.Developer;
import com.goit.javadev.tables.entity.skill.ProgramLang;
import com.goit.javadev.tables.entity.skill.SkillLevel;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class Query {
    private static final String SCHEMA = "`homework_4`.";
    Storage storage;
    Connection connection;
    PreparedStatement devsSalariesOnProjectSt;
    PreparedStatement devsOnProjectSt;
    PreparedStatement devsLangSt;
    PreparedStatement devsSkillLevelSt;
    PreparedStatement projectsSt;

    public Query() {
        this.storage = Storage.getInstance();
        this.connection = storage.getConnection();
        try {
            devsSalariesOnProjectSt = connection.prepareStatement("select "
                    + SCHEMA + "projects.id,  "
                    + SCHEMA + "projects.name,  " + SCHEMA + "projects.description, sum( "
                    + SCHEMA + "developers.salary) as projectCost from  "
                    + SCHEMA + "developer_project join  " + SCHEMA + "developers on  "
                    + SCHEMA + "developer_project.developer_id =  "
                    + SCHEMA + "developers.id join  " + SCHEMA + "projects on  "
                    + SCHEMA + "developer_project.project_id =  " + SCHEMA + "projects.id where  "
                    + SCHEMA + "projects.id = ?");

            devsOnProjectSt = connection.prepareStatement("select "
                    + SCHEMA + "developers.id as devId,  " + SCHEMA + "developers.name as devName,  "
                    + SCHEMA + "developers.age as devAge,  " + SCHEMA + "developers.gender as devGender,  "
                    + SCHEMA + "projects.name as projectName,  " + SCHEMA + "projects.description as projectDescription,  "
                    + SCHEMA + "projects.id as projectId from  " + SCHEMA + "developer_project join  "
                    + SCHEMA + "developers on  " + SCHEMA + "developer_project.developer_id =  "
                    + SCHEMA + "developers.id join  " + SCHEMA + "projects on  "
                    + SCHEMA + "developer_project.project_id =  " + SCHEMA + "projects.id where  "
                    + SCHEMA + "projects.id = ?");

            devsLangSt = connection.prepareStatement("select " + SCHEMA + "developers.id, "
                    + SCHEMA + "developers.name, " + SCHEMA + "developers.age, "
                    + SCHEMA + "developers.gender, " + SCHEMA + "developers.salary, "
                    + SCHEMA + "skills.programming_lang, " + SCHEMA + "skills.level from "
                    + SCHEMA + "developer_skill join " + SCHEMA + "developers on "
                    + SCHEMA + "developer_skill.developer_id = " + SCHEMA + "developers.id join "
                    + SCHEMA + "skills on " + SCHEMA + "developer_skill.skill_id = "
                    + SCHEMA + "skills.id where programming_lang = ?");

            devsSkillLevelSt = connection.prepareStatement("select " + SCHEMA + "developers.id, "
                    + SCHEMA + "developers.name, " + SCHEMA + "developers.age, "
                    + SCHEMA + "developers.gender, " + SCHEMA + "developers.salary, "
                    + SCHEMA + "skills.programming_lang, " + SCHEMA + "skills.level from "
                    + SCHEMA + "developer_skill join " + SCHEMA + "developers on "
                    + SCHEMA + "developer_skill.developer_id = " + SCHEMA + "developers.id join "
                    + SCHEMA + "skills on " + SCHEMA + "developer_skill.skill_id = "
                    + SCHEMA + "skills.id where level = ?");



            projectsSt = connection.prepareStatement("select  " + SCHEMA + "projects.date_contract as date,  "
                    + SCHEMA + "projects.name as projectName, count("
                    + SCHEMA + "developers.id) as devsOnProject from  "
                    + SCHEMA + "developer_project join  " + SCHEMA + "developers on  "
                    + SCHEMA + "developer_project.developer_id =  " + SCHEMA + "developers.id join  "
                    + SCHEMA + "projects on  " + SCHEMA + "developer_project.project_id =  "
                    + SCHEMA + "projects.id group by  " + SCHEMA + "projects.id order by date ASC");

        } catch (DaoException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getProjectCost(long id) {
        try {
            devsSalariesOnProjectSt.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = devsSalariesOnProjectSt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getNString("name");
                String description = rs.getString("description");
                int projectCost = rs.getInt("projectCost");
                System.out.println("Project ID: " + id +
                        "\nProject name: " + name +
                        "\nProject description: " + description +
                        "\nProject cost: " + projectCost);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getAllDevsOnProject(long id) {
        try {
            devsOnProjectSt.setLong(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = devsOnProjectSt.executeQuery()) {
            while (rs.next()) {
                long devId = rs.getLong("devId");
                String devName = rs.getNString("devName");
                int devAge = rs.getInt("devAge");
                Developer.Gender devGender = Developer.Gender.valueOf(rs.getString("devGender"));
                String projectName = rs.getNString("projectName");
                String projectDescription = rs.getNString("projectDescription");
                System.out.println("Developer ID: " + devId +
                        "\nDeveloper name: " + devName +
                        "\nDeveloper age: " + devAge +
                        "\nDeveloper gender: " + devGender +
                        "\nProject name: " + projectName +
                        "\nProject description: " + projectDescription +
                        "\nProject ID: " + id +
                        "\n----------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getAllDevsByProgramLang(String lang) {
        try {
            devsLangSt.setString(1, ProgramLang.getFromString(lang));//enum check
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = devsLangSt.executeQuery()) {
            while (rs.next()) {
                long devId = rs.getLong("id");
                String devName = rs.getNString("name");
                int devAge = rs.getInt("age");
                Developer.Gender devGender = Developer.Gender.valueOf(rs.getString("gender"));
                int devSalary = rs.getInt("salary");
                String programLang = rs.getNString("programming_lang");
                String level = rs.getNString("level");
                System.out.println("Developer ID: " + devId +
                        "\nDeveloper name: " + devName +
                        "\nDeveloper age: " + devAge +
                        "\nDeveloper gender: " + devGender +
                        "\nDeveloper salary: " + devSalary +
                        "\nProgramming language: " + programLang +
                        "\nSkill's level: " + level +
                        "\n----------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getAllDevsBySkillLevel(String skillLevel) {
        try {
            devsSkillLevelSt.setString(1, SkillLevel.getFromString(skillLevel));//enum check
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = devsSkillLevelSt.executeQuery()) {
            while (rs.next()) {
                long devId = rs.getLong("id");
                String devName = rs.getNString("name");
                int devAge = rs.getInt("age");
                Developer.Gender devGender = Developer.Gender.valueOf(rs.getString("gender"));
                int devSalary = rs.getInt("salary");
                String programLang = rs.getNString("programming_lang");
                String level = rs.getNString("level");
                System.out.println("Developer ID: " + devId +
                        "\nDeveloper name: " + devName +
                        "\nDeveloper age: " + devAge +
                        "\nDeveloper gender: " + devGender +
                        "\nDeveloper salary: " + devSalary +
                        "\nProgramming language: " + programLang +
                        "\nSkill's level: " + level +
                        "\n----------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void getAllProject() {

        try (ResultSet rs = projectsSt.executeQuery()) {
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                String projectName = rs.getNString("projectName");
                int devsOnProject = rs.getInt("devsOnProject");

                System.out.println("Project date: " + date +
                        "\nProject name: " + projectName +
                        "\nDevelopers on project: " + devsOnProject +
                        "\n----------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
