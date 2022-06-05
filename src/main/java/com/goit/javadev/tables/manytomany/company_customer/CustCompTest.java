package com.goit.javadev.tables.manytomany.company_customer;

import com.goit.javadev.feature.storage.Storage;
import java.sql.Connection;
import java.sql.SQLException;


public class CustCompTest {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        String jsonFileCompIn = "files/in/customers.json";
        String jsonFileCompOut = "files/out/company_customer.json";
        CompanyCustomerDaoService companyCustomerDaoService = new CompanyCustomerDaoService(connection);
//        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
//
//        List<CompanyCustomerKey> companyCustomerKeyList = List.of(
//                new CompanyCustomerKey(1,4),
//                new CompanyCustomerKey(2, 3),
//                new CompanyCustomerKey(3, 1),
//                new CompanyCustomerKey(4, 2)
//        );
//        String toJson = gson.toJson(companyCustomerKeyList);
//        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
//            fileWriter.write(toJson);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Optional<List<CompanyCustomerKey>> all = companyCustomerDaoService.getAll();
//        all.stream()
//                .forEach(System.out::println);

        companyCustomerDaoService.insertKey(new CompanyCustomerKey(4,4));

    }
}
