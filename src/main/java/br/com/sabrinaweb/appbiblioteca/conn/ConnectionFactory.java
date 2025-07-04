package br.com.sabrinaweb.appbiblioteca.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/library";
        String user = "root";
        String password = "devroot";
        return DriverManager.getConnection(url, user, password);
    }
}
