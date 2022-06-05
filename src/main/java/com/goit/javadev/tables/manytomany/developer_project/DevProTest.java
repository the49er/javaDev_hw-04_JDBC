package com.goit.javadev.tables.manytomany.developer_project;

import com.goit.javadev.feature.storage.Storage;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DevProTest {
    public static void main(String[] args) {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        DeveloperProjectDaoService developerProjectDaoService = new DeveloperProjectDaoService(connection);
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        String jsonFileCompOut = "files/out/developer_project_keys.json";
        List<DeveloperProjectKey> developerProjectKeyList = List.of(
                new DeveloperProjectKey(4,1),//4 1
                new DeveloperProjectKey(3,2),//3 2
                new DeveloperProjectKey(2,3),//2 3
                new DeveloperProjectKey(1,4),//1 4
                new DeveloperProjectKey(5,1),//5 1
                new DeveloperProjectKey(6,2),//6 2
                new DeveloperProjectKey(7,4),//7 4
                new DeveloperProjectKey(8,2),//8 2
                new DeveloperProjectKey(9,3),//9 3
                new DeveloperProjectKey(10,4));//10 4

        developerProjectDaoService.createKeysFromList(developerProjectKeyList);
//        String output = gson.toJson(developerProjectKeyList);
//
//        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
//           fileWriter.write(output);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
