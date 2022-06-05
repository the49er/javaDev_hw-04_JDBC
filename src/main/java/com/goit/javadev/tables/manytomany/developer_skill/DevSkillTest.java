package com.goit.javadev.tables.manytomany.developer_skill;

import com.goit.javadev.feature.storage.Storage;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DevSkillTest {
    public static void main(String[] args) {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        String jsonFileCompOut = "files/out/developer_skill_keys.json";
        List<DeveloperSkillKey> developerProjectKeyList = List.of(
                new DeveloperSkillKey(3,3),
                new DeveloperSkillKey(3,6),
                new DeveloperSkillKey(4,2),
                new DeveloperSkillKey(4,5),
                new DeveloperSkillKey(1,4),
                new DeveloperSkillKey(1,1),
                new DeveloperSkillKey(2,7),
                new DeveloperSkillKey(2,12),
                new DeveloperSkillKey(5,12),
                new DeveloperSkillKey(5,9),
                new DeveloperSkillKey(6,11),
                new DeveloperSkillKey(7,6),
                new DeveloperSkillKey(7,2),
                new DeveloperSkillKey(8,10),
                new DeveloperSkillKey(9,9),
                new DeveloperSkillKey(10,10));


        String output = gson.toJson(developerProjectKeyList);

        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
           fileWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
