package com.goit.javadev;

import com.goit.javadev.tables.entity.company.CompanyDaoService;
import com.goit.javadev.tables.entity.customer.CustomerDaoService;
import com.goit.javadev.tables.entity.developer.DeveloperDaoService;
import com.goit.javadev.tables.entity.project.ProjectDaoService;
import com.goit.javadev.feature.storage.DataBaseInitService;
import com.goit.javadev.feature.storage.Storage;
import com.goit.javadev.tables.manytomany.company_customer.CompanyCustomerDaoService;

import java.sql.Connection;
import java.sql.SQLException;

public class ProjectCapitalManagement {
    public static void main(String[] args) throws SQLException {

        String companiesJsonFileIn = "files/in/companies.json"; //companies
        String developersJsonFileIn = "files/in/developers.json"; // developers
        String projectsJsonFileIn = "files/in/projects.json"; // projects
        String customersJsonFileIn = "files/in/customers.json"; // customers
        String compCustomersJsonFileIn = "files/in/company_customer.json"; // company_customer

        //DataBaseInitialisation
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        new DataBaseInitService().initDbFlyWay(storage);

        //population of companies_table
        CompanyDaoService companyDbService = new CompanyDaoService(connection);
        companyDbService.insertNewCompaniesFromJsonFile(companiesJsonFileIn);

        //population of developers_table
        DeveloperDaoService developerDbService = new DeveloperDaoService(connection);
        developerDbService.insertEntitiesFromJsonFile(developersJsonFileIn);

        //population of customers_table
        CustomerDaoService customerDaoService = new CustomerDaoService(connection);
        customerDaoService.insertEntitiesFromJsonFile(customersJsonFileIn);

        //population of projects_table
        ProjectDaoService projectDaoService = new ProjectDaoService(connection);
        projectDaoService.insertEntitiesFromJsonFile(projectsJsonFileIn);

        //population of company_customer table (keys)
        CompanyCustomerDaoService companyCustomerDaoService = new CompanyCustomerDaoService(connection);
        companyCustomerDaoService.insertKeysFromJsonFile(compCustomersJsonFileIn);
    }
}
