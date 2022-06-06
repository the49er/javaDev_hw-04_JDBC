package com.goit.javadev;

import com.goit.javadev.feature.storage.StorageTemp;
import com.goit.javadev.tables.entity.company.CompanyDaoService;
import com.goit.javadev.tables.entity.customer.CustomerDaoService;
import com.goit.javadev.tables.entity.developer.DeveloperDaoService;
import com.goit.javadev.tables.entity.project.ProjectDaoService;
import com.goit.javadev.feature.storage.DataBaseInitService;
import com.goit.javadev.feature.storage.Storage;
import com.goit.javadev.tables.entity.skill.SkillDaoService;
import com.goit.javadev.tables.manytomany.company_customer.CompanyCustomerDaoService;
import com.goit.javadev.tables.manytomany.developer_project.DeveloperProjectDaoService;
import com.goit.javadev.tables.manytomany.developer_skill.DeveloperSkillDaoService;


import java.sql.Connection;
import java.util.Scanner;

public class ProjectCapitalManagement {
    public static void main(String[] args) {

        String companiesJsonFileIn = "files/in/companies.json"; //companies
        String developersJsonFileIn = "files/in/developers.json"; // developers
        String projectsJsonFileIn = "files/in/projects.json"; // projects
        String customersJsonFileIn = "files/in/customers.json"; // customers
        String skillsJsonFileIn = "files/in/skills.json"; // skills
        String compCustomerKeysJsonFileIn = "files/in/company_customer_keys.json"; // company_customer
        String devProjectKeysJsonFileIn = "files/in/developer_project_keys.json"; // developer_project
        String devSkillKeysJsonFileIn = "files/in/developer_skill_keys.json"; // developer_skill

        //input URL, USER and PASSWORD from your DataBase
        String connectionUrl = "jdbc:mysql://localhost:3306/homework_4";
        String connectionUser = "root";
        System.out.println("Input password for user: " + connectionUser + "\r");
        String connectionUserPassword = new Scanner(System.in).nextLine();
        String pathToFile = "sql/initDB.sql";

        StorageTemp storageTemp = new StorageTemp(connectionUrl, connectionUser,
                connectionUserPassword, pathToFile);
        storageTemp.executeUpdates();



        //DataBaseInitialisation with test_user
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        new DataBaseInitService().initDbFlyWay(storage);

        //population of companies table
        CompanyDaoService companyDbService = new CompanyDaoService(connection);
        companyDbService.insertEntitiesFromJsonFile(companiesJsonFileIn);

        //population of developers table
        DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
        developerDaoService.insertEntitiesFromJsonFile(developersJsonFileIn);

        //population of customers table
        CustomerDaoService customerDaoService = new CustomerDaoService(connection);
        customerDaoService.insertEntitiesFromJsonFile(customersJsonFileIn);

        //population of projects table
        ProjectDaoService projectDaoService = new ProjectDaoService(connection);
        projectDaoService.insertEntitiesFromJsonFile(projectsJsonFileIn);

        //population of skills table
        SkillDaoService skillDaoService = new SkillDaoService(connection);
        skillDaoService.insertEntitiesFromJsonFile(skillsJsonFileIn);

        //population of company_customer table (keys)
        CompanyCustomerDaoService companyCustomerDaoService = new CompanyCustomerDaoService(connection);
        companyCustomerDaoService.insertKeysFromJsonFile(compCustomerKeysJsonFileIn);

        //population of developer_project table (keys)
        DeveloperProjectDaoService developerProjectDaoService = new DeveloperProjectDaoService(connection);
        developerProjectDaoService.insertKeysFromJsonFile(devProjectKeysJsonFileIn);

        //population of developer_skill table (keys)
        DeveloperSkillDaoService developerSkillDaoService = new DeveloperSkillDaoService(connection);
        developerSkillDaoService.insertKeysFromJsonFile(devSkillKeysJsonFileIn);
    }
}
