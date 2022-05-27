package com.goit.javadev;

import com.goit.javadev.feature.storage.DataBaseInitService;
import com.goit.javadev.feature.storage.Storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbDemo {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();
        Connection connection = storage.getConnection();
        connection.setAutoCommit(false);
        String jdbc = "jdbc:h2:./testdb";
        String user = "sa";
        String password = "123";
        new DataBaseInitService().initDbFlyWay(storage);
        String sql = "CREATE TABLE IF NOT EXISTS testTable (id IDENTITY PRIMARY KEY, name VARCHAR(30))";

        connection.prepareStatement(sql);
    }
}
