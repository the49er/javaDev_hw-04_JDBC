package com.goit.javadev;

import com.goit.javadev.entity.company.CompanyDaoService;
import com.goit.javadev.entity.developer.DeveloperDaoService;
import com.goit.javadev.feature.prefs.Config;
import com.goit.javadev.feature.storage.DataBaseInitService;
import com.goit.javadev.feature.storage.Storage;

import java.sql.Connection;
import java.sql.SQLException;

public class AppConnectionDemoTest {
    public static void main(String[] args) throws SQLException {

        String companiesJsonFileIn = "files/in/companies.json"; //companies
        String developersJsonFileIn = "files/in/developers.json"; // developers
        //DataBaseInitialisation
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        new DataBaseInitService().initDbFlyWay(storage);

        //population of companies_table
        CompanyDaoService companyDbService = new CompanyDaoService(connection);
        companyDbService.createNewCompaniesFromJsonFile(companiesJsonFileIn);

        //population of developers_table
        DeveloperDaoService developerDbService = new DeveloperDaoService(connection);
        developerDbService.createNewDevelopersFromJsonFile(developersJsonFileIn);

        //CompanyDbService companyDbService = new CompanyDbService(storage);
        //companyDbService.createNewCompaniesFromJsonFile(jsonFileCompIn);
        //companyDbService.createNewCompany("Mac", "Food");
//        DeveloperDbService developer = new DeveloperDbService(storage);
       //developer.createNewDeveloper("Vlad", 38, "male", 5000, 2);
//        System.out.println(developer);
//        Connection connection = storage.getConnection();
//        Statement statement = connection.createStatement();

//        storage.executeUpdate("DROP TABLE IF EXISTS `test`.new_table");
//        storage.executeUpdate("DROP SCHEMA IF EXISTS `test`");
//        storage.executeUpdate("DROP TABLE IF EXISTS `test`.new_table");
        //storage.executeUpdate("CREATE DATABASE IF NOT EXISTS `test2`");
//        storage.executeUpdate("CREATE TABLE IF NOT EXISTS `homework_4`.new_table (id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(100) NOT NULL, PRIMARY KEY (id))");
//        storage.executeUpdate("INSERT INTO `homework_4`.new_table (name) VALUES ('Vova'), ('Stephen'), ('Naomi');");

//        String selectAll = "select * from `homework_4`.new_table";
//        ResultSet resultSet = statement.executeQuery(selectAll);
//        while (resultSet.next()){
//            System.out.println("resultSet.getInt(\"id\") = " + resultSet.getInt("id"));
//            System.out.println("resultSet.getNString(\"name\") = " + resultSet.getNString("name"));
            //System.out.println("resultSet.getInt(\"age\") = " + resultSet.getInt("age"));
//            System.out.println("resultSet.getNString(\"gender\") = " + resultSet.getNString("gender"));
//            System.out.println("resultSet.getInt(\"company_id\") = " + resultSet.getInt("company_id"));
//            System.out.println("resultSet.getInt(\"salary\") = " + resultSet.getInt("salary"));

        //}
//        resultSet.close();
//        statement.close();
    }

}
