package application;

import java.sql.*;

public class ConnectToDatabase {
    public final static String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
    public final static String DB_URL = "jdbc:mysql://localhost/MyDatabase?serverTimezone=UTC";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "";

    public static Connection connectToDatabase(){
        try {
            return DriverManager.getConnection(
                    ConnectToDatabase.DB_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection connectToServer(){
        try {
            return DriverManager.getConnection(
                    ConnectToDatabase.MYSQL_SERVER_URL,
                    ConnectToDatabase.USERNAME,
                    ConnectToDatabase.PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

};
