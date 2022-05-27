package com.goit.javadev.entity.developer;

import com.goit.javadev.feature.storage.Storage;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeveloperDaoService {
    private PreparedStatement insertSt;
    private PreparedStatement updateAllFieldsSt;
    private PreparedStatement updateNameFieldSt;



    public DeveloperDaoService(Connection connection) throws SQLException {

        insertSt = connection.prepareStatement(
                "INSERT INTO `homework_4`.developers (name, age, gender, " +
                        "salary, company_id) VALUES (?, ?, ?, ?, ?)"
        );

        updateAllFieldsSt = connection.prepareStatement(
                "UPDATE `homework_4`.developers SET name = ?, age = ?, " +
                        "gender = ?, salary = ?, company_id = ? WHERE id = ?"
        );

        updateNameFieldSt = connection.prepareStatement(
                "UPDATE `homework_4`.developers SET name = ? WHERE id = ?"
        );
    }

    public void createNewDeveloper(Developer developer) throws SQLException {
        insertSt.setString(1, developer.getName());
        insertSt.setInt(2, developer.getAge());
        insertSt.setString(3, developer.getGender().name());
        insertSt.setInt(4, developer.getSalary());
        insertSt.setInt(5, developer.getCompanyId());
        insertSt.executeUpdate();
    }

    public void createNewDevelopersFromJsonFile(String jsonFilePath) throws SQLException {
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

    }

    public void createDeveloperFromList (List<Developer> developerList) throws SQLException {
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
        insertSt.executeBatch();
    }

    public boolean updateAllFieldsOfDeveloperById(Developer developer, long id){
        try {
            updateAllFieldsSt.setString(1, developer.getName());
            updateAllFieldsSt.setInt(2, developer.getAge());
            updateAllFieldsSt.setString(3, developer.getGender().name());
            updateAllFieldsSt.setInt(4, developer.getSalary());
            updateAllFieldsSt.setInt(5, developer.getCompanyId());
            updateAllFieldsSt.setLong(6, id);
            return updateAllFieldsSt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
