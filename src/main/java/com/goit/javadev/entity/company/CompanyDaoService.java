package com.goit.javadev.entity.company;

import com.goit.javadev.feature.storage.Storage;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDaoService {
    private PreparedStatement insertSt;
    private PreparedStatement clearSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement getById;


    public CompanyDaoService(Connection connection) throws SQLException {

        insertSt = connection.prepareStatement(
                "INSERT INTO `homework_4`.companies (name, specialization) VALUES (?, ?)"
        );

        clearSt = connection.prepareStatement(
                "DELETE FROM `homework_4`.companies"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM `homework_4`.companies"
        );

        getById = connection.prepareStatement(
                "SELECT id FROM `homework_4`.companies"
        );
    }

    public void clearTable() throws SQLException {
        clearSt.executeUpdate();
    }

    public long createNewCompany(Company company) throws SQLException {
            insertSt.setString(1, company.getName());
            insertSt.setString(2, company.getSpecialization());
            insertSt.executeUpdate();
        long id;

        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }
        return id;
    }

    public Company getCompanyById(long id) throws SQLException {
        getById.setLong(1, id);

        try(ResultSet rs = getById.executeQuery()) {
            if(!rs.next()){
                return null;
            }
            Company company = new Company();
            company.setId(id);
            company.setName(rs.getNString("name"));
            company.setSpecialization("specialization");

            return company;
        }


    }

    public void createNewCompaniesFromJsonFile(String jsonFilePath) throws SQLException {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        String inString = "";
        try {
            inString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(jsonFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Company[] companies = gson.fromJson(inString, Company[].class);
        List<Company> companyList =
                Arrays.stream(companies)
                        .collect(Collectors.toList());

        for (int i = 0; i < companyList.size(); i++) {
            String name = companyList.get(i).getName();
            String specialization = companyList.get(i).getSpecialization();
            insertSt.setString(1, name);
            insertSt.setString(2, specialization);
            insertSt.addBatch();
        }
        insertSt.executeBatch();
    }
}