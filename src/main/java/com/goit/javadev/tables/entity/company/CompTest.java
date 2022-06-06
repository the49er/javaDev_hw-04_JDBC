package com.goit.javadev.tables.entity.company;

import com.goit.javadev.feature.storage.Storage;

import java.sql.Connection;
import java.sql.SQLException;

public class CompTest {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        String jsonFileCompOut = "files/out/companies.json";
        String jsonFileCompIn = "files/in/companies.json";
        connection.setAutoCommit(false);
        CompanyDaoService companyDbService = new CompanyDaoService(connection);
        //companyDbService.createNewCompaniesFromJsonFile(jsonFileCompIn);
        Company company5 = new Company(1, "SoftServ", "Software");
        companyDbService.insertNewEntity(company5);
        System.out.println(companyDbService.insertNewEntity(company5));

        //companyDbService.insertNewCompany(company1);
        //System.out.println(companyDbService.insertNewCompany(company1));
        System.out.println(companyDbService.getEntityById(1));
        //Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

//        Company company1 = new Company(1, "GameSoft", "Game Software");
//        Company company2 = new Company(2, "iNet Security", "Web Security");
//        Company company3 = new Company(3, "Global Link", "Networks");
//        Company company4 = new Company(4, "Soft Group", "Enterprise Solutions");
//        List<Company> companies = new ArrayList<>();
//
//        companies.add(company1);
//        companies.add(company2);
//        companies.add(company3);
//        companies.add(company4);
//        String outputString = gson.toJson(companies);
//        System.out.println(gson.toJson(companies));
//
//        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
//            fileWriter.write(outputString);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
