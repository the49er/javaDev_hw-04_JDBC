package com.goit.javadev.tables.entity.project;

import com.goit.javadev.feature.storage.Storage;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;

public class ProjectTest {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        String jsonFileCompIn = "files/in/projects.json";
        String jsonFileCompOut = "files/out/projects.json";
        ProjectDaoService projectDaoService = new ProjectDaoService(connection);
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

        projectDaoService.insertNewProjectsFromJsonFile(jsonFileCompIn);

//        List<Project> project = new ArrayList<>();
//        project.add(new Project(1, "CRM", "Customer Relationship Management", LocalDate.parse("2021-01-24"), 1, 4));
//        project.add(new Project(2, "ERP", "Enterprise Resource Planning", LocalDate.of(2022, 4, 12), 2, 3));
//        project.add(new Project(3, "SCM", "Software Configuration Management", LocalDate.of(2019, 5, 30), 3, 2));
//        project.add(new Project(4, "HMC", "Human Capital Management", LocalDate.of(2020, 9, 16), 4, 1));
//
//        LocalDate date = LocalDate.of(2022, 04, 12);
//        String js = gson.toJson(date);
//        System.out.println(js);
//
//        String outputString = gson.toJson(project);
//        System.out.println(gson.toJson(project));
//
//        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
//            fileWriter.write(outputString);
//        } catch (IOException e) {
//            e.printStackTrace();
//       }
//        Project pro = new Project(1, "CRM", "Customer Relationship Management", LocalDate.parse("2022-01-01"), 1, 4);
//        System.out.println(pro.getDate().toString());
//        projectDaoService.insertNewEntity(pro);

    }

}
