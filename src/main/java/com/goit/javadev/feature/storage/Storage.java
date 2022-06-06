package com.goit.javadev.feature.storage;

import com.goit.javadev.feature.prefs.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Storage {
    private static final Storage INSTANCE = new Storage();
    private Connection connection;

    private Storage() {
        try {
            Config config = new Config();
            String connectionUrl = config.getString(Config.DB_JDBC_CONNECTION_URL);
            String connectionUser = config.getString(Config.DB_JDBC_USER_1);
            String connectionUserPassword = config.getString(Config.DB_JDBC_USER_PASSWORD_1);

            connection = DriverManager.getConnection(connectionUrl,
                    connectionUser, connectionUserPassword);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }

    }

    public static Storage getInstance(){
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close (){
        try{
            connection.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public int executeUpdate(String sql){
        try (Statement st = connection.createStatement()){
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


}
