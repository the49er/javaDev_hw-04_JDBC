package com.goit.javadev.feature.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StorageTemp {
    Connection connection;
    String initDbFilePath;

    public StorageTemp(String connectionUrl, String connectionUser, String connectionUserPassword, String initDbFilePath) {
        this.initDbFilePath = initDbFilePath;
        try {
            this.connection = DriverManager.getConnection(connectionUrl,
                    connectionUser, connectionUserPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String[] getSqlArr() {
        String sqlString = null;
        try {
            sqlString = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(initDbFilePath))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] sqlArr = sqlString.split("\n");
        return sqlArr;

    }

    public int executeUpdate(String sql) {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(sql);
            return 1;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }
    public int executeUpdates() {
        if (getSqlArr().length > 0) {
            for (String sql : getSqlArr()) {
                executeUpdate(sql);
            }
            return 1;
        }
        return -1;
    }
}
