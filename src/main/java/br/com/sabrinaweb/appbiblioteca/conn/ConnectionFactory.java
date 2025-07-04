package br.com.sabrinaweb.appbiblioteca.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = "dbc:mysql://localhost:3306/library";
        String user = "devroot";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }
}
