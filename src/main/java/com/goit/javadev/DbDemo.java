package com.goit.javadev;

import com.goit.javadev.tables.entity.company.Company;
import com.goit.javadev.tables.entity.company.CompanyDaoService;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DbDemo {
    public static void main(String[] args) throws SQLException {
        String jdbc = "jdbc:h2:mem:./testDataBase";
        String sqlCreateDataBase = "CREATE SCHEMA `homework_4`";
        String sqlCreateTableCompany = "CREATE TABLE `homework_4`.companies (id IDENTITY PRIMARY KEY, " +
                "name VARCHAR(100), specialization VARCHAR(100))";
        Connection connection = DriverManager.getConnection(jdbc);
        connection.createStatement().executeUpdate(sqlCreateDataBase);
        connection.createStatement().executeUpdate(sqlCreateTableCompany);

        CompanyDaoService companyDaoService = new CompanyDaoService(connection);

        Company companyTest = new Company(1, "TestCompany", "UnitTest");
        long id = companyDaoService.insertNewCompany(companyTest);
        System.out.println(id);
        Company createdCompany = companyDaoService.getCompanyById(id);
        System.out.println(createdCompany);
    }
}
