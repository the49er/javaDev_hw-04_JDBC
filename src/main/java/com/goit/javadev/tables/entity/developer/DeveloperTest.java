package com.goit.javadev.tables.entity.developer;

import com.goit.javadev.feature.storage.Storage;

import java.sql.Connection;
import java.sql.SQLException;

public class DeveloperTest {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        String jsonFileCompIn = "files/in/developers.json";
        String jsonFileCompOut = "files/out/developers.json";
        DeveloperDaoService developerDbService = new DeveloperDaoService(connection);

        System.out.println(developerDbService.getEntityById(10));
//        List<Developer> fieldLike = developerDbService.getAllEntities();
//        System.out.println(fieldLike.size());
//        fieldLike.stream()
//                .forEach(System.out::println);
//
//        List<Developer> list = developerDbService.getDeveloperBySpecificFieldLike("I");
//        list.stream()
//                .forEach(System.out::println);
//        System.out.println(list.size());
//        developerDbService.createNewDevelopersFromJsonFile(jsonFileCompIn);
//        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
//        Developer developerYura = new Developer(1, "Ivan", 23, other, 3750, 1);
//        developerDbService.updateAllFieldsOfDeveloperById(developerYura, 1);
//        System.out.println(developerDbService.updateAllFieldsOfDeveloperById(developerYura, 1));
//        List<Developer> developers = new ArrayList<>();
//        developers.add(new Developer(1, "Vitaly", 25, "male", 3500, 1));
//        developers.add(new Developer(2, "Irina", 31, "female", 3400, 2));
//        developers.add(new Developer(3, "Sabrina", 38, "other", 4750, 3));
//        developers.add(new Developer(4, "Yehor", 42, "male", 4100, 4));
//        developers.add(new Developer(5, "Sergiy", 29, "male", 4800, 4));
//        developers.add(new Developer(6, "Olena", 19, "other", 2100, 1));
//        developers.add(new Developer(7, "Oksana", 42, "female", 4200, 2));
//        developers.add(new Developer(8, "John", 43, "male", 3600, 1));
//        developers.add(new Developer(9, "Dmytro", 35, "male", 5500, 3));
//        developers.add(new Developer(10, "Helga", 22, "female", 2000, 2));
//
//        String outputString = gson.toJson(developers);
//        System.out.println(gson.toJson(developers));
//
//        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
//            fileWriter.write(outputString);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
