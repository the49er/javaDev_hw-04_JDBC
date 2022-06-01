package com.goit.javadev.tables.entity.customer;

import com.goit.javadev.feature.storage.Storage;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerTest {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        String jsonFileCompIn = "files/in/customers.json";
        String jsonFileCompOut = "files/out/customers.json";
        CustomerDaoService customerDaoService = new CustomerDaoService(connection);
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

        List<Customer> customerList = List.of(
                new Customer(1, "Peri", "Building Constructions"),
                new Customer(2, "Mail Express", "Logistic Company"),
                new Customer(3, "MoneyBank", "Banking"),
                new Customer(4, "BetStore", "Gambling")
        );
        String js = gson.toJson(customerList);

        try(FileWriter fileWriter = new FileWriter(jsonFileCompOut)) {
            fileWriter.write(js);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
